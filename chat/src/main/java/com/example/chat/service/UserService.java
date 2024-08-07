package com.example.chat.service;

import com.example.chat.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserService {

    @GetMapping("/api/v1/users/profile")
    ResponseEntity<UserModel> getUserProfile(@RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/users/{id}/profile")
    ResponseEntity<UserModel> getOtherUserInfo(@PathVariable Integer id, @RequestHeader("Authorization") String token);
}
