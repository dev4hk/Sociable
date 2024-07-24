package com.example.notification.entity;

import com.example.notification.enums.NotificationType;
import com.example.notification.model.NotificationArgs;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NotificationArgs args;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }


}
