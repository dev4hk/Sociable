package com.example.post.service;

import com.example.post.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserService {

    @GetMapping("/api/v1/users/profile")
    ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token);

    @PatchMapping("/post/saveUnsave/{postId}")
    public ResponseEntity<Void> saveUnsavePost(@PathVariable Integer postId);
}
