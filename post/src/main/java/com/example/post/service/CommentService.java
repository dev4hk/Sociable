package com.example.post.service;

import com.example.post.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "COMMENT-SERVICE", url = "http://localhost:8083")
public interface CommentService {
    @DeleteMapping("/api/v1/comments/post/{postId}")
    Response<Void> deleteAllByPost(@PathVariable Integer postId, @RequestHeader("Authorization") String token);
}
