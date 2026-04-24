package com.narada.backend.service;

import com.narada.backend.model.ClipboardItem;
import com.narada.backend.repository.ClipboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClipboardService {
    private final ClipboardRepository repository;

    public ClipboardItem save(ClipboardItem item){
        return repository.save(item);
    }

    public List<ClipboardItem> getBySession(String sessionId){
        return repository.findBySessionIdOrderByCreatedAtDesc(sessionId);
    }
}
