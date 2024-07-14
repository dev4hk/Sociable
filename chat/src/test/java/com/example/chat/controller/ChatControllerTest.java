package com.example.chat.controller;

import com.example.chat.entity.Chat;
import com.example.chat.request.CreateChatRequest;
import com.example.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    private String testToken;
    private Chat testChat;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
        this.testChat = new Chat();
    }

    @Test
    void create_chat() throws Exception {
        CreateChatRequest request = CreateChatRequest.builder()
                .userId(2)
                .build();
        when(chatService.create(request.getUserId(), testToken)).thenReturn(testChat);
        mockMvc.perform(
                        post("/api/v1/chats")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_user_chats() throws Exception {
        when(chatService.findChatsByUser(testToken)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/chats")
                        .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }
}
