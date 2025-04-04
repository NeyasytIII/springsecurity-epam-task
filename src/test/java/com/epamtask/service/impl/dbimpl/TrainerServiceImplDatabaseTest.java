package com.epamtask.service.impl.dbimpl;

import com.epamtask.model.Trainer;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.service.TrainingTypeService;
import com.epamtask.service.impl.TrainerServiceImpl;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.UserNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    }

    @Test
    void testCreateTrainer_Success() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String specialization = "CARDIO";

        when(databaseTrainerStorage.findById(id)).thenReturn(Optional.empty());
        when(databaseTrainerStorage.findAll()).thenReturn(List.of());
        when(userNameGenerator.generateUserName(eq(firstName), eq(lastName))).thenReturn("John.Doe");

        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity(TrainingType.CARDIO);
        when(trainingTypeService.getTrainingTypeByName("CARDIO"))
                .thenReturn(Optional.of(trainingTypeEntity));

        trainerService.createTrainer(firstName, lastName, specialization);

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(databaseTrainerStorage).save(captor.capture());

        Trainer saved = captor.getValue();
        assertEquals("John.Doe", saved.getUserName());
        assertNotNull(saved.getPassword());
        assertTrue(saved.isActive());
        assertEquals(trainingTypeEntity, saved.getSpecializationType());
    }

    @Test
    void testUpdateTrainer_Valid() {
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
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
    void testUpdatePassword() {
        Trainer trainer = new Trainer();
        when(databaseTrainerStorage.findByUsername("jane.doe")).thenReturn(Optional.of(trainer));
        trainerService.updatePassword("jane.doe", "newpass");
        assertEquals("newpass", trainer.getPassword());
        verify(databaseTrainerStorage).save(trainer);
    }

    @Test
    void testActivateUser() {
        Trainer trainer = new Trainer();
        trainer.setActive(false);
        when(databaseTrainerStorage.findByUsername("jane.doe")).thenReturn(Optional.of(trainer));
        trainerService.activateUser("jane.doe");
        assertTrue(trainer.isActive());
        verify(databaseTrainerStorage).save(trainer);
    }

    @Test
    void testDeactivateUser() {
        Trainer trainer = new Trainer();
        trainer.setActive(true);
        when(databaseTrainerStorage.findByUsername("jane.doe")).thenReturn(Optional.of(trainer));
        trainerService.deactivateUser("jane.doe");
        assertFalse(trainer.isActive());
        verify(databaseTrainerStorage).save(trainer);
    }
}