package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.repository.TraineeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Loggable
    @Override
    public Trainee save(Trainee trainee) {
        return entityManager.merge(trainee);
    }

    @Loggable
    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Loggable
    @Override
    public List<Trainee> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
    }

    @Loggable
    @Override
    public void delete(Trainee entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Loggable
    @Override
    public void deleteById(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if (trainee != null) {
            entityManager.remove(trainee);
        }
    }

    @Loggable
    @Override
    public Optional<Trainee> findByUsername(String username) {
        TypedQuery<Trainee> query = entityManager.createQuery(
                "SELECT t FROM Trainee t LEFT JOIN FETCH t.trainers WHERE t.userName = :username", Trainee.class);
        query.setParameter("username", username);
        List<Trainee> result = query.getResultList();
        return result.stream().findFirst();
    }

    @Loggable
    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        return entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.userName = :username AND t.password = :password", Long.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult() > 0;
    }

    @Loggable
    @Override
    public void updatePassword(String username, String newPassword) {
        int updated = entityManager.createQuery("UPDATE Trainee t SET t.password = :newPassword WHERE t.userName = :username")
                .setParameter("newPassword", newPassword)
                .setParameter("username", username)
                .executeUpdate();
        entityManager.flush();
        System.out.println("Trainee updatePassword updated records: " + updated);
    }

    @Loggable
    @Override
    public void activateUser(String username) {
        entityManager.createQuery("UPDATE Trainee t SET t.isActive = TRUE WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @Override
    public void deactivateUser(String username) {
        entityManager.createQuery("UPDATE Trainee t SET t.isActive = FALSE WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @Override
    public void deleteByUsername(String username) {
        entityManager.createQuery("DELETE FROM Trainee t WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @Override
    public void update(Trainee trainee) {
        entityManager.merge(trainee);
    }

    @Loggable
    @Override
    public void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = findByUsername(traineeUsername).orElseThrow();
        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(
                entityManager.createQuery("SELECT t FROM Trainer t WHERE t.userName IN :usernames", Trainer.class)
                        .setParameter("usernames", trainerUsernames)
                        .getResultList()
        );
        entityManager.merge(trainee);
    }
}