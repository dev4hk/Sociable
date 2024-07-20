package com.example.post.service;

import com.example.post.dto.PostDto;
import com.example.post.entity.Post;
import com.example.post.enums.ErrorCode;
import com.example.post.exception.PostException;
import com.example.post.model.FileInfo;
import com.example.post.model.User;
import com.example.post.repository.PostRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileService fileService;
    private final CommentService commentService;


    @Transactional
    public PostDto create(String body, MultipartFile file, String token) {
        if ((body == null || body.isBlank()) && (file == null || file.isEmpty())) {
            throw new PostException(ErrorCode.INVALID_REQUEST);
        }
        User user = getUser(token);
        Post post = Post.of(body, user);
        if (file != null && !file.isEmpty()) {
            post.setFileInfo(this.fileService.upload(file, token).getResult());
        }
        return PostDto.fromEntity(this.postRepository.save(post));
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
        FileInfo fileInfo = this.fileService.upload(file, token).getResult();
        post.setFileInfo(fileInfo);
        return PostDto.fromEntity(this.postRepository.save(post));
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

    public Page<PostDto> getAllPostsByUserId(Integer id, Pageable pageable, String token) {
        User user = getUser(token);
        return postRepository.findAllByUserId(id, pageable).map(PostDto::fromEntity);
    }

    private User getUser(String token) {
        try {
            return userService.getUserProfile(token).getBody();
        } catch (Exception e) {
            if (e instanceof FeignException && ((FeignException) e).status() == 404) {
                throw new PostException(ErrorCode.USER_NOT_FOUND);
            } else {
                throw new PostException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private Post getPost(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
    }

    public PostDto getPostById(Integer postId, String token) {
        User user = getUser(token);
        return PostDto.fromEntity(this.getPost(postId));
    }

    public PostDto likeUnlikePost(Integer postId, String token) {
        User user = getUser(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
        if (post.getLikedBy().contains(user.getId())) {
            post.getLikedBy().remove(user.getId());
        } else {
            post.getLikedBy().add(user.getId());
        }
        return PostDto.fromEntity(postRepository.save(post));
    }

    public void saveUnsavePost(Integer postId) {
        Post post = getPost(postId);
        try {
            userService.saveUnsavePost(postId);
        } catch (Exception e) {
            throw new PostException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}