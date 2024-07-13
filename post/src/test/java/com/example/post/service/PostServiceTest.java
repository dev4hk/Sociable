package com.example.post.service;

import com.example.post.enums.ErrorCode;
import com.example.post.entity.Post;
import com.example.post.exception.PostException;
import com.example.post.fixture.PostFixture;
import com.example.post.fixture.UserFixture;
import com.example.post.model.User;
import com.example.post.repository.PostRepository;
import com.example.post.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @MockBean
    private CommentService commentService;

    private String testToken;
    private User testUser;

    @BeforeEach
    void setup() {
        this.testToken = "AABB";
        this.testUser = UserFixture.get(1);

    }

    @Test
    void create_post() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        Assertions.assertDoesNotThrow(() -> postService.create(body, file, testToken));
    }

    @Test
    void create_post_with_user_not_found() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        Integer userId = 1;

        when(userService.getUserProfile(testToken)).thenThrow(PostException.class);
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.create(body, file, testToken));
    }

    @Test
    void update_post() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        Integer postId = 1;

        Post post = PostFixture.get(postId, 1);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.modify(body, file, this.testToken, postId));
    }

    @Test
    void update_non_existing_post_returns_error() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, file, this.testToken, postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void update_post_by_another_user_returns_error() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);
        User writer = UserFixture.get(2);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(writer)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, file, this.testToken, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void update_with_no_contents_returns_error() {
        String body = null;
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "".getBytes());
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);
        User writer = UserFixture.get(2);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(writer)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, file, this.testToken, postId));
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void delete_post() {
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentService.deleteAllByPost(postId, this.testToken)).thenReturn(Response.success());

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
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void delete_post_by_another_user_returns_error() {
        Integer userId = 1;
        Integer postId = 1;

        Post post = PostFixture.get(postId, userId);
        User writer = UserFixture.get(2);

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(writer)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.delete(this.testToken, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void get_all_posts() {

        Pageable pageable = mock(Pageable.class);

        when(postRepository.findAll(pageable)).thenReturn(Page.empty());
        when(userService.getUserProfile(anyString())).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));

        Assertions.assertDoesNotThrow(() -> postService.getAllPosts(pageable, testToken));

    }

    @Test
    void get_my_posts() {

        Pageable pageable = mock(Pageable.class);
        Integer userId = 1;

        when(userService.getUserProfile(any())).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findAllByUserId(userId, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.getAllPostsByUserId(userId, pageable, ""));

    }

    @Test
    void like_Post() {
        Integer postId = 1;
        Integer userId = 1;
        Post post = PostFixture.get(1, userId);
        when(userService.getUserProfile(any())).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        assertEquals(1, post.getLikedBy().size());
        assertTrue(post.getLikedBy().contains(1));
    }

}
