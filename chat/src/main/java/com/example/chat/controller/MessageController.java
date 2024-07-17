package com.example.chat.controller;

import com.example.chat.entity.Message;
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
    public ResponseEntity<Message> createMessage(
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String token,
            @PathVariable Long chatId) throws IOException {
        return new ResponseEntity<>(messageService.createMessage(token, chatId, content, file), HttpStatus.CREATED);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> findMessageByChat(@PathVariable Long chatId, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(messageService.findMessageByChatId(chatId, token), HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam String filePath, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(messageService.getFile(filePath, token), HttpStatus.OK);
    }
}
