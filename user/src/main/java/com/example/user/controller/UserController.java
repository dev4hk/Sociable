package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.response.UserResponse;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>  getUserProfile(Principal connectedUser) {
        return ResponseEntity.ok(
                UserResponse.fromUser(
                        (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()
                ));
    }
}

