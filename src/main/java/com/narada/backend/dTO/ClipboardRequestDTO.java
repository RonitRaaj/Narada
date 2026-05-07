package com.narada.backend.dTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClipboardRequestDTO {
    @NotBlank(message = "Session ID cannot be empty")
    private String sessionId;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotBlank(message = "Device ID cannot be empty")
    private String deviceId;

    private String type = "text";
}
