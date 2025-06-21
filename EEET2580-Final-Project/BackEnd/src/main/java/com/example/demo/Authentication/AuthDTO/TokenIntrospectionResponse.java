package com.example.demo.Authentication.AuthDTO;

public class TokenIntrospectionResponse {
    private boolean isValid;

    private boolean isExpired;

    public TokenIntrospectionResponse(boolean isValid, boolean isExpired) {
        this.isValid = isValid;
        this.isExpired = isExpired;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
