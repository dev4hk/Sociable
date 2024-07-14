package com.example.chat.service;

import com.example.chat.entity.Chat;
import com.example.chat.entity.User;
import com.example.chat.model.UserModel;
import com.example.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    @Transactional
    public Chat create(Integer userId2, String token) {
        UserModel userModel1 = userService.getUserProfile(token).getBody();
        UserModel userModel2 = userService.getOtherUserInfo(userId2, token).getBody();
        User user1 = UserModel.toEntity(userModel1);
        User user2 = UserModel.toEntity(userModel2);
        Optional<Chat> optChat = chatRepository.findChatByUsers(user1, user2);

        if(optChat.isPresent()) {
            return optChat.get();
        }

        Chat chat = new Chat();
        chat.getUsers().add(user1);
        chat.getUsers().add(user2);

        return chatRepository.save(chat);
    }

    public Chat findChatById(Long chatId, String token) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException());
    }

    public List<Chat> findChatsByUser(String token) {
        UserModel userModel = userService.getUserProfile(token).getBody();
        return chatRepository.findByUsersId(userModel.getId());
    }
}
