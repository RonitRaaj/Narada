package com.narada.backend.controller;

import com.narada.backend.dTO.sessiondTO.EnterSessionDTO;
import com.narada.backend.dTO.sessiondTO.SessionResponseDTO;
import com.narada.backend.model.DeviceType;
import com.narada.backend.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<Map<String , String>> createSession(@RequestBody Map<String, String> body) {
        String deviceType = body.get("deviceType");
        DeviceType deviceTypes = DeviceType.valueOf(deviceType);
        String newRoomId = sessionService.createSession().getSessionId(); 
        EnterSessionDTO dto = new EnterSessionDTO(newRoomId , deviceTypes);
        String jwtToken = sessionService.enterSession(dto);

       return ResponseEntity.ok(Map.of(
        "token", jwtToken,
        "sessionId", newRoomId
        ));
    }

    @PostMapping("/enter")
    public ResponseEntity<String> enterSession(@Valid @RequestBody EnterSessionDTO request) {
        String token = sessionService.enterSession(request);
        return ResponseEntity.ok(token);
    }
}