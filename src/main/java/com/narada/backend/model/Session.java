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
public class Session {
    @Id
    private String sessionId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_devices", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "device_id")
    private Set<String> devices = new HashSet<>();

    public Session(String sessionId, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.devices = new HashSet<>();
    }
}