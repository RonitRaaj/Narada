package com.narada.backend.controller;

import com.narada.backend.dTO.*;
import com.narada.backend.dTO.sessiondTO.*;
import com.narada.backend.service.AuthService;
import com.narada.backend.service.ClipboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clipboard")
@RequiredArgsConstructor
public class ClipboardController{

    private final ClipboardService clipboardService;
    private final AuthService authService;

    @PostMapping("/add")
    public ResponseEntity<ClipboardResponseDTO> addClip(
            @RequestHeader("X-Session-Token") String token,
            @Valid @RequestBody ClipboardRequestDTO request) {
        
        EnterSessionDTO sessionContext = authService.getSessionDataFromToken(token);
        
        ClipboardResponseDTO response = clipboardService.saveItem(request, sessionContext);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ClipboardResponseDTO>> getHistory(
            @RequestHeader("X-Session-Token") String token) {
        
        EnterSessionDTO sessionContext = authService.getSessionDataFromToken(token);
        
        List<ClipboardResponseDTO> history = clipboardService.getSessionHistory(sessionContext.getSessionId());
        return ResponseEntity.ok(history);
    }
}