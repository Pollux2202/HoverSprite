package com.example.demo.Security.Utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String cookie = String.format("%s=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=None; Secure=false",
                name, value, maxAge);
        response.addHeader("Set-Cookie", cookie);
    }


}
