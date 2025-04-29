package com.epamtask.facade.impl;

import com.epamtask.dto.trainerdto.TrainerProfileResponseDto;
import com.epamtask.dto.trainerdto.TrainerShortDto;
import com.epamtask.exception.NotFoundException;
import com.epamtask.mapper.TraineeMapper;
import com.epamtask.mapper.TrainerMapper;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.service.TraineeService;
import com.epamtask.service.TrainerService;
import com.epamtask.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerFacadeImplTest {

    @Mock private TrainerService trainerService;
    @Mock private TrainingService trainingService;
    @Mock private TraineeService traineeService;
    @Mock private TrainerMapper trainerMapper;
    @Mock private TraineeMapper traineeMapper;

    @InjectMocks private TrainerFacadeImpl trainerFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_shouldDelegateToService() {
        trainerFacade.createTrainer("John", "Doe", "CARDIO");
        verify(trainerService).createTrainer("John", "Doe", "CARDIO");
    }

    @Test
    void updateTrainer_shouldDelegateToService() {
        Trainer trainer = new Trainer();
        trainerFacade.updateTrainer(trainer);
        verify(trainerService).updateTrainer(trainer);
    }

    @Test
    void getTrainerById_shouldReturnTrainer() {
        Trainer trainer = new Trainer();
        when(trainerService.getTrainerById(1L)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerFacade.getTrainerById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getTrainerByUsername_shouldReturnTrainer() {
        Trainer trainer = new Trainer();
        when(trainerService.getTrainerByUsername("Michael.Scott")).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerFacade.getTrainerByUsername("Michael.Scott");
        assertTrue(result.isPresent());
    }

    @Test
    void getAllTrainers_shouldReturnList() {
        List<Trainer> list = List.of(new Trainer());
        when(trainerService.getAllTrainers()).thenReturn(list);
        List<Trainer> result = trainerFacade.getAllTrainers();
        assertEquals(1, result.size());
    }

    @Test
    void activateUser_shouldDelegate() {
        trainerFacade.activateUser("user");
        verify(trainerService).activateUser("user");
    }

    @Test
    void deactivateUser_shouldDelegate() {
        trainerFacade.deactivateUser("user");
        verify(trainerService).deactivateUser("user");
    }

    @Test
    void getNotAssignedToTrainee_shouldReturnList() {
        List<Trainer> list = List.of(new Trainer());
        when(trainerService.getNotAssignedToTrainee("trainee")).thenReturn(list);
        List<Trainer> result = trainerFacade.getNotAssignedToTrainee("trainee");
        assertEquals(1, result.size());
    }

    @Test
    void getTrainersNotAssignedToTrainee_shouldReturnList() {
        List<Trainer> list = List.of(new Trainer());
        when(trainerService.getTrainersNotAssignedToTrainee("trainee")).thenReturn(list);
        List<Trainer> result = trainerFacade.getTrainersNotAssignedToTrainee("trainee");
        assertEquals(1, result.size());
    }

    @Test
    void getTrainerProfile_shouldReturnProfile() {
        String username = "Michael.Scott";
        Trainer trainer = new Trainer();
        trainer.setUserName(username);
        Training training = new Training();
        Trainee trainee = new Trainee();
        training.setTrainee(trainee);
        TrainerProfileResponseDto dto = new TrainerProfileResponseDto();
        dto.setTrainees(List.of());

        when(trainerService.getTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingService.getTrainingsByTrainerUsernameAndCriteria(eq(username), any(), any(), any())).thenReturn(List.of(training));
        when(trainerMapper.toProfileDto(trainer)).thenReturn(dto);
        when(traineeMapper.toShortDtoList(Set.of(trainee))).thenReturn(List.of());

        TrainerProfileResponseDto result = trainerFacade.getTrainerProfile(username);
        assertNotNull(result);
    }

    @Test
    void getTrainerProfile_shouldThrowIfNotFound() {
        when(trainerService.getTrainerByUsername("none")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trainerFacade.getTrainerProfile("none"));
    }

    @Test
    void getFreeTrainersNotAssignedByTrainings_shouldReturnFreeList() {
        String traineeUsername = "Test.Trainee";
        Trainee trainee = new Trainee();
        Trainer trainer1 = new Trainer(); trainer1.setUserName("t1"); trainer1.setActive(true);
        Trainer trainer2 = new Trainer(); trainer2.setUserName("t2"); trainer2.setActive(true);
        Training training = new Training(); training.setTrainer(trainer1);

        when(traineeService.getTraineeByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainingService.getTrainingsByTraineeUsernameAndCriteria(eq(traineeUsername), any(), any(), any(), any())).thenReturn(List.of(training));
        when(trainerService.getAllTrainers()).thenReturn(List.of(trainer1, trainer2));
        when(trainerMapper.toShortDtoList(Set.of(trainer2))).thenReturn(List.of(new TrainerShortDto()));

        List<TrainerShortDto> result = trainerFacade.getFreeTrainersNotAssignedByTrainings(traineeUsername);
        assertEquals(0, result.size());
    }
}