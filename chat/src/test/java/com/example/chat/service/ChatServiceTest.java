package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.exception.ChatException;
import com.example.chat.fixture.UserFixture;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserService userService;

    private User user1;
    private User user2;
    private UserModel userModel1;
    private UserModel userModel2;
    private Chat chat;
    private String testToken;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user1 = UserFixture.get(1);
        user2 = UserFixture.get(2);
        userModel1 = new UserModel();
        userModel2 = new UserModel();
        chat = new Chat();
        chat.setUsers(List.of(user1, user2));
        testToken = "AABB";

    }

    @Test
    void create_chatroom() {

        when(chatRepository.findChatByUsers(user1, user2)).thenReturn(Optional.empty());
        when(chatRepository.save(any())).thenReturn(chat);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel1)));
        when(userService.getOtherUserInfo(2, testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel2)));

        Chat chat = assertDoesNotThrow(() -> chatService.create(2, testToken));
        assertEquals(2, chat.getUsers().size());
    }

    @Test
    void find_chat_by_id() {
        when(chatRepository.findById(1L)).thenReturn(Optional.of(this.chat));
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel1)));
        Chat chat = assertDoesNotThrow(() -> chatService.findChatById(1L, testToken));
        assertTrue(chat.getUsers().size() == 2);
    }

    @Test
    void find_chats_by_user() {
        when(chatRepository.findByUsersId(1)).thenReturn(List.of());
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel1)));
        List<Chat> chats = assertDoesNotThrow(() -> chatService.findChatsByUser(testToken));
        assertEquals(0, chats.size());
    }

    @Test
    void find_non_existing_chat_throw_error() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ChatException.class, () -> chatService.findChatById(1L, testToken));
    }

    @Test
    void create_chat_for_non_existing_user_throws_exception() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/users/profile", new HashMap<>(), null, new RequestTemplate());
        when(userService.getUserProfile(testToken)).thenThrow(new FeignException.NotFound(null, request, null, null));
        assertThrows(ChatException.class, () -> chatService.create(2, testToken));
    }

}