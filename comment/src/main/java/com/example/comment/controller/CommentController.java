package com.example.comment.controller;

import com.example.comment.dto.CommentDto;
import com.example.comment.request.CommentRequest;
import com.example.comment.response.Response;
import com.example.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public Response<CommentDto> comment(
            @PathVariable Integer postId,
            @RequestHeader("Authorization") String token,
            @RequestBody CommentRequest request
    ) {
        return Response.success(commentService.create(postId, request.getComment(), token));
    }

    @GetMapping("/post/{postId}")
    public Response<Page<CommentDto>> getComments(
            @PathVariable Integer postId,
            @RequestHeader("Authorization") String token,
            Pageable pageable
    ) {
        return Response.success(commentService.findAllByPostId(postId, pageable, token));
    }
}
