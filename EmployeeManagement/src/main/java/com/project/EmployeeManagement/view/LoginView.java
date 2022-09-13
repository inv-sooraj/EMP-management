package com.project.EmployeeManagement.view;

import com.project.EmployeeManagement.entity.User;
import com.project.EmployeeManagement.json.Json;
import com.project.EmployeeManagement.security.util.TokenGenerator.Token;
import java.util.Date;

public class LoginView extends UserView {

    public static class TokenView {

        private final String value;
        @Json.DateTimeFormat
        private final Date expiry;

        public TokenView(Token token) {
            this.value = token.value;
            this.expiry = new Date(token.expiry);
        }

        public TokenView(String value, long expiry) {
            this.value = value;
            this.expiry = new Date(expiry);
        }

        public String getValue() {
            return value;
        }

        public Date getExpiry() {
            return expiry;
        }
    }

    private final TokenView accessToken;
    private final TokenView refreshToken;

    public LoginView(User user, Token accessToken, Token refreshToken) {
        super(user);
        this.accessToken = new TokenView(accessToken);
        this.refreshToken = new TokenView(refreshToken);
    }

    public LoginView(User user, TokenView accessToken, TokenView refreshToken) {
        super(user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenView getAccessToken() {
        return accessToken;
    }

    public TokenView getRefreshToken() {
        return refreshToken;
    }
}
