package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.enums.ErrorCode;
import com.example.chat.exception.ChatException;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    private final ChatService chatService;

    private final ChatRepository chatRepository;

    private final FileService fileService;


    @Transactional
    public Message createMessage(String token, Long chatId, String content, MultipartFile file) {
        User user = getUser(token);
        Chat chat = chatService.findChatById(chatId, token);

        if (content == null && file == null) {
            throw new ChatException(ErrorCode.INVALID_REQUEST);
        }
        if (Objects.requireNonNull(content).isBlank() && Objects.requireNonNull(file).getSize() == 0) {
            throw new ChatException(ErrorCode.INVALID_REQUEST);
        }

        Message message = Message.builder()
                .chat(chat)
                .content(content)
                .user(user)
                .build();
        if (file != null && !file.isEmpty()) {
            message.setFileInfo(this.fileService.upload(file, token).getResult());
        }

        Message saved = messageRepository.save(message);
        chat.getMessages().add(saved);
        chatRepository.save(chat);
        return saved;

    }

    public List<Message> findMessageByChatId(Long chatId, String token) {
        chatService.findChatById(chatId, token);
        return messageRepository.findByChatId(chatId);
    }

    private User getUser(String token) {
        return UserModel.toEntity(Objects.requireNonNull(userService.getUserProfile(token).getBody()));
    }

}