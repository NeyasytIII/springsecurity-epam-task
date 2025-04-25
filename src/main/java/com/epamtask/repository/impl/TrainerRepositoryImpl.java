package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.aspect.annotation.MeasureDb;
import com.epamtask.model.Trainer;
import com.epamtask.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Loggable
    @MeasureDb(table = "trainer", operation = "save")
    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getTrainerId() == null) {
            entityManager.persist(trainer);
            return trainer;
        } else {
            return entityManager.merge(trainer);
        }
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "select")
    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "select")
    @Override
    public List<Trainer> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "delete")
    @Override
    public void deleteById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "delete")
    @Override
    public void delete(Trainer entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            Trainer merged = entityManager.merge(entity);
            entityManager.remove(merged);
        }
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "select")
    @Override
    public Optional<Trainer> findByUsername(String username) {
        return entityManager.createQuery(
                        "SELECT t FROM Trainer t WHERE t.userName = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "delete")
    @Override
    public void deleteByUsername(String username) {
        entityManager.createQuery("DELETE FROM Trainer t WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "select")
    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(t) FROM Trainer t WHERE t.userName = :username AND t.password = :password",
                        Long.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        return count > 0;
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "update")
    @Override
    public void updatePassword(String username, String newPassword) {
        entityManager.createQuery(
                        "UPDATE Trainer t SET t.password = :newPassword WHERE t.userName = :username")
                .setParameter("newPassword", newPassword)
                .setParameter("username", username)
                .executeUpdate();
        entityManager.flush();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "update")
    @Override
    public void activateUser(String username) {
        entityManager.createQuery(
                        "UPDATE Trainer t SET t.isActive = true WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "update")
    @Override
    public void update(Trainer trainer) {
        entityManager.merge(trainer);
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "update")
    @Override
    public void deactivateUser(String username) {
        entityManager.createQuery(
                        "UPDATE Trainer t SET t.isActive = false WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Loggable
    @MeasureDb(table = "trainer", operation = "select")
    public List<Trainer> findNotAssignedToTrainee(String traineeUsername) {
        String q = "SELECT t FROM Trainer t WHERE t.trainerId NOT IN (" +
                "SELECT trInner.trainerId FROM Trainee tJoin JOIN tJoin.trainers trInner " +
                "WHERE tJoin.userName = :username)";
        return entityManager.createQuery(q, Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList();
    }
}