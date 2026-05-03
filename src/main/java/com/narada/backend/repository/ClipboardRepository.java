package com.narada.backend.repository;

import com.narada.backend.model.ClipboardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ClipboardRepository extends JpaRepository<ClipboardItem, Long> {
    List<ClipboardItem> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    @Transactional
    void deleteBySessionId(String sessionId);
}