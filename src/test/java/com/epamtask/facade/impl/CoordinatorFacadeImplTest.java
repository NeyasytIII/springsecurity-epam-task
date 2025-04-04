package com.epamtask.facade.impl;
import com.epamtask.facade.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CoordinatorFacadeImplTest {

    private TraineeFacade traineeFacade;
    private TrainerFacade trainerFacade;
    private TrainingFacade trainingFacade;
    private TrainingTypeFacade trainingTypeFacade;
    private CoordinatorFacade coordinatorFacade;

    @BeforeEach
    void setUp() {
        traineeFacade = mock(TraineeFacade.class);
        trainerFacade = mock(TrainerFacade.class);
        trainingFacade = mock(TrainingFacade.class);
        trainingTypeFacade = mock(TrainingTypeFacade.class);

        coordinatorFacade = new CoordinatorFacadeImpl(
                traineeFacade,
                trainerFacade,
                trainingFacade,
                trainingTypeFacade
        );
    }

    @Test
    void testGetTraineeFacade() {
        assertEquals(traineeFacade, coordinatorFacade.getTraineeFacade());
    }

    @Test
    void testGetTrainerFacade() {
        assertEquals(trainerFacade, coordinatorFacade.getTrainerFacade());
    }

    @Test
    void testGetTrainingFacade() {
        assertEquals(trainingFacade, coordinatorFacade.getTrainingFacade());
    }

    @Test
    void testGetTrainingTypeFacade() {
        assertEquals(trainingTypeFacade, coordinatorFacade.getTrainingTypeFacade());
    }
}