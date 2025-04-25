//package com.epamtask.service.impl.fileimpl;
//
//import com.epamtask.model.Trainee;
//import com.epamtask.model.Trainer;
//import com.epamtask.service.impl.TraineeServiceImpl;
//import com.epamtask.storege.datamodes.TraineeStorage;
//import com.epamtask.storege.datamodes.TrainerStorage;
//import com.epamtask.utils.UserNameGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceFileModeImplTest {
//
//    @Mock
//    private TraineeStorage fileTraineeStorage;
//    @Mock
//    private TrainerStorage trainerStorage;
//    @Mock
//    private UserNameGenerator userNameGenerator;
//
//    @InjectMocks
//    private TraineeServiceImpl traineeService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        traineeService = new TraineeServiceImpl("FILE", null, fileTraineeStorage, trainerStorage, userNameGenerator);
//    }
//
//    @Test
//    void testCreateTrainee_Success() {
//        Long id = 1L;
//        when(fileTraineeStorage.findById(id)).thenReturn(Optional.empty());
//        when(fileTraineeStorage.findAll()).thenReturn(Collections.emptyList());
//        when(userNameGenerator.generateUserName(eq("John"), eq("Doe"))).thenReturn("john.doe");
//        traineeService.createTrainee("John", "Doe", "Address", new Date());
//        verify(fileTraineeStorage).save(any(Trainee.class));
//    }
//
//    @Test
//    void testUpdateTrainee_Success() {
//        Trainee trainee = new Trainee(1L, "John", "Doe", "Address", new Date(), true);
//        trainee.setUserName("john");
//        traineeService.updateTrainee(trainee);
//        verify(fileTraineeStorage).save(trainee);
//    }
//
//    @Test
//    void testDeleteTrainee_Success() {
//        traineeService.deleteTrainee(1L);
//        verify(fileTraineeStorage).deleteById(1L);
//    }
//
//    @Test
//    void testGetTraineeById() {
//        Trainee trainee = new Trainee();
//        when(fileTraineeStorage.findById(1L)).thenReturn(Optional.of(trainee));
//        Optional<Trainee> result = traineeService.getTraineeById(1L);
//        assertTrue(result.isPresent());
//        verify(fileTraineeStorage).findById(1L);
//    }
//
//    @Test
//    void testGetAllTrainees() {
//        List<Trainee> list = List.of(new Trainee());
//        when(fileTraineeStorage.findAll()).thenReturn(list);
//        List<Trainee> result = traineeService.getAllTrainees();
//        assertEquals(1, result.size());
//        verify(fileTraineeStorage).findAll();
//    }
//
//    @Test
//    void testAssignTrainersToTrainee_Success() {
//        Trainee trainee = new Trainee();
//        trainee.setTrainers(new HashSet<>());
//        Trainer trainer = new Trainer();
//        when(fileTraineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
//        when(trainerStorage.findByUsername("trainer1")).thenReturn(Optional.of(trainer));
//        traineeService.assignTrainersToTrainee("john", List.of("trainer1"));
//        verify(fileTraineeStorage).save(any());
//    }
//
//    @Test
//    void testDeleteTraineeByUsername() {
//        Trainee trainee = new Trainee();
//        when(fileTraineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
//        traineeService.deleteTraineeByUsername("john");
//        verify(fileTraineeStorage).deleteByUsername("john");
//    }
//
//    @Test
//    void testUpdatePassword() {
//        Trainee trainee = new Trainee();
//        when(fileTraineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
//        traineeService.updatePassword("john", "newpass");
//        verify(fileTraineeStorage).save(trainee);
//    }
//
//    @Test
//    void testActivateUser() {
//        Trainee trainee = new Trainee();
//        when(fileTraineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
//        traineeService.activateUser("john");
//        assertTrue(trainee.isActive());
//        verify(fileTraineeStorage).save(trainee);
//    }
//
//    @Test
//    void testDeactivateUser() {
//        Trainee trainee = new Trainee();
//        when(fileTraineeStorage.findByUsername("john")).thenReturn(Optional.of(trainee));
//        traineeService.deactivateUser("john");
//        assertFalse(trainee.isActive());
//        verify(fileTraineeStorage).save(trainee);
//    }
//}