package com.narada.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sessions")
public class Session {
    
    @Id
    @Column(name = "session_id", length = 6)
    private String sessionId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_devices", joinColumns = @JoinColumn(name = "session_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private Set<DeviceType> devices = new HashSet<>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.devices = new HashSet<>();
    }
}