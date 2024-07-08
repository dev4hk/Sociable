package com.example.comment.service;

import com.example.comment.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserService {
    @GetMapping("/api/v1/users/profile")
    ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token);
}
