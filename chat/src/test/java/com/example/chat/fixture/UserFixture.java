package com.example.chat.fixture;

import com.example.chat.model.User;

public class UserFixture {
    public static User get(Integer userId) {
        User result = new User();
        result.setId(userId);

        return result;
    }
}