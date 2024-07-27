package com.example.notification.controller;

import com.example.notification.enums.NotificationType;
import com.example.notification.fixture.FileFixture;
import com.example.notification.fixture.NotificationFixture;
import com.example.notification.fixture.UserFixture;
import com.example.notification.model.User;
import com.example.notification.request.NotificationRequest;
import com.example.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.apache.http.client.methods.RequestBuilder.post;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String token = "AABB";
    User user1 = UserFixture.getUser(1, "firstname1", "lastname1", "email1@email.com");
    User user2 = UserFixture.getUser(2, "firstname2", "lastname2", "email2@email.com");
    NotificationType type = NotificationType.NEW_COMMENT_ON_POST;
    NotificationRequest request;

    @BeforeEach
    void setup() {
        request = new NotificationRequest(user1, user2.getId(), type, 1, FileFixture.get());
    }

    @Test
    void create_notification() throws Exception {
        when(notificationService.createAndSendNotification(any(NotificationRequest.class))).thenReturn(NotificationFixture.get());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void get_notifications() throws Exception {
        when(notificationService.getNotificationsByUser(any(), anyString())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/notifications")
                                .header(HttpHeaders.AUTHORIZATION, this.token)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}