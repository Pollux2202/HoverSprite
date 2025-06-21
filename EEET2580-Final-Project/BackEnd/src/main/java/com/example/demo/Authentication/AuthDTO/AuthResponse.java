package com.example.demo.Authentication.AuthDTO;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    private boolean isAuth;

    private String token;

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
