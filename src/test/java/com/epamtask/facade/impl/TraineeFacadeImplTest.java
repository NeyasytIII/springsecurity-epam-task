package com.epamtask.facade.impl;

import com.epamtask.model.Trainee;
import com.epamtask.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeFacadeImplTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeFacadeImpl traineeFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_shouldDelegateToService() {
        traineeFacade.createTrainee("John", "Doe", "123 Elm St", new Date());
        verify(traineeService).createTrainee(eq("John"), eq("Doe"), eq("123 Elm St"), any());
    }

    @Test
    void updateTrainee_shouldDelegateToService() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "123 Elm St", new Date(), true);
        traineeFacade.updateTrainee(trainee);
        verify(traineeService).updateTrainee(trainee);
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        traineeFacade.deleteTrainee("johndoe");
        verify(traineeService).deleteTraineeByUsername("johndoe");
    }

    @Test
    void getTraineeById_shouldReturnResultFromService() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "123 Elm St", new Date(), true);
        when(traineeService.getTraineeById(1L)).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = traineeFacade.getTraineeById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getTraineeId());
    }

    @Test
    void getTraineeByUsername_shouldReturnResultFromService() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "123 Elm St", new Date(), true);
        when(traineeService.getTraineeByUsername("johndoe")).thenReturn(Optional.of(trainee));
        Optional<Trainee> result = traineeFacade.getTraineeByUsername("johndoe");
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void getAllTrainees_shouldReturnAll() {
        List<Trainee> list = List.of(new Trainee(1L, "John", "Doe", "123 Elm", new Date(), true));
        when(traineeService.getAllTrainees()).thenReturn(list);
        List<Trainee> result = traineeFacade.getAllTrainees();
        assertEquals(1, result.size());
    }

    @Test
    void updatePassword_shouldDelegateToService() {
        traineeFacade.updatePassword("johndoe", "newpass");
        verify(traineeService).updatePassword("johndoe", "newpass");
    }

    @Test
    void activateTrainee_shouldDelegateToService() {
        traineeFacade.activateTrainee("johndoe");
        verify(traineeService).activateUser("johndoe");
    }

    @Test
    void deactivateTrainee_shouldDelegateToService() {
        traineeFacade.deactivateTrainee("johndoe");
        verify(traineeService).deactivateUser("johndoe");
    }

    @Test
    void assignTrainers_shouldDelegateToService() {
        traineeFacade.assignTrainersToTrainee("johndoe", List.of("trainer1", "trainer2"));
        verify(traineeService).assignTrainersToTrainee("johndoe", List.of("trainer1", "trainer2"));
    }
    @Test
    void setInitialPassword_shouldDelegateToService() {
        traineeFacade.setInitialPassword("johndoe", "initpass");
        verify(traineeService).setInitialPassword("johndoe", "initpass");
    }
}