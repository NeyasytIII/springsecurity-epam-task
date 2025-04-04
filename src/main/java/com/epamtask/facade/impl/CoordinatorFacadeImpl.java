package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.facade.*;
import com.epamtask.security.AuthContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorFacadeImpl implements CoordinatorFacade {
    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;
    private final TrainingFacade trainingFacade;
    private final TrainingTypeFacade trainingTypeFacade;

    @Autowired
    public CoordinatorFacadeImpl(
            TraineeFacade traineeFacade, TrainerFacade trainerFacade,
            TrainingFacade trainingFacade, TrainingTypeFacade trainingTypeFacade
    ) {
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
        this.trainingFacade = trainingFacade;
        this.trainingTypeFacade = trainingTypeFacade;
    }

    @Loggable
    @Override
    public TraineeFacade getTraineeFacade() {
        return traineeFacade;
    }

    @Loggable
    @Override
    public TrainerFacade getTrainerFacade() {
        return trainerFacade;
    }

    @Loggable
    @Override
    public TrainingFacade getTrainingFacade() {
        return trainingFacade;
    }

    @Loggable
    @Override
    public TrainingTypeFacade getTrainingTypeFacade() {
        return trainingTypeFacade;
    }

    }
