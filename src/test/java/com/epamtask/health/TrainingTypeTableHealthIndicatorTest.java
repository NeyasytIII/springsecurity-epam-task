package com.epamtask.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingTypeTableHealthIndicatorTest {

    private TrainingTypeTableHealthIndicator indicator;

    @BeforeEach
    void setUp() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(7);

        indicator = new TrainingTypeTableHealthIndicator(dataSource);
    }

    @Test
    void shouldReturnHealthUpWhenCountPositive() {
        Health health = indicator.health();
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails().values()).contains(7);
    }

    @Test
    void shouldReturnHealthDownOnException() throws Exception {
        DataSource errorDs = mock(DataSource.class);
        Connection errorConn = mock(Connection.class);
        when(errorDs.getConnection()).thenReturn(errorConn);
        when(errorConn.createStatement()).thenThrow(new RuntimeException("DB error"));

        Health health = new TrainingTypeTableHealthIndicator(errorDs).health();
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails().values().toString()).contains("DB error");
    }
}