package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.enums.ErrorCode;
import com.example.chat.exception.ChatException;
import com.example.chat.model.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    @Transactional
    public Chat create(Integer userId2, String token) {
        User user1 = getUser(token);
        User user2 = getOtherUser(userId2, token);
        Optional<Chat> optChat = chatRepository.findChatByUsers(user1, user2);

        if (optChat.isPresent()) {
            return optChat.get();
        }

        Chat chat = new Chat();
        chat.getUsers().add(user1);
        chat.getUsers().add(user2);

        return chatRepository.save(chat);
    }

    public Chat findChatById(Long chatId, String token) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException(ErrorCode.CHAT_NOT_FOUND));
    }

    public List<Chat> findChatsByUser(String token) {
        User user = getUser(token);
        return chatRepository.findByUsersId(user.getId());
    }

    private User getUser(String token) {
        try {
            return UserModel.toEntity(Objects.requireNonNull(userService.getUserProfile(token).getBody()));
        } catch (Exception e) {
            if (e instanceof FeignException && ((FeignException) e).status() == 404) {
                throw new ChatException(ErrorCode.USER_NOT_FOUND);
            } else {
                throw new ChatException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private User getOtherUser(Integer userId, String token) {
        try {
            return UserModel.toEntity(Objects.requireNonNull(userService.getOtherUserInfo(userId, token).getBody()));
        } catch (Exception e) {
            if (((FeignException) e).status() == 404) {
                throw new ChatException(ErrorCode.USER_NOT_FOUND);
            } else {
                throw new ChatException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

        }
    }
}
