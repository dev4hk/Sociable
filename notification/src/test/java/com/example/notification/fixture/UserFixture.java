package com.example.notification.fixture;

import com.example.notification.model.User;

public class UserFixture {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;

    public static User getUser(Integer id, String firstname, String lastname, String email) {
        User user = new User();
        user.setId(id);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        return user;
    }
}
