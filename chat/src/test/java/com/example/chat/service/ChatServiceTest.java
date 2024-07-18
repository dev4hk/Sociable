package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.exception.ChatException;
import com.example.chat.fixture.UserFixture;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private UserModel userModel;
    private Chat chat;
    private String testToken;

    @BeforeEach
    void setup() {
        user1 = UserFixture.get(1);
        user2 = UserFixture.get(2);
        userModel = new UserModel();
        chat = new Chat();
        chat.setUsers(List.of(user1, user2));
        testToken = "AABB";
    }

    @Test
    void create_chatroom() {

        when(chatRepository.findChatByUsers(user1, user2)).thenReturn(Optional.empty());
        when(chatRepository.save(chat)).thenReturn(chat);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(userService.getOtherUserInfo(any(), any())).thenReturn(ResponseEntity.of(Optional.of(mock(UserModel.class))));

        assertDoesNotThrow(() -> chatService.create(2, testToken));
        assertEquals(2, chat.getUsers().size());
    }

    @Test
    void find_chat_by_id() {
        when(chatRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mock(Chat.class)));
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        assertDoesNotThrow(() -> chatService.findChatById(1L, testToken));
    }

    @Test
    void find_chats_by_user() {
        when(chatRepository.findByUsersId(1)).thenReturn(List.of());
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        assertDoesNotThrow(() -> chatService.findChatsByUser(testToken));
    }

    @Test
    void find_non_existing_chat_throw_error() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChatException.class, () -> chatService.findChatById(1L, testToken));
    }


}