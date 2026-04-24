package com.narada.backend.controller;

import com.narada.backend.model.ClipboardItem;
import com.narada.backend.model.Session;
import com.narada.backend.repository.SessionRepository;
import com.narada.backend.service.ClipboardService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/session")
    public String createSession(){
        String sessionId = UUID.randomUUID().toString().substring(0,6);
        sessionRepo.save(new Session(sessionId, LocalDateTime.now()));
        return sessionId;
    }

    @PostMapping("/clipboard")
    public ClipboardItem addItem(@RequestBody ClipboardItem item){
        return service.save(item);
    }

    @GetMapping("/clipboard/{sessionId}")
    public List<ClipboardItem> getItems(@PathVariable String sessionId){
        return service.getBySession(sessionId);
    }
}
