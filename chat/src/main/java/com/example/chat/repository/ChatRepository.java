package com.example.chat.repository;

import com.example.chat.entity.Chat;
import com.example.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByUsersId(Integer userId);

    @Query("SELECT c from Chat c WHERE :user1 MEMBER of c.users AND :user2 MEMBER of c.users")
    Optional<Chat> findChatByUsers(@Param("user1") User user1, @Param("user2") User user2);
}
