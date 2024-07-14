package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.fixture.UserFixture;
import com.example.chat.model.User;
import com.example.chat.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockBean
    private ChatRepository chatRepository;

    private User user1;
    private User user2;
    private Chat chat;

    @BeforeEach
    void setup() {
        user1 = UserFixture.get(1);
        user2 = UserFixture.get(2);
        chat = new Chat();
        chat.setUsers(List.of(user1, user2));
    }

    @Test
    void create_chatroom() {
        when(chatRepository.findChatByUsers(user1, user2)).thenReturn(Optional.empty());
        when(chatRepository.save(any())).thenReturn(mock(Chat.class));

        assertDoesNotThrow(() -> chatService.create(1, 2));
    }

    @Test
    void find_chat_by_id() {
        when(chatRepository.findById(anyInt())).thenReturn(mock(Chat.class));
        assertDoesNotThrow(() -> chatService.findChatById(1));
    }

    @Test
    void find_chats_by_user() {
        when(chatRepository.findChatByUser(user1)).thenReturn(List.of());
        assertDoesNotThrow(() -> chatService.findChatsByUser(user1.getId()));
    }





}