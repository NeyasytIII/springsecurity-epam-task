package com.epamtask.facade;

public interface CoordinatorFacade {
    TraineeFacade getTraineeFacade();
    TrainerFacade getTrainerFacade();
    TrainingFacade getTrainingFacade();
    TrainingTypeFacade getTrainingTypeFacade();
}