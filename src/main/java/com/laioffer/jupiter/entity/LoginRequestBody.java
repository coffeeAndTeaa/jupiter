package com.laioffer.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestBody {
    private final String userId;
    private final String passWord;

    @JsonCreator
    public LoginRequestBody(@JsonProperty("user_id") String userId, @JsonProperty("password") String passWord) {
        this.userId = userId;
        this.passWord = passWord;
    }
    public String getUserId() {
        return userId;
    }

    public String getPassWord() {
        return passWord;
    }
}
