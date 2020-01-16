package com.vitalykalenik.topsecret.data;

/**
 * @author Vitaly Kalenik
 */
public class TokenStore {
    private static final TokenStore ourInstance = new TokenStore();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token = "";

    public static TokenStore getInstance() {
        return ourInstance;
    }

    private TokenStore() {
    }
}
