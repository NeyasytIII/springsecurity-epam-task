//package com.epamtask.storege.loader;
//
//import com.epamtask.exception.InvalidDataException;
//import com.epamtask.model.Training;
//import com.epamtask.model.Trainee;
//import com.epamtask.model.Trainer;
//import com.epamtask.storege.loader.filereaders.JsonFileReader;
//import com.epamtask.storege.loader.validation.TrainingValidator;
//import com.epamtask.storege.loader.validation.common.FileValidator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//
//import java.io.ByteArrayInputStream;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingStorageLoaderTest {
//
//    @Mock
//    private ResourceLoader resourceLoader;
//
//    @Mock
//    private JsonFileReader trainingFileReader;
//
//    @Mock
//    private TrainingValidator trainingValidator;
//
//    @Mock
//    private Resource resource;
//
//    private TrainingStorageLoader trainingStorageLoader;
//    private Map<Long, Training> trainingStorage;
//    private Map<Long, Trainee> traineeStorage;
//    private Map<Long, Trainer> trainerStorage;
//
//    private final String validTrainingFilePath = "training-json-data/valid-data/valid-training.json";
//    private final String duplicateTrainingFilePath = "training-json-data/invalid-data/duplicate-training.json";
//    private final String invalidNameTrainingFilePath = "training-json-data/invalid-data/invalid-name-training.json";
//    private final String missingUserTrainingFilePath = "training-json-data/invalid-data/missing-user-training.json";
//
//    @BeforeEach
//    void setUp() throws Exception {
//        trainingStorage = new HashMap<>();
//        traineeStorage = new HashMap<>();
//        trainerStorage = new HashMap<>();
//        trainingStorageLoader = new TrainingStorageLoader(resourceLoader, validTrainingFilePath, trainingFileReader, trainingValidator);
//        lenient().when(resourceLoader.getResource(anyString())).thenReturn(resource);
//        lenient().when(resource.exists()).thenReturn(true);
//        lenient().when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("sample data".getBytes()));
//    }
//
//    @Test
//    void testLoadValidTrainings() {
//        Trainer trainer1 = new Trainer(1L, "Trainer", "One", "Strength", true);
//        Trainee trainee1 = new Trainee(1L, "Trainee", "One", "Some address", new Date(), true);
//        Training training1 = new Training(100L, "Training A", null, new Date(), "60 minutes");
//        training1.setTrainer(trainer1);
//        training1.setTrainee(trainee1);
//
//        Trainer trainer2 = new Trainer(2L, "Trainer", "Two", "Cardio", true);
//        Trainee trainee2 = new Trainee(2L, "Trainee", "Two", "Another address", new Date(), true);
//        Training training2 = new Training(200L, "Training B", null, new Date(), "45 minutes");
//        training2.setTrainer(trainer2);
//        training2.setTrainee(trainee2);
//
//        try (MockedStatic<FileValidator> fileValidatorMock = org.mockito.Mockito.mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, validTrainingFilePath)).thenReturn(true);
//            when(trainingFileReader.readFromFile(resource, Training.class)).thenReturn(List.of(training1, training2));
//            when(trainingValidator.validate(anyList(), anyMap(), anyMap())).thenReturn(Collections.emptyList());
//            trainingStorageLoader.loadTrainings(trainingStorage, traineeStorage, trainerStorage);
//            assertEquals(2, trainingStorage.size());
//            assertEquals(training1, trainingStorage.get(100L));
//            assertEquals(training2, trainingStorage.get(200L));
//        }
//    }
//
//    @Test
//    void testLoadDuplicateTrainings() {
//        trainingStorageLoader = new TrainingStorageLoader(resourceLoader, duplicateTrainingFilePath, trainingFileReader, trainingValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = org.mockito.Mockito.mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, duplicateTrainingFilePath)).thenReturn(true);
//            when(trainingFileReader.readFromFile(resource, Training.class)).thenReturn(Collections.emptyList());
//            when(trainingValidator.validate(anyList(), anyMap(), anyMap())).thenReturn(List.of("Duplicate training ID"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainingStorageLoader.loadTrainings(trainingStorage, traineeStorage, trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainings"));
//            assertTrue(trainingStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadInvalidNameTrainings() {
//        trainingStorageLoader = new TrainingStorageLoader(resourceLoader, invalidNameTrainingFilePath, trainingFileReader, trainingValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = org.mockito.Mockito.mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, invalidNameTrainingFilePath)).thenReturn(true);
//            when(trainingFileReader.readFromFile(resource, Training.class)).thenReturn(Collections.emptyList());
//            when(trainingValidator.validate(anyList(), anyMap(), anyMap())).thenReturn(List.of("Invalid training name"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainingStorageLoader.loadTrainings(trainingStorage, traineeStorage, trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainings"));
//            assertTrue(trainingStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadMissingUserTrainings() {
//        trainingStorageLoader = new TrainingStorageLoader(resourceLoader, missingUserTrainingFilePath, trainingFileReader, trainingValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = org.mockito.Mockito.mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, missingUserTrainingFilePath)).thenReturn(true);
//            when(trainingFileReader.readFromFile(resource, Training.class)).thenReturn(Collections.emptyList());
//            when(trainingValidator.validate(anyList(), anyMap(), anyMap())).thenReturn(List.of("Training references non-existing user"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainingStorageLoader.loadTrainings(trainingStorage, traineeStorage, trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainings"));
//            assertTrue(trainingStorage.isEmpty());
//        }
//    }
//}