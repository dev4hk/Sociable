package com.example.user.controller;

import com.example.user.auth.AuthenticationRequest;
import com.example.user.auth.AuthenticationResponse;
import com.example.user.auth.AuthenticationService;
import com.example.user.auth.RegisterRequest;
import com.example.user.enums.ErrorCode;
import com.example.user.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void signup() throws Exception {
        String firstname = "firstname";
        String lastname = "lastname";
        String email = "email@email.com";
        String password = "password";

        RegisterRequest request = new RegisterRequest(firstname, lastname, email, password);

        when(authenticationService.register(request)).thenReturn(any(AuthenticationResponse.class));

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signup_with_existing_email_throws_error() throws Exception {
        String firstname = "firstname";
        String lastname = "lastname";
        String email = "email@email.com";
        String password = "password";

        RegisterRequest request = new RegisterRequest(firstname, lastname, email, password);

        when(authenticationService.register(request)).thenThrow(new UserException(ErrorCode.USER_ALREADY_EXISTS));

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void login() throws Exception {
        String email = "email@email.com";
        String password = "password";

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationService.authenticate(request)).thenReturn(any(AuthenticationResponse.class));
        mockMvc.perform(
                        post("/api/v1/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void login_with_non_existing_credential_throws_error() throws Exception {
        String email = "email@email.com";
        String password = "password";

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationService.authenticate(request)).thenThrow(new UserException(ErrorCode.USER_NOT_FOUND));
        mockMvc.perform(
                        post("/api/v1/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void login_with_wrong_password_throws_error() throws Exception {
        String email = "email@email.com";
        String password = "password";

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationService.authenticate(request)).thenThrow(new UserException(ErrorCode.BAD_CREDENTIAL));
        mockMvc.perform(
                        post("/api/v1/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
