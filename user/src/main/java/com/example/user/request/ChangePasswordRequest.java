package com.example.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChangePasswordRequest {

    @NotEmpty(message = "Old Password is mandatory")
    @NotNull(message = "Old Password is mandatory")
    @Size(min = 8, message = "Old Password should be 8 characters long minimum")
    private String oldPassword;

    @NotEmpty(message = "New Password is mandatory")
    @NotNull(message = "New Password is mandatory")
    @Size(min = 8, message = "New Password should be 8 characters long minimum")
    private String newPassword;

    @NotEmpty(message = "Confirm password is mandatory")
    @NotNull(message = "Confirm password is mandatory")
    @Size(min = 8, message = "Confirm password should be 8 characters long minimum")
    private String confirmPassword;
}