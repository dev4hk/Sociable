package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    private final ChatService chatService;

    private final ChatRepository chatRepository;

    private final FileUtils fileUtils;

    public Message createMessage(String token, Long chatId, String content, MultipartFile file) throws IOException {
        User user = UserModel.toEntity(Objects.requireNonNull(userService.getUserProfile(token).getBody()));
        Chat chat = chatService.findChatById(chatId, token);

        if(content == null && file == null) {
            throw new RuntimeException("Cannot create message because content and file are empty");
        }
        if(Objects.requireNonNull(content).isBlank() && Objects.requireNonNull(file).getSize() == 0) {
            throw new RuntimeException("Cannot create message because content and file are empty");
        }

        Map<String, String> fileMap = null;
        if(file != null && file.getSize() != 0) {
            fileMap = fileUtils.upload(user, chatId, file);
        }

        Message message = Message.builder()
                .chat(chat)
                .content(content)
                .user(user)
                .build();

        if(fileMap != null) {
            message.setFilePath(fileMap.get("filePath"));
            message.setFileName(fileMap.get("fileName"));
            message.setContentType(fileMap.get("contentType"));
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

    public byte[] getFile(String filePath, String token) {
        User user = UserModel.toEntity(Objects.requireNonNull(userService.getUserProfile(token).getBody()));
        return fileUtils.readFileFromLocation(filePath);
    }
}
