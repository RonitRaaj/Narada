package com.narada.backend.controller;

import com.narada.backend.dTO.sessiondTO.EnterSessionDTO;
import com.narada.backend.dTO.sessiondTO.SessionResponseDTO;
import com.narada.backend.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<SessionResponseDTO> createSession() {
        return ResponseEntity.ok(sessionService.createSession());
    }

    @PostMapping("/enter")
    public ResponseEntity<String> enterSession(@Valid @RequestBody EnterSessionDTO request) {
        String token = sessionService.enterSession(request);
        return ResponseEntity.ok(token);
    }
}