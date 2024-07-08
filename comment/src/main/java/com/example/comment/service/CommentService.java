package com.example.comment.service;

import com.example.comment.dto.CommentDto;
import com.example.comment.entity.Comment;
import com.example.comment.model.Post;
import com.example.comment.model.User;
import com.example.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentDto create(Integer postId, String comment, String token) {
        User user = this.userService.getUserProfile(token).getBody();
        Post post = this.postService.getPostById(postId, token).getResult();
        Comment toCreate = Comment.of(user.getId(), post.getId(), comment);
        return CommentDto.fromEntity(commentRepository.save(toCreate));

    }

    public Page<CommentDto> findAllByPostId(Integer postId, Pageable pageable, String token) {
        User user = this.userService.getUserProfile(token).getBody();
        return commentRepository.findAllByPostId(postId, pageable).map(CommentDto::fromEntity);
    }
}
