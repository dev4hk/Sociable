package com.example.post.controller;

import com.example.post.dto.PostDto;
import com.example.post.enums.ErrorCode;
import com.example.post.exception.PostException;
import com.example.post.fixture.PostFixture;
import com.example.post.request.PostCreateRequest;
import com.example.post.request.PostModifyRequest;
import com.example.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private String testToken;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
    }

    @Test
    void create_post() throws Exception {
        String body = "body";
        when(postService.create(eq(body), anyString()))
                .thenReturn(PostDto.fromEntity(PostFixture.get(1, 1)));
        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(body)))
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_post() throws Exception {
        String body = "body";

        when(postService.modify(eq(body), any(), any()))
                .thenReturn(PostDto.fromEntity(PostFixture.get(1, 1)));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(body)))
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_non_existing_post_returns_error() throws Exception {
        String body = "body";

        doThrow(new PostException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(body), any(), eq(1));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(body)))
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_post() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_non_existing_post_returns_error() throws Exception {
        doThrow(new PostException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void get_posts() throws Exception {
        when(postService.getAllPosts(any(), anyString())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_my_posts() throws Exception {
        when(postService.getMyPosts(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts/my")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}