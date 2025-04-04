package com.epamtask.aspect;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class TransactionLoggingAspectTest {

    static class TransactionalService {
        @Transactional
        public String process() {
            return "done";
        }
    }

    @Test
    void testTransactionLogging() {
        TransactionalService target = new TransactionalService();
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(new TransactionLoggingAspect());

        TransactionalService proxy = factory.getProxy();
        String result = proxy.process();

        assertEquals("done", result);
    }
}