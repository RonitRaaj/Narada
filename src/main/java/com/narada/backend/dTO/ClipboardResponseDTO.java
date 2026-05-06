package com.narada.backend.dTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClipboardResponseDTO {
    private String sessionId;
    private String content;
    private String deviceId;
    private LocalDateTime createdAt;
}
