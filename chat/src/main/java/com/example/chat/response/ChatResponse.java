package com.example.chat.response;

import com.example.chat.entity.Chat;
import com.example.chat.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatResponse {
    private Long id;
    private List<User> users;

    public static ChatResponse fromChat(Chat chat) {
        return new ChatResponse(
                chat.getId(),
                chat.getUsers()
        );
    }
}
