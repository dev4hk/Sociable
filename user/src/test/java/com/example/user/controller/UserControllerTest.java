package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.enums.Role;
import com.example.user.fixture.ChangeUserInfoRequestFixture;
import com.example.user.fixture.UserFixture;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    @WithMockUser
    @Test
    void change_password() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword", "NewPassword", "NewPassword");
        doNothing().when(userService).changePassword(request, mockPrincipal);
        mockMvc.perform(
                        patch("/api/v1/users/change/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @WithAnonymousUser
    @Test
    void change_password_without_login_throws_error() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        ChangePasswordRequest request = new ChangePasswordRequest("OldPassword", "NewPassword", "NewPassword");
        doNothing().when(userService).changePassword(request, mockPrincipal);
        mockMvc.perform(
                        patch("/api/v1/users/change/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @Test
    void change_user_info() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        String token = "AABB";
        User user = UserFixture.get();
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

    @WithAnonymousUser
    @Test
    void change_user_info_without_login_throws_error () throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        String token = "AABB";
        User user = UserFixture.get();
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
        String token = "AABB";
        mockMvc.perform(
                        get("/api/v1/users/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .with(user(new User(1, "firstname", "lastname", "email", "password", null, null, null, null, null, Role.USER, null, null, null, null)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
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

    @WithMockUser
    @Test
    void get_other_user_info() throws Exception {
        Integer userId = 1;
        String token = "AABB";
        when(userService.getOtherUserInfo(userId)).thenReturn(UserFixture.get());
        mockMvc.perform(
                        get("/api/v1/users/1/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void get_other_user_info_without_login_throws_error() throws Exception {
        Integer userId = 1;
        String token = "AABB";
        when(userService.getOtherUserInfo(userId)).thenReturn(UserFixture.get());
        mockMvc.perform(
                        get("/api/v1/users/1/profile")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @Test
    void get_other_users_info() throws Exception {
        String token = "AABB";
        String query = "A";
        Principal mockPrincipal = mock(Principal.class);
        when(userService.getOtherUsersInfo(query, mockPrincipal)).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .param("query", query)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void get_other_users_info_without_login_throws_error() throws Exception {
        String token = "AABB";
        String query = "A";
        Principal mockPrincipal = mock(Principal.class);
        when(userService.getOtherUsersInfo(query, mockPrincipal)).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .param("query", query)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @Test
    void follow_user() throws Exception {
        String token = "AABB";
        Integer userId = 1;
        when(userService.followUser(any(), eq(userId))).thenReturn(UserFixture.get());
        mockMvc.perform(
                        put("/api/v1/users/follow/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void follow_user_without_login_throws_error() throws Exception {
        String token = "AABB";
        Integer userId = 1;
        when(userService.followUser(any(), eq(userId))).thenReturn(UserFixture.get());
        mockMvc.perform(
                        put("/api/v1/users/follow/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @Test
    void save_post() throws Exception {
        String token = "AABB";
        Integer postId = 1;
        when(userService.savePost(eq(postId), any())).thenReturn(UserFixture.get());
        mockMvc.perform(
                        put("/api/v1/users/post/save/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void save_post_without_login_throws_error() throws Exception {
        String token = "AABB";
        Integer postId = 1;
        when(userService.savePost(eq(postId), any())).thenReturn(UserFixture.get());
        mockMvc.perform(
                        put("/api/v1/users/post/save/1")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser
    @Test
    void get_user_suggestions() throws Exception {
        String token = "AABB";
        when(userService.getUserSuggestions(any())).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users/suggestions")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void get_user_suggestions_without_login_throws_Error() throws Exception {
        String token = "AABB";
        Principal mockPrincipal = mock(Principal.class);
        when(userService.getUserSuggestions(mockPrincipal)).thenReturn(List.of());
        mockMvc.perform(
                        get("/api/v1/users/suggestions")
                                .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
