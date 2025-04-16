package com.epamtask.service.impl.dbimpl;
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
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        traineeService = new TraineeServiceImpl(
                "DATABASE",
                traineeStorage,
                mock(TraineeStorage.class),
                trainerStorage,
                userNameGenerator
        );
    }

    @Test
    void createTrainee_Success() {
        when(traineeStorage.findById(1L)).thenReturn(Optional.empty());
        when(traineeStorage.findAll()).thenReturn(new ArrayList<>());
        when(userNameGenerator.generateUserName(any(), any())).thenReturn("john.doe");

        traineeService.createTrainee("John", "Doe", "Address", new Date());

        verify(traineeStorage).save(any(Trainee.class));
    }

    @Test
    void updateTrainee_Success() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("John.Doe");

        when(traineeStorage.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));

        traineeService.updateTrainee(trainee);

        verify(traineeStorage).save(trainee);
    }

    @Test
    void deleteTrainee_Success() {
        traineeService.deleteTrainee(1L);
        verify(traineeStorage).deleteById(1L);
    }

    @Test
    void deleteTraineeByUsername_Success() {
        when(traineeStorage.findByUsername("John")).thenReturn(Optional.of(new Trainee()));
        traineeService.deleteTraineeByUsername("John");
        verify(traineeStorage).deleteByUsername("John");
    }

    @Test
    void getTraineeById_Success() {
        when(traineeStorage.findById(1L)).thenReturn(Optional.of(new Trainee()));
        Optional<Trainee> result = traineeService.getTraineeById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getTraineeByUsername_Success() {
        when(traineeStorage.findByUsername("john")).thenReturn(Optional.of(new Trainee()));
        Optional<Trainee> result = traineeService.getTraineeByUsername("john");
        assertTrue(result.isPresent());
    }

    @Test
    void getAllTrainees_Success() {
        when(traineeStorage.findAll()).thenReturn(List.of(new Trainee()));
        List<Trainee> result = traineeService.getAllTrainees();
        assertEquals(1, result.size());
    }

    @Test
    void assignTrainersToTrainee_Success() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());
        Trainer trainer = new Trainer();
        when(traineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
        when(trainerStorage.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

        traineeService.assignTrainersToTrainee("john", List.of("trainer1"));

        verify(traineeStorage).save(trainee);
    }

    @Test
    void updatePassword_Success() {
        Trainee trainee = new Trainee();
        when(traineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.updatePassword("john", "newpass");

        verify(traineeStorage).save(trainee);
        assertEquals("newpass", trainee.getPassword());
    }

    @Test
    void activateUser_Success() {
        Trainee trainee = new Trainee();
        when(traineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.activateUser("john");

        verify(traineeStorage).save(trainee);
        assertTrue(trainee.isActive());
    }

    @Test
    void deactivateUser_Success() {
        Trainee trainee = new Trainee();
        when(traineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));

        traineeService.deactivateUser("john");

        verify(traineeStorage).save(trainee);
        assertFalse(trainee.isActive());
    }
}