package com.example.chat.fixture;

import com.example.chat.entity.Message;

public class MessageFixture {
    public static Message get(String content) {
        Message message = new Message();
        message.setContent(content);
        return message;
    }
}
