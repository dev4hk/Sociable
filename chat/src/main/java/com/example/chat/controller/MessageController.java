package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.response.MessageResponse;
import com.example.chat.response.Response;
import com.example.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/chat/{chatId}")
    public Response<MessageResponse> createMessage(
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId) {
        return Response.success(MessageResponse.fromMessage(messageService.createMessage(token, chatId, content, file)));
    }

    @GetMapping("/chat/{chatId}")
    public Response<List<MessageResponse>> findMessageByChat(@PathVariable Long chatId, @RequestHeader("Authorization") String token) {
        return Response.success(messageService.findMessageByChatId(chatId, token).stream().map(MessageResponse::fromMessage).toList());
    }

}
