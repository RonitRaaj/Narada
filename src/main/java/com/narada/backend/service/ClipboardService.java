package com.narada.backend.service;

import com.narada.backend.dTO.ClipboardRequestDTO;
import com.narada.backend.dTO.ClipboardResponseDTO;
import com.narada.backend.dTO.sessiondTO.EnterSessionDTO;
import com.narada.backend.model.ClipboardItem;
import com.narada.backend.model.Session;
import com.narada.backend.repository.*;
import com.narada.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClipboardService {

    private final ClipboardRepository clipboardItemRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public ClipboardResponseDTO saveItem(ClipboardRequestDTO request, EnterSessionDTO sessionContext) {

        Session session = sessionRepository.findById(sessionContext.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session active room not found."));

        
        ClipboardItem item = new ClipboardItem();
        item.setContent(request.getContent());
        item.setSourceDevice(sessionContext.getDeviceType());
        item.setSession(session);

        ClipboardItem savedItem = clipboardItemRepository.save(item);

        return new ClipboardResponseDTO(
                savedItem.getId(),
                savedItem.getContent(),
                savedItem.getSourceDevice().name(),
                savedItem.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ClipboardResponseDTO> getSessionHistory(String sessionId) {
        return clipboardItemRepository.findBySessionIdOrderByCreatedAtDesc(sessionId)
                .stream()
                .map(item -> new ClipboardResponseDTO(
                        item.getId(),
                        item.getContent(),
                        item.getSourceDevice().name(),
                        item.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}