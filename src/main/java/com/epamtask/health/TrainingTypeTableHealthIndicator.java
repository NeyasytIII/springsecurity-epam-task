package com.epamtask.health;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.health.base.AbstractTableCountHealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TrainingTypeTableHealthIndicator extends AbstractTableCountHealthIndicator {
    public TrainingTypeTableHealthIndicator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Loggable
    protected String getTableName() {
        return "training_type";
    }
}