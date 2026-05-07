package com.narada.backend.controller;

import com.narada.backend.dTO.ClipboardRequestDTO;
import com.narada.backend.dTO.ClipboardResponseDTO;
import com.narada.backend.model.Session;
import com.narada.backend.repository.SessionRepository;
import com.narada.backend.service.ClipboardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class ClipboardController {
    private final ClipboardService service;
    private final SessionRepository sessionRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/session")
    public String createSession() {
        String sessionId = UUID.randomUUID().toString().substring(0, 6);
        sessionRepo.save(new Session(sessionId, LocalDateTime.now()));
        return sessionId;
    }

    @PostMapping("/clipboard")
    public ResponseEntity<?> addItem(@Valid @RequestBody ClipboardRequestDTO request) {

        ClipboardResponseDTO saved = service.save(request);

            messagingTemplate.convertAndSend(
                    "/topic/clipboard/" + saved.getSessionId(), saved
            );

        return ResponseEntity.ok(saved); 
    }

    @GetMapping("/clipboard/{sessionId}")
    public ResponseEntity<?> getItems(@PathVariable String sessionId) {

        List<ClipboardResponseDTO> items = service.getBySession(sessionId);
        return ResponseEntity.ok(items);
    }
}