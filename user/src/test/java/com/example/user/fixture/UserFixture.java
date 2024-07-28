package com.example.user.fixture;

import com.example.user.entity.User;

public class UserFixture {
    public static User get() {
        User user = new User();
        user.setId(1);
        return user;
    }
}
