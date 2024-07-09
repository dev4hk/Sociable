package com.example.user.controller;

import com.example.user.request.ChangePasswordRequest;
import com.example.user.response.UserResponse;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>  getUserProfile(@RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                UserResponse.fromUser(
                        this.userService.getUserProfile(token)
                ));
    }

}

