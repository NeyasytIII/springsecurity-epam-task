package com.epamtask.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingConfig {

    @PostConstruct
    public void configure() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        ConsoleAppender<ch.qos.logback.classic.spi.ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        Logger rootLogger = context.getLogger("ROOT");
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(consoleAppender);
        context.getLogger("org.hibernate.SQL").setLevel(Level.DEBUG);
        context.getLogger("org.hibernate.type.descriptor.sql.BasicBinder").setLevel(Level.TRACE);
        context.getLogger("org.springframework.orm.jpa").setLevel(Level.DEBUG);
        context.getLogger("org.springframework.transaction").setLevel(Level.DEBUG);

        System.out.println("Logging configuration initialized!");
    }
}