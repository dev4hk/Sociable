package com.example.chat.entity;

import com.example.chat.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String name;

    private String image;

    @ElementCollection
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

}
