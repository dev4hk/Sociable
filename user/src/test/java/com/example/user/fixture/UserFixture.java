package com.example.user.fixture;

import com.example.user.entity.User;

public class UserFixture {
    public static User get(Integer userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }
}
