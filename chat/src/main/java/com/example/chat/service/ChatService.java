package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {
    public Chat create(Integer userId1, Integer userId2) {
        return null;
    }

    public Chat findChatById(Integer chatId) {
        return null;
    }

    public List<Chat> findChatsByUser(Integer userId) {
        return null;
    }
}
