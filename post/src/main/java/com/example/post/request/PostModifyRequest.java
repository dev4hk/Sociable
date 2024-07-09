package com.example.post.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequest {

    @NotEmpty(message = "Post content is mandatory")
    @NotNull(message = "Post content is mandatory")
    private String body;
}
