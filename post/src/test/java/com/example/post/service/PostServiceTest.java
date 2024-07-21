package com.example.post.service;

import com.example.post.enums.ErrorCode;
import com.example.post.entity.Post;
import com.example.post.exception.PostException;
import com.example.post.fixture.FileFixture;
import com.example.post.fixture.PostFixture;
import com.example.post.fixture.UserFixture;
import com.example.post.model.User;
import com.example.post.repository.PostRepository;
import com.example.post.response.Response;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    @Mock
    private FileService fileService;

    private String testToken;
    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.testToken = "AABB";
        this.testUser = UserFixture.get(1);

    }

    @Test
    void create_post() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userService.getUserProfile(testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));
        when(fileService.upload(file, testToken)).thenReturn(Response.success(FileFixture.get()));

        Assertions.assertDoesNotThrow(() -> postService.create(body, file, testToken));
    }

    @Test
    void create_post_with_user_not_found() {
        String body = "body";
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        Integer userId = 1;

        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/users/profile", new HashMap<>(), null, new RequestTemplate());
        when(userService.getUserProfile(testToken)).thenThrow(new FeignException.NotFound(null, request, null, null));
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
        when(fileService.upload(file, testToken)).thenReturn(Response.success(FileFixture.get()));

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
        postService.likeUnlikePost(postId, this.testToken);
        assertEquals(1, post.getLikedBy().size());
        assertTrue(post.getLikedBy().contains(1));
    }

    @Test
    void get_saved_post() {
        Pageable pageable = mock(Pageable.class);
        when(userService.getUserProfile(this.testToken)).thenReturn(ResponseEntity.of(Optional.of(this.testUser)));
        when(postRepository.findAllSavedPosts(anySet(), any())).thenReturn(Page.empty());
        assertDoesNotThrow(() -> postService.getSavedPosts(pageable, testToken));
    }

    @Test
    void save_post() {
        Integer postId = 1;
        Integer userId = 1;
        Post post = PostFixture.get(1, userId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.saveUnsavePost(postId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        postService.saveUnsavePost(postId);
        verify(userService, times(1)).saveUnsavePost(postId);
    }

    @Test
    void save_non_existing_post_throws_error() {
        Integer postId = 1;
        when(postRepository.findById(postId)).thenThrow(new PostException(ErrorCode.POST_NOT_FOUND));
        PostException exception = assertThrows(PostException.class, () -> postService.saveUnsavePost(postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void save_post_with_error_from_user_service_throws_error () {
        Integer postId = 1;
        Integer userId = 1;
        Post post = PostFixture.get(1, userId);
        Request request = Request.create(Request.HttpMethod.GET, "/post/save/1", new HashMap<>(), null, new RequestTemplate());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.saveUnsavePost(postId)).thenThrow(new FeignException.NotFound(null, request, null, null));
        PostException exception = assertThrows(PostException.class, () -> postService.saveUnsavePost(postId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

}
