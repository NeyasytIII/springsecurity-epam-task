package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainer;
import com.epamtask.repository.TrainerRepository;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    @Loggable
    public void verifySave(String username) {
        Optional<Trainer> saved = findByUsername(username);
        if (saved.isPresent()) {
            System.out.println("✅ Trainer saved and confirmed from DB: " + saved.get());
        } else {
            System.out.println("❌ Trainer NOT found in DB after save: " + username);
        }
    }
    @Override
    @Transactional
    public void update(Trainer trainer) {
        trainerRepository.save(trainer);
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public boolean verifyLogin(String username, String password) {
        return trainerRepository.existsByUsernameAndPassword(username, password);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findNotAssignedToTrainee(String traineeUsername) {
        return trainerRepository.findNotAssignedToTrainee(traineeUsername);
    }
    @Transactional
    @Override
    public void updatePassword(String username, String newPassword) {
        Optional<Trainer> trainer = findByUsername(username);
        if (trainer.isPresent()) {
            trainer.get().setPassword(newPassword);
            save(trainer.get());
        } else {
            throw new IllegalArgumentException("Trainer not found");
        }
    }
}