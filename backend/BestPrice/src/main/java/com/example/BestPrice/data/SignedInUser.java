package com.example.BestPrice.data;

import org.springframework.context.annotation.Bean;

public class SignedInUser {
    private static final SignedInUser instance = new SignedInUser();

    private String username;

    public SignedInUser() {
    }

    public static SignedInUser getInstance(){
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
