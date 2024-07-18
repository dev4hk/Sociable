package com.example.chat.response;

import com.example.chat.entity.Message;
import com.example.chat.model.FileInfo;
import com.example.chat.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse {
    private Long id;

    private String content;

    private FileInfo fileInfo;

    private User user;

    public static MessageResponse fromMessage(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getFileInfo(),
                message.getUser()
        );
    }
}
