package com.narada.backend.dTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClipboardResponseDTO {
    private Long id;
    private String content;
    private String sourceDevice;
    private LocalDateTime createdAt;
}