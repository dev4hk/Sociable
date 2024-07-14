package com.example.chat.fixture;

import com.example.chat.entity.Chat;
import com.example.chat.entity.User;

public class ChatFixture {

    public static Chat get(User user1, User user2) {
        Chat chat = new Chat();
        chat.getUsers().add(user1);
        chat.getUsers().add(user2);
        chat.setId(1L);
        return chat;
    }
}
