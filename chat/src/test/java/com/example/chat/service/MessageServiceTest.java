package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.fixture.ChatFixture;
import com.example.chat.fixture.MessageFixture;
import com.example.chat.fixture.UserFixture;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private ChatService chatService;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private FileService fileService;

    @MockBean
    private UserService userService;


    @Test
    void create_chat() throws IOException {
        Long chatId = 1L;
        String testToken = "AABB";
        UserModel userModel = new UserModel();
        userModel.setId(1);
        User user1 = UserModel.toEntity(userModel);
        User user2 = UserFixture.get(2);
        Chat chat = ChatFixture.get(user1, user2);
        String content = "content";
        Message message = MessageFixture.get(content);

        MockMultipartFile testFile = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        when(chatService.findChatById(chatId, testToken)).thenReturn(chat);
        when(fileService.upload(any(), any())).thenReturn(any());
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(messageRepository.save(message)).thenReturn(message);
        when(chatRepository.save(chat)).thenReturn(chat);
        assertDoesNotThrow(() -> messageService.createMessage(testToken, chatId, content, testFile));
        assertTrue(chat.getMessages().size() == 1);
    }

    @Test
    void find_messages_by_chat() {
        Long chatId = 1L;
        String testToken = "AABB";
        UserModel userModel = new UserModel();
        userModel.setId(1);
        User user1 = UserModel.toEntity(userModel);
        User user2 = UserFixture.get(2);
        Chat chat = ChatFixture.get(user1, user2);
        String content = "content";
        Message message = MessageFixture.get(content);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(userModel)));
        when(chatService.findChatById(chatId, testToken)).thenReturn(chat);
        when(messageRepository.findByChatId(chatId)).thenReturn(List.of());

        assertDoesNotThrow(() -> messageService.findMessageByChatId(chatId, testToken));
    }



}
