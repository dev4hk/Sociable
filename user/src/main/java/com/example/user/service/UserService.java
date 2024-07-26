package com.example.user.service;

import com.example.user.config.JwtService;
import com.example.user.entity.User;
import com.example.user.enums.ErrorCode;
import com.example.user.enums.NotificationType;
import com.example.user.exception.UserException;
import com.example.user.model.FileInfo;
import com.example.user.repository.UserRepository;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.request.NotificationRequest;
import com.example.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FileService fileService;
    private final NotificationService notificationService;
    private static final String AUTH_PREFIX = "Bearer ";

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.BAD_CREDENTIAL, "Wrong Password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new UserException(ErrorCode.INVALID_REQUEST, "Passwords are not matching");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    public User getOtherUserInfo(Integer id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User Not Found"));
    }
    public List<User> getOtherUsersInfo(String query, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return this.userRepository.findOtherUsers(user.getId(), query);
    }

    public User changeUserInfo(MultipartFile image, ChangeUserInfoRequest request, Principal connectedUser, String token) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDescription(request.getDescription());
        if(image != null && !image.isEmpty()) {
            FileInfo fileInfo = fileService.upload(image, token).getResult();
            user.setFileInfo(fileInfo);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User followUser(Principal connectedUser, Integer userId) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if(!user.getFollowings().contains(userId)) {
            followUser(user, otherUser);
            createNotification(user, otherUser);
        }
        else {
            unfollowUser(user, otherUser);
        }
        userRepository.save(otherUser);
        return userRepository.save(user);
    }

    private void createNotification(User sourceUser, User targetUser) {
        NotificationRequest notificationRequest = generateNotificationRequest(sourceUser, targetUser);
        createAndSendNotification(notificationRequest);
    }

    private void createAndSendNotification(NotificationRequest notificationRequest) {
        try {
            this.notificationService.createAndSendNotification(notificationRequest);
        } catch (Exception e) {
            throw new UserException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private NotificationRequest generateNotificationRequest(User sourceUser, User targetUser) {
        return new NotificationRequest(UserResponse.fromUser(sourceUser), targetUser.getId(), NotificationType.FOLLOW_USER);
    }

    public List<User> getFollowings(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userRepository.getFollowings(user.getFollowings());
    }

    public List<User> getFollowers(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userRepository.getFollowers(user.getFollowers());
    }

    private void followUser(User from, User to) {
        from.getFollowings().add(to.getId());
        to.getFollowers().add(from.getId());
    }

    private void unfollowUser(User from, User to) {
        from.getFollowings().remove(to.getId());
        to.getFollowers().remove(from.getId());
    }

    public User savePost(Integer postId, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if(user.getSavedPosts().contains(postId)) {
            user.getSavedPosts().remove(postId);
        }
        else {
            user.getSavedPosts().add(postId);
        }
        return userRepository.save(user);
    }

    public List<User> getUserSuggestions(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return this.userRepository.findUserSuggestions(user.getId(), user.getFollowings());
    }
}