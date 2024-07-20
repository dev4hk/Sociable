package com.example.comment.service;

import com.example.comment.entity.Comment;
import com.example.comment.exception.CommentException;
import com.example.comment.fixture.CommentFixture;
import com.example.comment.fixture.PostFixture;
import com.example.comment.fixture.UserFixture;
import com.example.comment.model.Post;
import com.example.comment.model.User;
import com.example.comment.repository.CommentRepository;
import com.example.comment.response.Response;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private CommentRepository commentRepository;

    private String testToken;

    private User testUser;

    private Post testPost;

    private Comment testComment;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.testToken = "AABB";
        this.testUser = UserFixture.get(1);
        this.testPost = PostFixture.get(1, 1);
        this.testComment = CommentFixture.get(1, 1, "comment");
    }

    @Test
    void create_comment() {
        String comment = "comment";
        Integer postId = 1;
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postService.getPostById(postId, this.testToken)).thenReturn(Response.success(this.testPost));
        when(commentRepository.save(any())).thenReturn(mock(Comment.class));

        assertDoesNotThrow(() -> commentService.create(postId, comment, testToken));
    }

    @Test
    void create_comment_with_non_existing_user() {
        String comment = "comment";
        Integer postId = 1;
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/users/profile", new HashMap<>(), null, new RequestTemplate());
        when(userService.getUserProfile(null)).thenThrow(new FeignException.NotFound(null, request, null, null));
        CommentException exception = Assertions.assertThrows(CommentException.class, () -> commentService.create(postId, comment, null));
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getStatus());
    }

    @Test
    void get_all_comments_by_post() {
        Integer postId = 1;
        Pageable pageable = mock(Pageable.class);
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(commentRepository.findAllByPostId(postId, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> commentService.findAllByPostId(postId, pageable, this.testToken));
    }

    @Test
    void delete_comment() {
        Integer commentId = 1;
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(testUser)));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(this.testComment));
        assertDoesNotThrow(() -> commentService.deleteComment(commentId, testToken));
        verify(commentRepository, times(1)).delete(this.testComment);
    }

    @Test
    void delete_comments_by_postId() {
        Integer postId = 1;
        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(testUser)));
        when(postService.getPostById(postId, this.testToken)).thenReturn(Response.success(this.testPost));
        assertDoesNotThrow(() -> this.commentService.deleteCommentsByPostId(postId, testToken));
        verify(this.commentRepository, times(1)).deleteAllByPostId(postId);
    }
}
