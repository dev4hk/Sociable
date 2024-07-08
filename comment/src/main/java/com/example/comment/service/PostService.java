package com.example.comment.service;

import com.example.comment.model.Post;
import com.example.comment.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "POST-SERVICE", url = "http://localhost:8082")
public interface PostService {
    @GetMapping("/api/v1/posts/{postId}")
    Response<Post> getPostById(@PathVariable Integer postId, @RequestHeader("Authorization") String token);
}
