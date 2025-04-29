package com.epamtask.service.impl.dbimpl;
import com.epamtask.config.ApplicationContextProvider;
import com.epamtask.exception.NotFoundException;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.service.impl.TraineeServiceImpl;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TraineeServiceImplDbTest {
    @Mock
    private TraineeStorage traineeStorage;

    @Mock
    private TrainerStorage trainerStorage;

    @Mock
    private UserNameGenerator userNameGenerator;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_shouldSaveTrainee() {
        when(userNameGenerator.generateUserName("John", "Doe")).thenReturn("john.doe");
        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.empty());

        TrainerStorage mockTrainerStorage = mock(TrainerStorage.class);
        when(mockTrainerStorage.findByUsername("john.doe")).thenReturn(Optional.empty());

        try (MockedStatic<ApplicationContextProvider> mocked = mockStatic(ApplicationContextProvider.class)) {
            mocked.when(() -> ApplicationContextProvider.getBean(TrainerStorage.class)).thenReturn(mockTrainerStorage);

            traineeService.createTrainee("John", "Doe", "Street 123", new Date());

            verify(traineeStorage).save(any(Trainee.class));
        }
    }

    @Test
    void createTrainee_shouldThrowException_whenUsernameTakenByTrainer() {
        when(userNameGenerator.generateUserName("John", "Doe")).thenReturn("john.doe");
        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.empty());
        when(trainerStorage.findByUsername("john.doe")).thenReturn(Optional.of(mock(Trainer.class)));

        try (MockedStatic<ApplicationContextProvider> mocked = mockStatic(ApplicationContextProvider.class)) {
            mocked.when(() -> ApplicationContextProvider.getBean(TrainerStorage.class)).thenReturn(trainerStorage);

            assertThrows(IllegalArgumentException.class, () ->
                    traineeService.createTrainee("John", "Doe", "Street 123", new Date()));
        }
    }

    @Test
    void updateTrainee_shouldUpdateFields() {
        Trainee existing = new Trainee();
        existing.setUserName("john.doe");

        Trainee updateDto = new Trainee();
        updateDto.setUserName("john.doe");
        updateDto.setFirstName("Johnny");
        updateDto.setLastName("Updated");

        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.of(existing));

        traineeService.updateTrainee(updateDto);

        verify(traineeStorage).save(existing);
    }

    @Test
    void deleteTrainee_shouldCallStorage() {
        traineeService.deleteTrainee(1L);
        verify(traineeStorage).deleteById(1L);
    }

    @Test
    void deleteTraineeByUsername_shouldDelete() {
        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.of(new Trainee()));

        traineeService.deleteTraineeByUsername("john.doe");

        verify(traineeStorage).deleteByUsername("john.doe");
    }

    @Test
    void deleteTraineeByUsername_shouldThrow_ifNotFound() {
        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                traineeService.deleteTraineeByUsername("john.doe"));
    }

    @Test
    void getTraineeById_shouldReturnResult() {
        when(traineeStorage.findById(1L)).thenReturn(Optional.of(new Trainee()));
        assertTrue(traineeService.getTraineeById(1L).isPresent());
    }

    @Test
    void getTraineeByUsername_shouldReturnResult() {
        when(traineeStorage.findByUsername("john.doe")).thenReturn(Optional.of(new Trainee()));
        assertTrue(traineeService.getTraineeByUsername("john.doe").isPresent());
    }

    @Test
    void getAllTrainees_shouldReturnList() {
        when(traineeStorage.findAll()).thenReturn(List.of(new Trainee()));
        List<Trainee> list = traineeService.getAllTrainees();
        assertEquals(1, list.size());
    }

    @Test
    void getAllTrainees_shouldThrow_ifEmpty() {
        when(traineeStorage.findAll()).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> traineeService.getAllTrainees());
    }

    @Test
    void activateUser_shouldCallStorage() {
        traineeService.activateUser("john.doe");
        verify(traineeStorage).activateUser("john.doe");
    }

    @Test
    void deactivateUser_shouldCallStorage() {
        traineeService.deactivateUser("john.doe");
        verify(traineeStorage).deactivateUser("john.doe");
    }

    @Test
    void assignTrainersToTrainee_shouldUpdateTrainerList() {
        traineeService.assignTrainersToTrainee("john.doe", List.of("trainer1", "trainer2"));
        verify(traineeStorage).updateTraineeTrainersList("john.doe", List.of("trainer1", "trainer2"));
    }
}