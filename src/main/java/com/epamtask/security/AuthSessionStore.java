package com.epamtask.security;
import com.epamtask.aspect.annotation.Loggable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthSessionStore {
    private final Map<String, Credentials> tokenToCredentials = new ConcurrentHashMap<>();
    @Loggable
    public String createToken(String username, String password) {
        String token = UUID.randomUUID().toString();
        tokenToCredentials.put(token, new Credentials(username, password));
        return token;
    }
    @Loggable
    public Credentials getCredentials(String token) {
        if (!tokenToCredentials.containsKey(token)) {
            throw new SecurityException("Access denied: invalid or missing token");
        }
        return tokenToCredentials.get(token);
    }

    public static class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
