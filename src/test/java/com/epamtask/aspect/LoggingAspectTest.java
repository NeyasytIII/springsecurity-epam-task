package com.epamtask.aspect;

import com.epamtask.aspect.annotation.Loggable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class LoggingAspectTest {

    static class TestService {
        @Loggable
        public String doSomething(String arg) {
            return "Hello " + arg;
        }
    }

    @Test
    void testLoggableAnnotation() {
        LoggingAspect aspect = new LoggingAspect();
        TestService target = new TestService();

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(aspect);
        TestService proxy = factory.getProxy();

        String result = proxy.doSomething("World");
        assertEquals("Hello World", result);
    }
}