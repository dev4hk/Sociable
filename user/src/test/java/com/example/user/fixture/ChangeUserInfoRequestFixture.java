package com.example.user.fixture;

import com.example.user.request.ChangeUserInfoRequest;

public class ChangeUserInfoRequestFixture {
    public static ChangeUserInfoRequest get() {
        return new ChangeUserInfoRequest(
                "firstname",
                "lastname",
                "description"
        );
    }
}
