package com.example.chat.entity;

import com.example.chat.model.FileInfo;
import com.example.chat.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "filePath", column = @Column(name = "content_file_path")),
            @AttributeOverride(name = "fileType", column = @Column(name = "content_file_type"))
    })
    private FileInfo fileInfo;

    private String contentType;

    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private Chat chat;

}