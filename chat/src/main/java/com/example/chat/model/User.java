package com.example.chat.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class User {

    private Integer id;

    private String firstname;

    private String lastname;

    private String email;
}
