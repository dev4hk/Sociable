package com.example.chat.entity;

import com.example.chat.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    private String content;

    private String fileName;

    private String filePath;

    private String contentType;

    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

}