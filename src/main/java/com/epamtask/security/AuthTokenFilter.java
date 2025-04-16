package com.epamtask.security;

import com.epamtask.security.AuthSessionStore.Credentials;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthSessionStore authSessionStore;

    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();
    private static final ThreadLocal<String> currentPassword = new ThreadLocal<>();

    public AuthTokenFilter(AuthSessionStore authSessionStore) {
        this.authSessionStore = authSessionStore;
    }

    public static String getUsername() {
        String username = currentUsername.get();
        if (username == null) {
            throw new SecurityException("No username in context");
        }
        return username;
    }

    public static String getPassword() {
        String password = currentPassword.get();
        if (password == null) {
            throw new SecurityException("No password in context");
        }
        return password;
    }

    public static void clear() {
        currentUsername.remove();
        currentPassword.remove();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("X-Auth-Token");

        if (token != null && !token.isBlank()) {
            try {
                Credentials credentials = authSessionStore.getCredentials(token.trim());
                currentUsername.set(credentials.getUsername());
                currentPassword.set(credentials.getPassword());
            } catch (SecurityException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            clear();
        }
    }
}