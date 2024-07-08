package com.example.post.service;

import com.example.post.enums.ErrorCode;
import com.example.post.dto.PostDto;
import com.example.post.entity.Post;
import com.example.post.exception.PostException;
import com.example.post.model.User;
import com.example.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public PostDto create(String body, String token) {
        User user = getUser(token);
        Post saved = postRepository.save(Post.of(body, user));
        return PostDto.fromEntity(saved);
    }

    @Transactional
    public PostDto modify(String body, String token, Integer postId) {
        User user = getUser(token);
        Post post = getPost(postId);

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getEmail(), postId));
        }

        post.setBody(body);

        return PostDto.fromEntity(postRepository.saveAndFlush(post));

    }

    @Transactional
    public void delete(String token, Integer postId) {
        User user = getUser(token);
        Post post = getPost(postId);

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getEmail(), postId));
        }

        postRepository.delete(post);
    }

    public Page<PostDto> getAllPosts(Pageable pageable, String token) {
        User user = getUser(token);
        return postRepository.findAll(pageable).map(PostDto::fromEntity);
    }

    public Page<PostDto> getMyPosts(Pageable pageable, String token) {
        User user = getUser(token);
        return postRepository.findAllByUserId(user.getId(), pageable).map(PostDto::fromEntity);
    }

    private User getUser(String token) {
        return userService.getUserProfile(token).getBody();
    }

    private Post getPost(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
    }

}