//package com.epamtask.storege.loader;
//
//import com.epamtask.exception.InvalidDataException;
//import com.epamtask.model.Trainer;
//import com.epamtask.storege.loader.filereaders.JsonFileReader;
//import com.epamtask.storege.loader.validation.uservalidation.TrainerValidator;
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
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainerStorageLoaderTest {
//
//    @Mock
//    private ResourceLoader resourceLoader;
//
//    @Mock
//    private JsonFileReader trainerFileReader;
//
//    @Mock
//    private TrainerValidator trainerValidator;
//
//    @Mock
//    private Resource resource;
//
//    private TrainerStorageLoader trainerStorageLoader;
//    private Map<Long, Trainer> trainerStorage;
//
//    private final String validTrainerFilePath = "trainer-json-data/valid-data/valid-trainer.json";
//    private final String duplicateTrainerFilePath = "trainer-json-data/invalid-data/duplicate-trainer.json";
//    private final String invalidNameTrainerFilePath = "trainer-json-data/invalid-data/invalid-name-trainer.json";
//    private final String missingUserTrainerFilePath = "trainer-json-data/invalid-data/missing-user-trainer.json";
//
//    @BeforeEach
//    void setUp() throws Exception {
//        trainerStorage = new HashMap<>();
//        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, validTrainerFilePath, trainerFileReader, trainerValidator);
//        lenient().when(resourceLoader.getResource(anyString())).thenReturn(resource);
//        lenient().when(resource.exists()).thenReturn(true);
//        lenient().when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("sample data".getBytes()));
//    }
//
//    @Test
//    void testLoadValidTrainers() {
//        Trainer trainer1 = new Trainer(1L, "John", "Doe", "Strength", true);
//        Trainer trainer2 = new Trainer(2L, "Jane", "Smith", "Cardio", true);
//
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, validTrainerFilePath)).thenReturn(true);
//            when(trainerFileReader.readFromFile(resource, Trainer.class)).thenReturn(List.of(trainer1, trainer2));
//            when(trainerValidator.validate(anyList())).thenReturn(Collections.emptyList());
//            trainerStorageLoader.loadTrainers(trainerStorage);
//            trainerStorage.forEach((id, trainer) -> {
//                System.out.println("Trainer ID: " + id + ", Username: " + trainer.getUserName());
//            });
//
//            assertEquals(2, trainerStorage.size());
//            assertEquals(trainer1, trainerStorage.get(1L));
//            assertEquals(trainer2, trainerStorage.get(2L));
//            assertEquals("John.Doe", trainerStorage.get(1L).getUserName());
//            assertEquals("Jane.Smith", trainerStorage.get(2L).getUserName());
//            verify(trainerFileReader).readFromFile(resource, Trainer.class);
//            verify(trainerValidator).validate(anyList());
//        }
//    }
//
//    @Test
//    void testLoadDuplicateTrainers() {
//        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, duplicateTrainerFilePath, trainerFileReader, trainerValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, duplicateTrainerFilePath)).thenReturn(true);
//            when(trainerFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
//            when(trainerValidator.validate(anyList())).thenReturn(List.of("Duplicate trainer ID"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainerStorageLoader.loadTrainers(trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainers"));
//            assertTrue(trainerStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadInvalidNameTrainers() {
//        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, invalidNameTrainerFilePath, trainerFileReader, trainerValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, invalidNameTrainerFilePath)).thenReturn(true);
//            when(trainerFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
//            when(trainerValidator.validate(anyList())).thenReturn(List.of("Invalid trainer name"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainerStorageLoader.loadTrainers(trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainers"));
//            assertTrue(trainerStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadMissingUserTrainers() {
//        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, missingUserTrainerFilePath, trainerFileReader, trainerValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, missingUserTrainerFilePath)).thenReturn(true);
//            when(trainerFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
//            when(trainerValidator.validate(anyList())).thenReturn(List.of("Trainer references non-existing user"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    trainerStorageLoader.loadTrainers(trainerStorage)
//            );
//            assertTrue(exception.getMessage().contains("Errors validating trainers"));
//            assertTrue(trainerStorage.isEmpty());
//        }
//    }
//}