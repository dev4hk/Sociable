package com.example.comment.service;

import com.example.comment.entity.Comment;
import com.example.comment.fixture.PostFixture;
import com.example.comment.fixture.UserFixture;
import com.example.comment.model.Post;
import com.example.comment.model.User;
import com.example.comment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentRepository commentRepository;

    private String testToken;

    private User testUser;

    private Post testPost;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
        this.testUser = UserFixture.get(1);
        this.testPost = PostFixture.get(1);
    }

    @Test
    void create_comment() {
        String comment = "comment";
        Integer postId = 1;
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postService.getPostById(postId)).thenReturn(ResponseEntity.of(Optional.of(this.testPost)));
        when(commentRepository.save(any())).thenReturn(mock(Comment.class));

        assertDoesNotThrow(() -> commentService.create(postId, comment, testToken));
    }

    @Test
    void get_all_comments_by_post() {
        Pageable pageable = mock(Pageable.class);
        Integer postId = 1;

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postService.getPostById(postId)).thenReturn(ResponseEntity.of(Optional.of(this.testPost)));
        when(commentRepository.findAllByPostId(postId, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> commentService.findAllByPostId(postId, pageable));
    }
}
