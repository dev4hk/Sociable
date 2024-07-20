package com.example.comment.controller;

import com.example.comment.dto.CommentDto;
import com.example.comment.request.CommentRequest;
import com.example.comment.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private String testToken;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
    }

    @Test
    void create_comment() throws Exception {
        CommentDto commentDto = new CommentDto();
        when(this.commentService.create(1, "comment", this.testToken))
                        .thenReturn(commentDto);
        mockMvc.perform(
                        post("/api/v1/comments/post/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new CommentRequest("comment")))
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_comments_by_postId() throws Exception {
        Integer postId = 1;

        Pageable pageable = mock(Pageable.class);
        when(this.commentService.findAllByPostId(postId, pageable, this.testToken))
                .thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/comments/post/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_comment() throws Exception {
        Integer commentId = 1;

        mockMvc.perform(
                        delete("/api/v1/comments/1")
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)

                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(this.commentService, times(1)).deleteComment(commentId, this.testToken);
    }

    @Test
    void delete_comments_by_postId() throws Exception {
        Integer postId = 1;

        mockMvc.perform(
                        delete("/api/v1/comments/post/1")
                                .header(HttpHeaders.AUTHORIZATION, this.testToken)

                )
                .andDo(print())
                .andExpect(status().isOk());
        verify(this.commentService, times(1)).deleteCommentsByPostId(postId, this.testToken);
    }
}
