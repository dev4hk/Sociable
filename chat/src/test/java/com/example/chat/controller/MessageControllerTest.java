package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.fixture.MessageFixture;
import com.example.chat.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    private String testToken;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
    }

    @Test
    void create_message() throws Exception {
        String content = "content";
        Long chatId = 1L;
        Message message = MessageFixture.get("content");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        when(messageService.createMessage(testToken, chatId, content, file))
                .thenReturn(message);
        mockMvc.perform(
                        multipart(HttpMethod.POST,"/api/v1/messages/chat/" + chatId)
                                .file(file)
                                .param("content", content)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void find_messages_by_chatId() throws Exception {
        Long chatId = 1L;
        when(messageService.findMessageByChatId(chatId, testToken)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/messages/chat/" + chatId)
                        .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
