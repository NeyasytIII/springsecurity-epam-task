//package com.epamtask.service.impl.fileimpl;
//
//import com.epamtask.model.Training;
//import com.epamtask.model.TrainingType;
//import com.epamtask.service.impl.TrainingServiceImpl;
//import com.epamtask.storege.datamodes.TrainingStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TrainingServiceFileModeImplTest {
//
//    private TrainingStorage fileTrainingStorage;
//    private TrainingServiceImpl trainingService;
//
//    @BeforeEach
//    void setUp() {
//        fileTrainingStorage = mock(TrainingStorage.class);
//        trainingService = new TrainingServiceImpl(
//                "file",
//                null,
//                fileTrainingStorage,
//                null,
//                null,
//                null
//        );
//    }
//
//    @Test
//    void testCreateTraining_Success() {
//        Long id = 1L;
//        when(fileTrainingStorage.findById(id)).thenReturn(Optional.empty());
//
//        trainingService.createTraining(id, 2L, 3L, "Yoga", TrainingType.STRENGTH, new Date(), "1h");
//
//        verify(fileTrainingStorage).save(any(Training.class));
//    }
//
//    @Test
//    void testCreateTraining_DuplicateId_ShouldThrowException() {
//        when(fileTrainingStorage.findById(1L)).thenReturn(Optional.of(new Training()));
//        assertThrows(IllegalArgumentException.class, () ->
//                trainingService.createTraining(1L, 2L, 3L, "Yoga", TrainingType.YOGA, new Date(), "1h"));
//    }
//
//    @Test
//    void testGetTrainingById_Success() {
//        Training training = new Training();
//        when(fileTrainingStorage.findById(1L)).thenReturn(Optional.of(training));
//        Optional<Training> result = trainingService.getTrainingById(1L);
//        assertTrue(result.isPresent());
//        assertEquals(training, result.get());
//    }
//
//    @Test
//    void testGetTrainingById_NullId_ShouldThrowException() {
//        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingById(null));
//    }
//
//    @Test
//    void testGetTrainingsByTrainerId_Success() {
//        Map<Long, Training> map = Map.of(1L, new Training());
//        when(fileTrainingStorage.findByTrainerId(2L)).thenReturn(map);
//        Map<Long, Training> result = trainingService.getTrainingsByTrainerId(2L);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetTrainingsByTrainerId_Null_ShouldThrowException() {
//        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingsByTrainerId(null));
//    }
//
//    @Test
//    void testGetTrainingsByTraineeId_Success() {
//        Map<Long, Training> map = Map.of(1L, new Training());
//        when(fileTrainingStorage.findByTraineeId(3L)).thenReturn(map);
//        Map<Long, Training> result = trainingService.getTrainingsByTraineeId(3L);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetTrainingsByTraineeId_Null_ShouldThrowException() {
//        assertThrows(IllegalArgumentException.class, () -> trainingService.getTrainingsByTraineeId(null));
//    }
//
//    @Test
//    void testGetAllTrainings_Success() {
//        when(fileTrainingStorage.findAll()).thenReturn(List.of(new Training()));
//        List<Training> result = trainingService.getAllTrainings();
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetAllTrainings_Empty_ShouldThrowException() {
//        when(fileTrainingStorage.findAll()).thenReturn(List.of());
//        assertThrows(IllegalStateException.class, () -> trainingService.getAllTrainings());
//    }
//
//    @Test
//    void testGetTrainingsByTraineeUsernameAndCriteria_Success() {
//        when(fileTrainingStorage.findByTraineeUsernameAndCriteria(any(), any(), any(), any(), any()))
//                .thenReturn(List.of(new Training()));
//        List<Training> result = trainingService.getTrainingsByTraineeUsernameAndCriteria("john", null, null, null, null);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetTrainingsByTraineeUsernameAndCriteria_InvalidUsername_ShouldThrow() {
//        assertThrows(IllegalArgumentException.class, () ->
//                trainingService.getTrainingsByTraineeUsernameAndCriteria(" ", null, null, null, null));
//    }
//
//    @Test
//    void testGetTrainingsByTrainerUsernameAndCriteria_Success() {
//        when(fileTrainingStorage.findByTrainerUsernameAndCriteria(any(), any(), any(), any()))
//                .thenReturn(List.of(new Training()));
//        List<Training> result = trainingService.getTrainingsByTrainerUsernameAndCriteria("mike", null, null, null);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetTrainingsByTrainerUsernameAndCriteria_InvalidUsername_ShouldThrow() {
//        assertThrows(IllegalArgumentException.class, () ->
//                trainingService.getTrainingsByTrainerUsernameAndCriteria(" ", null, null, null));
//    }
//}