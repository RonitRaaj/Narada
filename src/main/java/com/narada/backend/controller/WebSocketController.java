package com.narada.backend.controller;

import com.narada.backend.model.ClipboardItem;
import com.narada.backend.service.ClipboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ClipboardService clipboardService;

    @MessageMapping("/clipboard")
    public void sendClipboard(ClipboardItem item){
        ClipboardItem saved = clipboardService.save(item);

        messagingTemplate.convertAndSend(
                "/topic/clipboard/" + item.getSessionId(), saved
        );
    }
}
