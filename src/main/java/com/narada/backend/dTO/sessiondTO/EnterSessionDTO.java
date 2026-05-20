package com.narada.backend.dTO.sessiondTO;

import com.narada.backend.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnterSessionDTO {

    @NotBlank(message = "Session ID cannot be empty")
    @Size(min = 6, max = 6, message = "Session ID must be exactly 6 characters long")
    private String sessionId;
    
    @NotNull(message = "Device Type must be selected")
    @Pattern(regexp = "^(?i)(MOBILE|TABLET|LAPTOP|DESKTOP|SMART_TV)$")
    private DeviceType deviceType;
}