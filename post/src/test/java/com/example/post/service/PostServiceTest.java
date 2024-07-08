package com.example.post.service;

import com.example.post.enums.ErrorCode;
import com.example.post.entity.Post;
import com.example.post.exception.PostException;
import com.example.post.fixture.PostFixture;
import com.example.post.fixture.UserFixture;
import com.example.post.model.User;
import com.example.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserService userService;

    private String testToken;
    private User testUser;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
        this.testUser = UserFixture.get("email@email.com", 1);

    }

    @Test
    void create_post() {
        String body = "body";

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        Assertions.assertDoesNotThrow(() -> postService.create(body, testToken));
    }

    @Test
    void create_post_with_user_not_found() {
        String body = "body";
        Integer userId = 1;

        when(userService.getUserProfile(testToken)).thenThrow(PostException.class);
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.create(body, testToken));
    }

    @Test
    void update_post() {
        String body = "body";
        Integer postId = 1;

        Post post = PostFixture.get(postId, 1);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.saveAndFlush(any())).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.modify(body, this.testToken, postId));
    }

    @Test
    void update_non_existing_post_returns_error() {
        String body = "body";
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, this.testToken, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void update_post_by_another_user_returns_error() {
        String body = "body";
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);
        User writer = UserFixture.get("email1", 2);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(writer)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, this.testToken, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void delete_post() {
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Assertions.assertDoesNotThrow(() -> postService.delete(this.testToken, postId));
    }

    @Test
    void delete_non_existing_post_returns_error() {
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.delete(this.testToken, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void delete_post_by_another_user_returns_error() {
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);
        User writer = UserFixture.get("email1", 2);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(writer)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.delete(this.testToken, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void get_all_posts() {

        Pageable pageable = mock(Pageable.class);

        when(postRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.getAllPosts(pageable));

    }

    @Test
    void get_my_posts() {

        Pageable pageable = mock(Pageable.class);
        Integer userId = 1;

        when(userService.getUserProfile(any())).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findAllByUserId(userId, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.getMyPosts(pageable, ""));

    }

}
