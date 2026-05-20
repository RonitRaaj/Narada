package com.narada.backend.dTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClipboardRequestDTO {
    @NotBlank(message = "Clipboard content cannot be empty")
    private String content;
}