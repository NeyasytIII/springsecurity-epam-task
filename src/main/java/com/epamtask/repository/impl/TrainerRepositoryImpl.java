package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
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
    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }

    @Loggable
    @Override
    public List<Trainer> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class).getResultList();
    }

    @Override
    @Loggable
    public void deleteById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
    }

    @Override
    @Loggable
    public void delete(Trainer entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            Trainer merged = entityManager.merge(entity);
            entityManager.remove(merged);
        }
    }

    @Override
    @Loggable
    public Optional<Trainer> findByUsername(String username) {
        return entityManager.createQuery(
                        "SELECT t FROM Trainer t WHERE t.userName = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Loggable
    public void deleteByUsername(String username) {
        entityManager.createQuery("DELETE FROM Trainer t WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    public List<Trainer> findNotAssignedToTrainee(String traineeUsername) {
        String query = "SELECT t FROM Trainer t WHERE t.trainerId NOT IN (" +
                "SELECT trInner.trainerId FROM Trainee tJoin JOIN tJoin.trainers trInner " +
                "WHERE tJoin.userName = :username)";

        return entityManager.createQuery(query, Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList();
    }

    @Override
    @Loggable
    public boolean existsByUsernameAndPassword(String username, String password) {
        return entityManager.createQuery(
                        "SELECT COUNT(t) FROM Trainer t WHERE t.userName = :username AND t.password = :password", Long.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult() > 0;
    }

    @Override
    @Loggable
    public void updatePassword(String username, String newPassword) {
        int updated = entityManager.createQuery("UPDATE Trainer t SET t.password = :newPassword WHERE t.userName = :username")
                .setParameter("newPassword", newPassword)
                .setParameter("username", username)
                .executeUpdate();
        entityManager.flush();
        System.out.println("Trainer updatePassword updated records: " + updated);
    }

    @Override
    @Loggable
    public void activateUser(String username) {
        entityManager.createQuery("UPDATE Trainer t SET t.isActive = true WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    @Loggable
    public void update(Trainer trainer) {
        entityManager.merge(trainer);
    }

    @Override
    @Loggable
    public void deactivateUser(String username) {
        entityManager.createQuery("UPDATE Trainer t SET t.isActive = false WHERE t.userName = :username")
                .setParameter("username", username)
                .executeUpdate();
    }
}