package com.example.post.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Test
    void create_post() {
        String body = "body";
        String email = "email@email.com";

        when(userService.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        Assertions.assertDoesNotThrow(() -> postService.create(body, email));
    }

    @Test
    void create_post_with_user_not_found() {
        String body = "body";
        String email = "email@email.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.create(body, email));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void update_post() {
        String body = "body";
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User user = post.getUser();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.saveAndFlush(any())).thenReturn(post);

        Assertions.assertDoesNotThrow(() -> postService.modify(body, email, postId));
    }

    @Test
    void update_non_existing_post_returns_error() {
        String body = "body";
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User user = post.getUser();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, email, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void update_post_by_another_user_returns_error() {
        String body = "body";
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User writer = UserFixture.get("email1", "password1", 2);

        when(userService.findByEmail(email)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.modify(body, email, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void delete_post() {
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User user = post.getUser();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Assertions.assertDoesNotThrow(() -> postService.delete(email, postId));
    }

    @Test
    void delete_non_existing_post_returns_error() {
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User user = post.getUser();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.delete(email, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void delete_post_by_another_user_returns_error() {
        String email = "email@email.com";
        Integer postId = 1;

        Post post = PostFixture.get(email, postId, 1);
        User writer = UserFixture.get("email1", "password1", 2);

        when(userService.findByEmail(email)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostException exception = Assertions.assertThrows(PostException.class, () -> postService.delete(email, postId));
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
        User user = mock(User.class);

        when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> postService.getMyPosts(pageable, ""));

    }

}
