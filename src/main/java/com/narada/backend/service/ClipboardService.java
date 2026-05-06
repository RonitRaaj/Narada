package com.narada.backend.service;

import com.narada.backend.dTO.ClipboardRequestDTO;
import com.narada.backend.dTO.ClipboardResponseDTO;
import com.narada.backend.model.ClipboardItem;
import com.narada.backend.model.Session;
import com.narada.backend.repository.ClipboardRepository;
import com.narada.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClipboardService {
    private final ClipboardRepository repository;
    private final SessionRepository sessionRepository;

    @Transactional
    public ClipboardResponseDTO save(ClipboardRequestDTO request) {
        ClipboardItem item = new ClipboardItem();
        item.setContent(request.getContent());
        item.setSessionId(request.getSessionId());
        item.setDeviceId(request.getDeviceId());
        item.setType(request.getType());

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

        repository.save(item);

        return ClipboardResponseDTO.builder()
            .sessionId(item.getSessionId())
            .content(item.getContent())
            .deviceId(item.getDeviceId())
            .createdAt(item.getCreatedAt())
            .build();
    }

    public List<ClipboardResponseDTO> getBySession(String sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new IllegalArgumentException("Session not found.");
        }

        List<ClipboardItem> items = repository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        return items.stream()
        .map(item -> ClipboardResponseDTO.builder()
                .sessionId(item.getSessionId())
                .content(item.getContent())
                .deviceId(item.getDeviceId())
                .createdAt(item.getCreatedAt())
                .build())
        .collect(Collectors.toList());
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
            log.info("TEST CLEANUP: Successfully removed " + expiredSessions.size() + " expired test sessions.");
        }
    }
}