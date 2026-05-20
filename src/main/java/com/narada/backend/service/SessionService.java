package com.narada.backend.service;

import com.narada.backend.dTO.sessiondTO.EnterSessionDTO;
import com.narada.backend.dTO.sessiondTO.SessionResponseDTO;
import com.narada.backend.model.Session;
import com.narada.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AuthService authService;

    @Transactional
    public SessionResponseDTO createSession() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (sessionRepository.existsById(code));

        Session session = new Session(code);
        sessionRepository.save(session);

        return new SessionResponseDTO(code);
    }

    @Transactional
    public String enterSession(EnterSessionDTO request) {
        Session session = sessionRepository.findById(request.getSessionId().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Session not found."));

        if (!session.getDevices().contains(request.getDeviceType())) {
            if (session.getDevices().size() >= 5) {
                throw new IllegalStateException("Device limit reached (Max 5 unique device types allowed).");
            }
            session.getDevices().add(request.getDeviceType());
            sessionRepository.save(session);
        }

        return authService.authenticateSessionEntry(request);
    }
}