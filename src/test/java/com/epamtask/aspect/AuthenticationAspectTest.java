package com.epamtask.aspect;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.security.AuthContextHolder;
import com.epamtask.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthenticationAspectTest.TestConfig.class)
class AuthenticationAspectTest {

    @Configuration
    @EnableAspectJAutoProxy
    @ComponentScan(basePackageClasses = {AuthenticationAspect.class})
    static class TestConfig {
        @Bean
        public AuthenticationService authenticationService() {
            return Mockito.mock(AuthenticationService.class);
        }

        @Bean
        public TestService testService() {
            return new TestService();
        }
    }

    static class TestService {
        @Authenticated
        public String securedMethod() {
            return "Access granted";
        }
    }

    @Autowired
    TestService testService;

    @Autowired
    AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        AuthContextHolder.clear();
    }

    @Test
    void shouldAllowAccessWhenAuthenticated() {
        AuthContextHolder.setCredentials("user", "pass");
        Mockito.when(authenticationService.authenticate("user", "pass")).thenReturn(true);

        String result = testService.securedMethod();

        assertEquals("Access granted", result);
    }

    @Test
    void shouldDenyAccessWhenNotAuthenticated() {
        AuthContextHolder.setCredentials("user", "wrong");
        Mockito.when(authenticationService.authenticate("user", "wrong")).thenReturn(false);

        assertThrows(SecurityException.class, () -> testService.securedMethod());
    }
}