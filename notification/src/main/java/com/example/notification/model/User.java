package com.example.notification.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class User {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private FileInfo fileInfo;

}
