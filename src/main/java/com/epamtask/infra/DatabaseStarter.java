package com.epamtask.infra;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
@Profile("dev|prod")
public class DatabaseStarter {

    @PostConstruct
    public void startDatabase() {
        if (!isPostgresRunning()) {
            try {
                new ProcessBuilder("docker-compose", "up", "-d").inheritIO().start().waitFor();
                System.out.println("Docker Compose started Postgres container.");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to start Docker Compose", e);
            }
        } else {
            System.out.println("Postgres is already running, no need to start docker-compose.");
        }
    }

    private boolean isPostgresRunning() {
        int port = getDatabasePort();
        try (Socket socket = new Socket("localhost", port)) {
            return socket.isConnected();
        } catch (IOException ex) {
            return false;
        }
    }

    private int getDatabasePort() {
        String profile = System.getProperty("spring.profiles.active");
        if ("prod".equals(profile)) {
            return 5434;
        } else {
            return 5433;
        }
    }
}