package com.epamtask.storege;

import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.storege.loader.TraineeStorageLoader;
import com.epamtask.storege.loader.TrainerStorageLoader;
import com.epamtask.storege.loader.TrainingStorageLoader;
import com.epamtask.storege.loader.initializer.StorageInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class StorageInitializerTest {

    @Mock
    private TraineeStorageLoader traineeLoader;
    @Mock
    private TrainerStorageLoader trainerLoader;
    @Mock
    private TrainingStorageLoader trainingLoader;

    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;
    private StorageInitializer storageInitializer;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        trainerStorage = new HashMap<>();
        trainingStorage = new HashMap<>();
        storageInitializer = new StorageInitializer(traineeStorage, trainerStorage, trainingStorage, traineeLoader, trainerLoader, trainingLoader);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testLoadDataSuccess() {
        doAnswer(invocation -> {
            Map<Long, Trainee> map = invocation.getArgument(0);
            map.put(1L, new Trainee(1L, "Alice", "Brown", "Address 1", null, true));
            return null;
        }).when(traineeLoader).loadTrainees(any());
        doAnswer(invocation -> {
            Map<Long, Trainer> map = invocation.getArgument(0);
            map.put(10L, new Trainer(10L, "Bob", "Smith", "Strength Training", true));
            return null;
        }).when(trainerLoader).loadTrainers(any());
        doAnswer(invocation -> {
            Map<Long, Training> map = invocation.getArgument(0);
            map.put(100L, new Training(100L, "Training A", null, null, "60 minutes"));
            return null;
        }).when(trainingLoader).loadTrainings(any(), any(), any());

        storageInitializer.loadData();

        String output = outContent.toString();
        assertTrue(output.contains("Starting data loading"));
        assertTrue(output.contains("Trainees loaded: 1"));
        assertTrue(output.contains("Trainers loaded: 1"));
        assertTrue(output.contains("Trainings loaded: 1"));
        assertTrue(output.contains("All data loaded successfully"));
    }

    @Test
    void testLoadDataInvalid() {
        doThrow(new InvalidDataException("Test error")).when(traineeLoader).loadTrainees(any());
        storageInitializer.loadData();
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Data loading error: Test error"));
    }

    @Test
    void testLoadDataUnexpectedError() {
        doThrow(new RuntimeException("Unexpected error")).when(traineeLoader).loadTrainees(any());
        storageInitializer.loadData();
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Unexpected error during storage initialization: Unexpected error"));
    }

    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}