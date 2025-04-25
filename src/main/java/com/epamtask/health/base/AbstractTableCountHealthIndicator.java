package com.epamtask.health.base;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTableCountHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    protected AbstractTableCountHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected abstract String getTableName();

    @Override
    @Loggable
    public Health health() {
        String table = getTableName();
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {

            if (rs.next()) {
                int count = rs.getInt(1);
                Map<String, Object> details = new LinkedHashMap<>();
                details.put("table", table);
                details.put("count", count);
                return Health.up().withDetails(details).build();
            } else {
                return Health.unknown().withDetail("table", table).withDetail("info", "No result").build();
            }

        } catch (Exception ex) {
            return Health.down(ex).withDetail("table", table).build();
        }
    }
}