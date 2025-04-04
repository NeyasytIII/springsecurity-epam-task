//package com.epamtask.service.impl.fileimpl;
//
//import com.epamtask.model.Trainer;
//import com.epamtask.service.impl.TrainerServiceImpl;
//import com.epamtask.storege.datamodes.TrainerStorage;
//import com.epamtask.utils.UserNameGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TrainerServiceFileModeImplTest {
//
//    @Mock
//    private TrainerStorage fileTrainerStorage;
//
//    @Mock
//    private UserNameGenerator userNameGenerator;
//
//    @InjectMocks
//    private TrainerServiceImpl trainerService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        trainerService = new TrainerServiceImpl("FILE", null, fileTrainerStorage, userNameGenerator, null);
//    }
//
//    @Test
//    void testCreateTrainer_Success() {
//        Long id = 1L;
//        when(fileTrainerStorage.findById(id)).thenReturn(Optional.empty());
//        when(fileTrainerStorage.findAll()).thenReturn(List.of());
//        when(userNameGenerator.generateUserName(eq("John"), eq("Doe")))
//                .thenReturn("John.Doe");
//
//        trainerService.createTrainer("John", "Doe", "Yoga");
//
//        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
//        verify(fileTrainerStorage).save(captor.capture());
//        assertEquals("John.Doe", captor.getValue().getUserName());
//    }
//
//    @Test
//    void testCreateTrainer_InvalidInput() {
//        assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(null, "A", "B"));
//        assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer( "", "A", "B"));
//        assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer( "A", null, "B"));
//        assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer( "A", "B", ""));
//    }
//
//    @Test
//    void testCreateTrainer_AlreadyExists() {
//        when(fileTrainerStorage.findById(1L)).thenReturn(Optional.of(new Trainer()));
//        assertThrows(IllegalArgumentException.class,
//                () -> trainerService.createTrainer("John", "Doe", "Yoga"));
//    }
//
//    @Test
//    void testUpdateTrainer() {
//        Trainer trainer = new Trainer();
//        trainer.setTrainerId(2L);
//        trainerService.updateTrainer(trainer);
//        verify(fileTrainerStorage).save(trainer);
//    }
//
//    @Test
//    void testUpdateTrainer_Null() {
//        assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(null));
//    }
//
//    @Test
//    void testGetTrainerById() {
//        Trainer trainer = new Trainer();
//        when(fileTrainerStorage.findById(5L)).thenReturn(Optional.of(trainer));
//        assertTrue(trainerService.getTrainerById(5L).isPresent());
//    }
//
//    @Test
//    void testGetTrainerByUsername() {
//        Trainer trainer = new Trainer();
//        when(fileTrainerStorage.findByUsername("username")).thenReturn(Optional.of(trainer));
//        assertTrue(trainerService.getTrainerByUsername("username").isPresent());
//    }
//
//    @Test
//    void testGetAllTrainers() {
//        when(fileTrainerStorage.findAll()).thenReturn(List.of(new Trainer()));
//        assertEquals(1, trainerService.getAllTrainers().size());
//    }
//
//    @Test
//    void testDeleteTrainer() {
//        trainerService.deleteTrainer(77L);
//        verify(fileTrainerStorage).deleteById(77L);
//    }
//
//    @Test
//    void testDeleteTrainer_Null() {
//        assertThrows(IllegalArgumentException.class, () -> trainerService.deleteTrainer(null));
//    }
//
//    @Test
//    void testUpdatePassword() {
//        Trainer trainer = new Trainer();
//        when(fileTrainerStorage.findByUsername("u")).thenReturn(Optional.of(trainer));
//        trainerService.updatePassword("u", "pass");
//        verify(fileTrainerStorage).save(trainer);
//        assertEquals("pass", trainer.getPassword());
//    }
//
//    @Test
//    void testActivateUser() {
//        Trainer trainer = new Trainer();
//        trainer.setActive(false);
//        when(fileTrainerStorage.findByUsername("u")).thenReturn(Optional.of(trainer));
//        trainerService.activateUser("u");
//        verify(fileTrainerStorage).save(trainer);
//        assertTrue(trainer.isActive());
//    }
//
//    @Test
//    void testDeactivateUser() {
//        Trainer trainer = new Trainer();
//        trainer.setActive(true);
//        when(fileTrainerStorage.findByUsername("u")).thenReturn(Optional.of(trainer));
//        trainerService.deactivateUser("u");
//        verify(fileTrainerStorage).save(trainer);
//        assertFalse(trainer.isActive());
//    }
//}