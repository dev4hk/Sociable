package com.example.chat.response;

import com.example.chat.entity.Message;
import com.example.chat.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse {
    private Long id;

    private String content;

    private String fileName;

    private String filePath;

    private String contentType;

    private User user;

    public static MessageResponse fromMessage(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getFileName(),
                message.getFilePath(),
                message.getContentType(),
                message.getUser()
        );
    }
}
