package com.subaru.maneki.model;

import java.security.SecureRandom;

/**
 * @author zhangchaojie
 * @since 2016-08-15
 */
public class LoginTicket {

    private static final SecureRandom random         = new SecureRandom();

    private static final String       POSSIBLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String                    token;

    public LoginTicket() {
        token = generate(10);
    }

    private static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
        }
        return sb.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
