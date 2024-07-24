package com.example.post.service;

import com.example.post.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserService {

    @GetMapping("/api/v1/users/profile")
    ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token);

    @PutMapping("/api/v1/users/post/save/{postId}")
    ResponseEntity<User> savePost(@PathVariable Integer postId, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/users/{id}/profile")
    ResponseEntity<User> getOtherUserInfo(@PathVariable Integer id, @RequestHeader("Authorization") String token);

}
