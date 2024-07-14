package com.example.chat.model;

import com.example.chat.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;

    public static User toEntity(UserModel model) {
        User user = new User();
        user.setId(model.getId());
        user.setFirstname(model.getFirstname());
        user.setLastname(model.getLastname());
        user.setEmail(model.getEmail());
        return user;
    }
}