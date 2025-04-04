//package com.epamtask.storege.loader;
//
//import com.epamtask.exception.InvalidDataException;
//import com.epamtask.model.Trainee;
//import com.epamtask.storege.loader.filereaders.JsonFileReader;
//import com.epamtask.storege.loader.validation.common.FileValidator;
//import com.epamtask.storege.loader.validation.uservalidation.TraineeValidator;
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
//import static org.mockito.ArgumentMatchers.anyMap;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TraineeStorageLoaderTest {
//
//    @Mock
//    private ResourceLoader resourceLoader;
//
//    @Mock
//    private JsonFileReader traineeFileReader;
//
//    @Mock
//    private TraineeValidator traineeValidator;
//
//    @Mock
//    private FileValidator fileValidator;
//
//    @Mock
//    private Resource resource;
//
//    private TraineeStorageLoader traineeStorageLoader;
//    private Map<Long, Trainee> traineeStorage;
//
//    private final String validTraineeFilePath = "trainee-json-data/valid-data/valid-trainee.json";
//    private final String duplicateTraineeFilePath = "trainee-json-data/invalid-data/duplicate-trainee.json";
//    private final String invalidNameTraineeFilePath = "trainee-json-data/invalid-data/invalid-name-trainee.json";
//    private final String negativeIdTraineeFilePath = "trainee-json-data/invalid-data/negative-id-trainee.json";
//
//    @BeforeEach
//    void setUp() throws Exception {
//        traineeStorage = new HashMap<>();
//        lenient().when(resourceLoader.getResource(anyString())).thenReturn(resource);
//        lenient().when(resource.exists()).thenReturn(true);
//        lenient().when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("sample data".getBytes()));
//    }
//
//    @Test
//    void testLoadValidTrainees() {
//        traineeStorageLoader = new TraineeStorageLoader(resourceLoader, validTraineeFilePath, fileValidator, traineeFileReader, traineeValidator);
//        Trainee trainee1 = new Trainee(1L, "John", "Doe", "Some address", new Date(), true);
//        Trainee trainee2 = new Trainee(2L, "Jane", "Smith", "Another address", new Date(), true);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, validTraineeFilePath)).thenReturn(true);
//            when(traineeFileReader.readFromFile(resource, Trainee.class)).thenReturn(List.of(trainee1, trainee2));
//            when(traineeValidator.validate(anyList())).thenReturn(Collections.emptyList());
//            traineeStorageLoader.loadTrainees(traineeStorage);
//            assertEquals(2, traineeStorage.size());
//            assertEquals(trainee1, traineeStorage.get(1L));
//            assertEquals(trainee2, traineeStorage.get(2L));
//        }
//    }
//
//    @Test
//    void testLoadDuplicateTrainees() {
//        traineeStorageLoader = new TraineeStorageLoader(resourceLoader, duplicateTraineeFilePath, fileValidator, traineeFileReader, traineeValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, duplicateTraineeFilePath)).thenReturn(true);
//            when(traineeFileReader.readFromFile(resource, Trainee.class)).thenReturn(Collections.emptyList());
//            when(traineeValidator.validate(anyList())).thenReturn(List.of("Duplicate trainee ID"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    traineeStorageLoader.loadTrainees(traineeStorage)
//            );
//            assertTrue(exception.getMessage().contains("Validation errors"));
//            assertTrue(traineeStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadInvalidNameTrainees() {
//        traineeStorageLoader = new TraineeStorageLoader(resourceLoader, invalidNameTraineeFilePath, fileValidator, traineeFileReader, traineeValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, invalidNameTraineeFilePath)).thenReturn(true);
//            when(traineeFileReader.readFromFile(resource, Trainee.class)).thenReturn(Collections.emptyList());
//            when(traineeValidator.validate(anyList())).thenReturn(List.of("Invalid trainee name"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    traineeStorageLoader.loadTrainees(traineeStorage)
//            );
//            assertTrue(exception.getMessage().contains("Validation errors"));
//            assertTrue(traineeStorage.isEmpty());
//        }
//    }
//
//    @Test
//    void testLoadNegativeIdTrainees() {
//        traineeStorageLoader = new TraineeStorageLoader(resourceLoader, negativeIdTraineeFilePath, fileValidator, traineeFileReader, traineeValidator);
//        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
//            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, negativeIdTraineeFilePath)).thenReturn(true);
//            when(traineeFileReader.readFromFile(resource, Trainee.class)).thenReturn(Collections.emptyList());
//            when(traineeValidator.validate(anyList())).thenReturn(List.of("Negative trainee ID"));
//            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
//                    traineeStorageLoader.loadTrainees(traineeStorage)
//            );
//            assertTrue(exception.getMessage().contains("Validation errors"));
//            assertTrue(traineeStorage.isEmpty());
//        }
//    }
//}