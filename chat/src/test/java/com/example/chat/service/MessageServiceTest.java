package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.enums.ErrorCode;
import com.example.chat.exception.ChatException;
import com.example.chat.fixture.ChatFixture;
import com.example.chat.fixture.FileFixture;
import com.example.chat.fixture.MessageFixture;
import com.example.chat.fixture.UserFixture;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.response.Response;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private FileService fileService;

    @Mock
    private UserService userService;

    private Long chatId;
    private String testToken;
    private UserModel userModel;
    private User user1;
    private User user2;
    private Chat chat;
    private String content;
    private Message message;
    private MockMultipartFile file;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        chatId = 1L;
        testToken = "AABB";
        userModel = new UserModel();
        userModel.setId(1);
        user1 = UserModel.toEntity(userModel);
        user2 = UserFixture.get(2);
        chat = ChatFixture.get(user1, user2);
        content = "content";
        message = MessageFixture.get(content);
        file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
    }


    @Test
    void create_message() throws IOException {
        when(fileService.upload(file, testToken)).thenReturn(Response.success(FileFixture.get()));
        when(chatService.findChatById(chatId, testToken)).thenReturn(chat);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(messageRepository.save(message)).thenReturn(message);
        when(chatRepository.save(chat)).thenReturn(chat);
        assertDoesNotThrow(() -> messageService.createMessage(testToken, chatId, content, file));
        assertTrue(chat.getMessages().size() == 1);
    }

    @Test
    void find_messages_by_chat() {
        Message message = MessageFixture.get(content);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(chatService.findChatById(chatId, testToken)).thenReturn(chat);
        when(messageRepository.findByChatId(chatId)).thenReturn(List.of());

        assertDoesNotThrow(() -> messageService.findMessageByChatId(chatId, testToken));
    }

    @Test
    void create_message_without_both_content_and_file_throw_error() {
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(chatService.findChatById(chatId, testToken)).thenReturn(chat);
        ChatException exception = assertThrows(ChatException.class, () -> messageService.createMessage(testToken, 1L, null, null));
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void create_message_for_non_existing_user_throws_exception() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/users/profile", new HashMap<>(), null, new RequestTemplate());
        when(userService.getUserProfile(testToken)).thenThrow(new FeignException.NotFound(null, request, null, null));
        assertThrows(ChatException.class, () -> messageService.createMessage(testToken, 1L, content, file));
    }

}
