package com.example.post.controller;

import com.example.post.dto.PostDto;
import com.example.post.enums.ErrorCode;
import com.example.post.exception.PostException;
import com.example.post.fixture.PostFixture;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        when(postService.create(eq(body), eq(file), anyString()))
                .thenReturn(PostDto.fromEntity(PostFixture.get(1, 1)));
        mockMvc.perform(
                        multipart(HttpMethod.POST,"/api/v1/posts")
                                .file(file)
                                .param("body", body)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_post() throws Exception {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(postService.modify(eq(body), eq(file), any(), any()))
                .thenReturn(PostDto.fromEntity(PostFixture.get(1, 1)));

        mockMvc.perform(
                multipart(HttpMethod.PUT, "/api/v1/posts/1")
                        .file(file)
                        .param("body", body)
                        .header(HttpHeaders.AUTHORIZATION, this.testToken)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_non_existing_post_returns_error() throws Exception {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        doThrow(new PostException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(body), eq(file), any(), eq(1));

        mockMvc.perform(
                        multipart(HttpMethod.PUT, "/api/v1/posts/1")
                                .file(file)
                                .param("body", body)
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
    void get_user_posts() throws Exception {
        when(postService.getAllPostsByUserId(any(), any(), any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts/user/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void like_post() throws Exception {
        Integer postId = 1;
        when(postService.likeUnlikePost(postId, this.testToken)).thenReturn(PostDto.fromEntity(PostFixture.get(1, 1)));
        mockMvc.perform(
                        get("/api/v1/posts/1/like")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
