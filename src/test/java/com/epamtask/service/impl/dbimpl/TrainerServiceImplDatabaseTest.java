package com.epamtask.service.impl.dbimpl;

import com.epamtask.config.ApplicationContextProvider;
import com.epamtask.model.Trainer;
import com.epamtask.service.TrainingTypeService;
import com.epamtask.service.impl.TrainerServiceImpl;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceImplDatabaseTest {

    @Mock
    private TrainerStorage databaseTrainerStorage;

    @Mock
    private UserNameGenerator userNameGenerator;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainerServiceImpl trainerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainerService = new TrainerServiceImpl(
                "DATABASE",
                databaseTrainerStorage,
                mock(TrainerStorage.class),
                userNameGenerator,
                trainingTypeService
        );

        ApplicationContext context = mock(ApplicationContext.class);
        ApplicationContextProvider.setContext(context);
        when(context.getBean(TrainerStorage.class)).thenReturn(databaseTrainerStorage);
    }

    @Test
    void testUpdateTrainer_Valid() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("john.doe");

        when(databaseTrainerStorage.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

        trainerService.updateTrainer(trainer);

        verify(databaseTrainerStorage).save(trainer);
    }

    @Test
    void testGetTrainerById() {
        Trainer trainer = new Trainer();
        when(databaseTrainerStorage.findById(2L)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerService.getTrainerById(2L);
        assertTrue(result.isPresent());
        verify(databaseTrainerStorage).findById(2L);
    }

    @Test
    void testGetTrainerByUsername() {
        Trainer trainer = new Trainer();
        when(databaseTrainerStorage.findByUsername("j.doe")).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerService.getTrainerByUsername("j.doe");
        assertTrue(result.isPresent());
        verify(databaseTrainerStorage).findByUsername("j.doe");
    }

    @Test
    void testGetAllTrainers() {
        List<Trainer> list = List.of(new Trainer());
        when(databaseTrainerStorage.findAll()).thenReturn(list);
        List<Trainer> result = trainerService.getAllTrainers();
        assertEquals(1, result.size());
        verify(databaseTrainerStorage).findAll();
    }

    @Test
    void testDeleteTrainer() {
        trainerService.deleteTrainer(5L);
        verify(databaseTrainerStorage).deleteById(5L);
    }


    @Test
    void testActivateUser() {
        trainerService.activateUser("John.Doe");
        verify(databaseTrainerStorage).activateUser("John.Doe");
    }

    @Test
    void testDeactivateUser() {
        trainerService.deactivateUser("John.Doe");
        verify(databaseTrainerStorage).deactivateUser("John.Doe");
    }
}