package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.aspect.annotation.MeasureDb;
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
    @MeasureDb(table = "trainee", operation = "save")
    @Override
    public Trainee save(Trainee trainee) {
        return entityManager.merge(trainee);
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "select")
    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "select")
    @Override
    public List<Trainee> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)
                .getResultList();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "delete")
    @Override
    public void delete(Trainee entity) {
        entityManager.remove(entityManager.contains(entity)
                ? entity
                : entityManager.merge(entity));
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "delete")
    @Override
    public void deleteById(Long id) {
        Trainee t = entityManager.find(Trainee.class, id);
        if (t != null) {
            entityManager.remove(t);
        }
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "select")
    @Override
    public Optional<Trainee> findByUsername(String username) {
        TypedQuery<Trainee> q = entityManager.createQuery(
                "SELECT t FROM Trainee t LEFT JOIN FETCH t.trainers WHERE t.userName = :username",
                Trainee.class);
        q.setParameter("username", username);
        return q.getResultStream().findFirst();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "select")
    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        Long cnt = entityManager.createQuery(
                        "SELECT COUNT(t) FROM Trainee t WHERE t.userName = :username AND t.password = :password",
                        Long.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        return cnt > 0;
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "update")
    @Override
    public void updatePassword(String username, String newPassword) {
        entityManager.createQuery(
                        "UPDATE Trainee t SET t.password = :newPassword WHERE t.userName = :username")
                .setParameter("newPassword", newPassword)
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "update")
    @Override
    public void activateUser(String username) {
        entityManager.createQuery(
                        "UPDATE Trainee t SET t.isActive = TRUE WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "update")
    @Override
    public void deactivateUser(String username) {
        entityManager.createQuery(
                        "UPDATE Trainee t SET t.isActive = FALSE WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "delete")
    @Override
    public void deleteByUsername(String username) {
        entityManager.createQuery(
                        "DELETE FROM Trainee t WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "update")
    @Override
    public void update(Trainee trainee) {
        entityManager.merge(trainee);
    }

    @Loggable
    @MeasureDb(table = "trainee", operation = "update")
    @Override
    public void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = findByUsername(traineeUsername).orElseThrow();
        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(
                entityManager.createQuery(
                                "SELECT t FROM Trainer t WHERE t.userName IN :usernames", Trainer.class)
                        .setParameter("usernames", trainerUsernames)
                        .getResultList()
        );
        entityManager.merge(trainee);
    }

}