package com.example.chat.controller;

import com.example.chat.entity.Chat;
import com.example.chat.request.CreateChatRequest;
import com.example.chat.response.Response;
import com.example.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public Response<Chat> createChat(@RequestBody CreateChatRequest request, @RequestHeader("Authorization") String token) {
        Chat chat = chatService.create(request.getUserId(), token);
        return Response.success(chat);
    }

    @GetMapping
    public Response<List<Chat>> findChatsByUser(@RequestHeader("Authorization") String token) {
        return Response.success(chatService.findChatsByUser(token));
    }

}
