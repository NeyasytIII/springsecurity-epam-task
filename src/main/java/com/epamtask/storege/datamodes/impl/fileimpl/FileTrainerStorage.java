package com.epamtask.storege.datamodes.impl.fileimpl;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.dao.TrainerDAO;
import com.epamtask.model.Trainer;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("fileTrainerStorage")
public class FileTrainerStorage implements TrainerStorage {

    private final TrainerDAO trainerDAO;

    public FileTrainerStorage(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Loggable
    @Override
    public void save(Trainer trainer) {
        trainerDAO.create(trainer.getTrainerId(), trainer);
    }

    @Loggable
    @Override
    public Optional<Trainer> findById(Long id) {
        return trainerDAO.findById(id);
    }

    @Loggable
    @Override
    public Optional<Trainer> findByUsername(String username) {
        return trainerDAO.findByUsername(username);
    }

    @Loggable
    @Override
    public List<Trainer> findAll() {
        return trainerDAO.getAll().values().stream().toList();
    }
    @Loggable



    @Override
    public List<Trainer> findNotAssignedToTrainee(String traineeUsername) {
        return List.of();
    }



    @Loggable
    @Override
    public void deleteById(Long id) {
        trainerDAO.deleteById(id);
    }

    @Override
    public void activateUser(String username) {
        
    }

    @Override
    public void deactivateUser(String username) {

    }

}