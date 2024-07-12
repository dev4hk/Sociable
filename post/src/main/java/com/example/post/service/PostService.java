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
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;

    @Transactional
    public PostDto create(String body, MultipartFile file, String token) {
        if ((body == null || body.isBlank()) && (file == null || file.isEmpty())) {
            throw new PostException(ErrorCode.INVALID_REQUEST);
        }
        User user = getUser(token);
        Post post = Post.of(body, user);
        return uploadFile(file, post);
    }

    @Transactional
    public PostDto modify(String body, MultipartFile file, String token, Integer postId) {
        if ((body == null || body.isBlank()) && (file == null || file.isEmpty())) {
            throw new PostException(ErrorCode.INVALID_REQUEST);
        }
        User user = getUser(token);
        Post post = getPost(postId);

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getId(), postId));
        }
        if (body != null && !body.isBlank()) {
            post.setBody(body);
        }
        return uploadFile(file, post);
    }

    @Transactional
    public void delete(String token, Integer postId) {
        User user = getUser(token);
        Post post = getPost(postId);

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getId(), postId));
        }

        postRepository.delete(post);
        commentService.deleteAllByPost(postId, token);
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

    private PostDto uploadFile(MultipartFile file, Post post) {
        if (file == null || file.isEmpty()) {
            return PostDto.fromEntity(postRepository.save(post));
        }
        String filePath = fileStorageService.saveFile(file, post.getUserId());
        post.setFilePath(filePath);
        post.setFileType(file.getContentType());
        return PostDto.fromEntity(postRepository.save(post));
    }

    public PostDto getPostById(Integer postId, String token) {
        User user = getUser(token);
        return PostDto.fromEntity(this.getPost(postId));
    }
}