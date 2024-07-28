package com.example.user.service;

import com.example.user.auth.AuthenticationRequest;
import com.example.user.auth.AuthenticationResponse;
import com.example.user.auth.AuthenticationService;
import com.example.user.auth.RegisterRequest;
import com.example.user.config.JwtService;
import com.example.user.entity.Token;
import com.example.user.entity.User;
import com.example.user.enums.ErrorCode;
import com.example.user.exception.UserException;
import com.example.user.fixture.UserFixture;
import com.example.user.repository.TokenRepository;
import com.example.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("email@email.com")
                .password("password")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(UserFixture.get(1));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
        when(tokenRepository.save(any(Token.class))).thenReturn(Token.builder().id(1).build());

        AuthenticationResponse response = assertDoesNotThrow(() -> authenticationService.register(request));
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void register_exsiting_email_throws_error() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("email@email.com")
                .password("password")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(UserFixture.get(1)));

        UserException response = assertThrows(UserException.class, () -> authenticationService.register(request));
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, response.getErrorCode());
    }

    @Test
    void authentication() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(UserFixture.get(1)));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
        when(tokenRepository.findAllValidTokensByUser(anyInt())).thenReturn(List.of());
        when(tokenRepository.save(any(Token.class))).thenReturn(Token.builder().id(1).build());

        AuthenticationResponse response = assertDoesNotThrow(() -> authenticationService.authenticate(request));
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void authentication_with_wrong_password_throws_exception() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(request));
    }

}
