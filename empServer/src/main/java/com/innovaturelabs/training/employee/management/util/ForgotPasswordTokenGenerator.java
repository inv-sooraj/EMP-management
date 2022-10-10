package com.innovaturelabs.training.employee.management.util;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.Assert;

import com.innovaturelabs.training.employee.management.security.util.InvalidTokenException;
import com.innovaturelabs.training.employee.management.security.util.TokenExpiredException;

public class ForgotPasswordTokenGenerator {

    public static final class PasswordToken {

        public final String data;

        public final long time;

        public final long expiry;

        private PasswordToken(String data, long time, long expiry) {
            this.data = data;
            this.time = time;
            this.expiry = expiry;
        }
    }

    public static final class PasswordStatus {

        public final String data;

        public final long time;
        public final long expiry;

        private PasswordStatus(String data, long time, long expiry) {
            this.data = data;
            this.time = time;
            this.expiry = expiry;
        }
    }

    private final SecureRandom random = new SecureRandom();

    private final TextEncryptor textEncryptor;

    private static final String SEPARATOR = "#";

    private final Duration expiry = Duration.ofMinutes(10);

    public ForgotPasswordTokenGenerator(String password, String salt) {

        textEncryptor = Encryptors.text(password, salt);

    }

    public PasswordToken create(String data) {

        Assert.notNull(data, "data cannot be null");

        long currentTime = System.currentTimeMillis();
        long exp = this.expiry.toMillis();
        int rand = random.nextInt();

        String token = data + SEPARATOR + currentTime + SEPARATOR + exp + SEPARATOR + rand;

        return new PasswordToken(textEncryptor.encrypt(token), currentTime, currentTime + exp);
    }

    public PasswordStatus verify(String token) throws InvalidTokenException, TokenExpiredException {

        String value;

        try {
            value = textEncryptor.decrypt(token);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Token cannot be decoded", e);
        }

        String[] parts = value.split(SEPARATOR);
        if (parts.length != 4) {
            throw new InvalidTokenException("Token content is invalid");
        }

        long keyTime;
        long exp;
        try {
            keyTime = Long.parseLong(parts[1]);
            exp = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("Token content is invalid", e);
        }

        if (exp > 0) {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - keyTime;

            if (timeDiff >= exp) {
                throw new TokenExpiredException("Token is expired");
            }
        }

        return new PasswordStatus(parts[0], keyTime, keyTime + exp);

    }

}
