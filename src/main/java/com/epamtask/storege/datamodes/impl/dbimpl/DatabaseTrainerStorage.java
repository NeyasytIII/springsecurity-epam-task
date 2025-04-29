package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainer;
import com.epamtask.repository.TrainerRepository;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Primary
@Component("databaseTrainerStorage")
public class DatabaseTrainerStorage implements TrainerStorage {

    private final TrainerRepository trainerRepository;

    public DatabaseTrainerStorage(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByUsername(String username) {
        return trainerRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findNotAssignedToTrainee(String traineeUsername) {
        return trainerRepository.findNotAssignedToTrainee(traineeUsername);
    }

    @Override
    @Transactional
    public void activateUser(String username) {
        trainerRepository.activateUser(username);
    }

    @Override
    @Transactional
    public void deactivateUser(String username) {
        trainerRepository.deactivateUser(username);
    }
}