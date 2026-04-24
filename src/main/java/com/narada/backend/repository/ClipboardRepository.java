package com.narada.backend.repository;

import com.narada.backend.model.ClipboardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClipboardRepository  extends JpaRepository<ClipboardItem, Long> {
    List<ClipboardItem> findBySessionIdOrderByCreatedAtDesc(String sessionId);
}
