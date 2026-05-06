package com.narada.backend.service;

import com.narada.backend.model.ClipboardItem;
import com.narada.backend.model.Session;
import com.narada.backend.repository.ClipboardRepository;
import com.narada.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClipboardService {
    private final ClipboardRepository repository;
    private final SessionRepository sessionRepository;

    @Transactional
    public ClipboardItem save(ClipboardItem item) {
        Session session = sessionRepository.findById(item.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found."));

        Set<String> linkedDevices = session.getDevices();

        if (!linkedDevices.contains(item.getDeviceId())) {
            if (linkedDevices.size() >= 5) {
                throw new IllegalStateException("Device limit reached (Max 5).");
            }
            linkedDevices.add(item.getDeviceId());
            sessionRepository.save(session);
        }

        return repository.save(item);
    }

    public List<ClipboardItem> getBySession(String sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new IllegalArgumentException("Session not found.");
        }
        return repository.findBySessionIdOrderByCreatedAtDesc(sessionId);
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupOldSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);

        List<Session> expiredSessions = sessionRepository.findAll().stream()
                .filter(session -> session.getCreatedAt().isBefore(threshold))
                .toList();

        for (Session session : expiredSessions) {
            repository.deleteBySessionId(session.getSessionId());
            sessionRepository.delete(session);
        }

        if (!expiredSessions.isEmpty()) {
            System.out.println("TEST CLEANUP: Successfully removed " + expiredSessions.size() + " expired test sessions.");
        }
    }
}