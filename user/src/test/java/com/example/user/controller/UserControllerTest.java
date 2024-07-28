package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.enums.Role;
import com.example.user.fixture.ChangeUserInfoRequestFixture;
import com.example.user.fixture.UserFixture;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    @Test
    void change_password() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword", "NewPassword", "NewPassword");
        doNothing().when(userService).changePassword(eq(request), any(User.class));
        mockMvc.perform(
                        patch("/api/v1/users/change/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    void change_password_without_login_throws_error() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword", "NewPassword", "NewPassword");
        doNothing().when(userService).changePassword(eq(request), any(User.class));
        mockMvc.perform(
                        patch("/api/v1/users/change/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void change_user_info() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        String token = "AABB";
        User user = UserFixture.get(1);
        ChangeUserInfoRequest request = ChangeUserInfoRequestFixture.get();
        when(userService.changeUserInfo(any(), any(), any(), any())).thenReturn(user);
        mockMvc.perform(
                        multipart(HttpMethod.PUT, "/api/v1/users/change/info")
                                .file(mockFile)
                                .param("request", objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void change_user_info_without_login_throws_error () throws Exception {
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        String token = "AABB";
        User user = UserFixture.get(1);
        ChangeUserInfoRequest request = ChangeUserInfoRequestFixture.get();
        when(userService.changeUserInfo(any(), any(), any(), any())).thenReturn(user);
        mockMvc.perform(
                        multipart(HttpMethod.PUT, "/api/v1/users/change/info")
                                .file(mockFile)
                                .param("request", objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void get_user_profile() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "AABB";
        mockMvc.perform(
                        get("/api/v1/users/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_user_profile_without_login_throws_error() throws Exception {
        String token = "AABB";
        mockMvc.perform(
                        get("/api/v1/users/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void get_other_user_info() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Integer userId = 1;
        String token = "AABB";
        when(userService.getOtherUserInfo(userId)).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        get("/api/v1/users/1/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_other_user_info_without_login_throws_error() throws Exception {
        Integer userId = 1;
        String token = "AABB";
        when(userService.getOtherUserInfo(userId)).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        get("/api/v1/users/1/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void get_other_users_info() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "AABB";
        String query = "A";
        when(userService.getOtherUsersInfo(eq(query), any(User.class))).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .param("query", query)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_other_users_info_without_login_throws_error() throws Exception {
        String token = "AABB";
        String query = "A";
        when(userService.getOtherUsersInfo(eq(query), any(User.class))).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .param("query", query)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void follow_user() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "AABB";
        Integer userId = 1;
        when(userService.followUser(any(), eq(userId))).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        put("/api/v1/users/follow/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void follow_user_without_login_throws_error() throws Exception {
        String token = "AABB";
        Integer userId = 1;
        when(userService.followUser(any(), eq(userId))).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        put("/api/v1/users/follow/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void save_post() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "AABB";
        Integer postId = 1;
        when(userService.savePost(eq(postId), any())).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        put("/api/v1/users/post/save/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void save_post_without_login_throws_error() throws Exception {
        String token = "AABB";
        Integer postId = 1;
        when(userService.savePost(eq(postId), any())).thenReturn(UserFixture.get(1));
        mockMvc.perform(
                        put("/api/v1/users/post/save/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void get_user_suggestions() throws Exception {
        User principal = new User(1, "firstname", "lastname", "email@email.com", "", null, "description", null, null, null, Role.USER, null, null, null, null);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = "AABB";
        when(userService.getUserSuggestions(any())).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users/suggestions")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void get_user_suggestions_without_login_throws_Error() throws Exception {
        String token = "AABB";
        when(userService.getUserSuggestions(any(User.class))).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users/suggestions")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
