package com.example.comment.service;

import com.example.comment.dto.CommentDto;
import com.example.comment.entity.Comment;
import com.example.comment.exception.CommentException;
import com.example.comment.exception.ErrorCode;
import com.example.comment.model.Post;
import com.example.comment.model.User;
import com.example.comment.repository.CommentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentDto create(Integer postId, String comment, String token) {
        User user = getUser(token);
        Post post = this.postService.getPostById(postId, token).getResult();
        Comment toCreate = Comment.of(user, post.getId(), comment);
        return CommentDto.fromEntity(commentRepository.save(toCreate));

    }

    public Page<CommentDto> findAllByPostId(Integer postId, Pageable pageable, String token) {
        User user = getUser(token);
        return commentRepository.findAllByPostId(postId, pageable).map(CommentDto::fromEntity);
    }

    public void deleteComment(Integer commentId, String token) {
        User user = getUser(token);
        Comment comment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND, String.format("%s not found", commentId)));
        if(!Objects.equals(comment.getUserId(), user.getId())) {
            throw new CommentException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getEmail(), commentId));
        }
        this.commentRepository.delete(comment);
    }

    @Transactional
    public void deleteCommentsByPostId(Integer postId, String token) {
        User user = getUser(token);
        Post post = this.postService.getPostById(postId, token).getResult();
        if(!Objects.equals(user.getId(), post.getUserId())) {
            throw new CommentException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", user.getEmail(), postId));
        }
        this.commentRepository.deleteAllByPostId(postId);
    }

    private User getUser(String token) {
        try {
            return userService.getUserProfile(token).getBody();
        } catch (Exception e) {
            if (e instanceof FeignException && ((FeignException) e).status() == 404) {
                throw new CommentException(ErrorCode.USER_NOT_FOUND);
            } else {
                throw new CommentException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
