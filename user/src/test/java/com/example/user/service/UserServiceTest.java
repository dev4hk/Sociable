package com.example.user.service;

import com.example.user.config.JwtService;
import com.example.user.entity.User;
import com.example.user.enums.ErrorCode;
import com.example.user.exception.UserException;
import com.example.user.fixture.UserFixture;
import com.example.user.model.FileInfo;
import com.example.user.model.Notification;
import com.example.user.repository.UserRepository;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.request.NotificationRequest;
import com.example.user.response.Response;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private FileService fileService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void change_password() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("old")
                .newPassword("new")
                .confirmPassword("new")
                .build();
        User user = UserFixture.get(1);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(UserFixture.get(1));
        assertDoesNotThrow(() -> userService.changePassword(request, user));
    }

    @Test
    void change_password_with_wrong_old_password_throws_error() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("old")
                .newPassword("new")
                .confirmPassword("new")
                .build();
        User user = UserFixture.get(1);
        user.setPassword("old2");
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        UserException exception = assertThrows(UserException.class, () -> userService.changePassword(request, user));
        assertEquals(ErrorCode.BAD_CREDENTIAL, exception.getErrorCode());
    }

    @Test
    void change_password_with_wrong_confirm_password_throws_error() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("old")
                .newPassword("new")
                .confirmPassword("new2")
                .build();
        User user = UserFixture.get(1);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        UserException exception = assertThrows(UserException.class, () -> userService.changePassword(request, user));
        assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
    }

    @Test
    void get_other_user_info() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(UserFixture.get(1)));
        assertDoesNotThrow(() -> userService.getOtherUserInfo(userId));
    }

    @Test
    void get_non_existing_user_info() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserException exception = assertThrows(UserException.class, () -> userService.getOtherUserInfo(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void get_other_users_info() {
        String query = "query";
        User user = UserFixture.get(1);
        when(userRepository.findOtherUsers(user.getId(), query)).thenReturn(List.of());
        assertDoesNotThrow(() -> userService.getOtherUsersInfo(query, user));
    }

    @Test
    void change_user_info() {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        ChangeUserInfoRequest request = ChangeUserInfoRequest.builder()
                .firstname("firstname")
                .lastname("lastname")
                .description("description")
                .build();
        User user = UserFixture.get(1);
        String token = "AABB";
        FileInfo fileInfo = FileInfo.builder()
                .filePath("path")
                .fileType("type")
                .build();

        when(fileService.upload(file, token)).thenReturn(Response.success(fileInfo));
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.changeUserInfo(file, request, user, token));
    }

    @Test
    void change_user_info_file_service_throws_error() {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
        ChangeUserInfoRequest request = ChangeUserInfoRequest.builder()
                .firstname("firstname")
                .lastname("lastname")
                .description("description")
                .build();
        User user = UserFixture.get(1);
        String token = "AABB";
        FileInfo fileInfo = FileInfo.builder()
                .filePath("path")
                .fileType("type")
                .build();
        Request fileRequest = Request.create(Request.HttpMethod.POST, "/api/v1/files", new HashMap<>(), null, new RequestTemplate());
        when(fileService.upload(file, token)).thenThrow(new FeignException.InternalServerError("message", fileRequest, null, null));
        FeignException exception = assertThrows(FeignException.class, () -> userService.changeUserInfo(file, request, user, token));
        assertEquals(500, exception.status());
    }

    @Test
    void follow_user() {
        User user = UserFixture.get(1);
        User otherUser = UserFixture.get(2);
        Integer userId = 2;

        when(userRepository.findById(userId)).thenReturn(Optional.of(otherUser));
        when(notificationService.createAndSendNotification(any(NotificationRequest.class))).thenReturn(Response.success(Notification.builder().build()));
        when(userRepository.save(otherUser)).thenReturn(otherUser);
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.followUser(user, userId));
        assertTrue(user.getFollowings().contains(userId));
        assertTrue(otherUser.getFollowers().contains(user.getId()));
    }

    @Test
    void follow_non_existing_user_throws_exception() {
        User user = UserFixture.get(1);
        User otherUser = UserFixture.get(2);
        Integer userId = 2;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserException exception = assertThrows(UserException.class, () -> userService.followUser(user, userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void save_post() {
        Integer postId = 1;
        User user = UserFixture.get(1);
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.savePost(postId, user));
        assertTrue(user.getSavedPosts().contains(postId));
    }

    @Test
    void get_user_suggestions() {
        User user = UserFixture.get(1);
        when(userRepository.findUserSuggestions(user.getId(), user.getFollowings())).thenReturn(List.of());
        List<User> users = assertDoesNotThrow(() -> userService.getUserSuggestions(user));
        assertEquals(0, users.size());
    }


}
