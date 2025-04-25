package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.aspect.annotation.MeasureDb;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Loggable
    @MeasureDb(table = "training", operation = "save")
    @Override
    public Training save(Training training) {
        if (training.getTrainingId() == null) {
            entityManager.persist(training);
        } else {
            entityManager.merge(training);
        }
        return training;
    }

    @Loggable
    @MeasureDb(table = "training", operation = "update")
    @Override
    public void update(Training training) {
        entityManager.merge(training);
    }

    @Loggable
    @MeasureDb(table = "training", operation = "select")
    @Override
    public Optional<Training> findById(Long id) {
        String jpql = "SELECT t FROM Training t " +
                "JOIN FETCH t.trainingType " +
                "JOIN FETCH t.trainee " +
                "JOIN FETCH t.trainer " +
                "WHERE t.trainingId = :id";

        List<Training> results = entityManager.createQuery(jpql, Training.class)
                .setParameter("id", id)
                .getResultList();

        return results.stream().findFirst();
    }

    @Loggable
    @MeasureDb(table = "training", operation = "select")
    @Override
    public List<Training> findAll() {
        return entityManager.createQuery("SELECT t FROM Training t", Training.class)
                .getResultList();
    }

    @Loggable
    @MeasureDb(table = "training", operation = "delete")
    @Override
    public void deleteById(Long id) {
        Training training = entityManager.find(Training.class, id);
        if (training != null) {
            entityManager.remove(training);
        }
    }

    @Loggable
    @MeasureDb(table = "training", operation = "delete")
    @Override
    public void delete(Training training) {
        if (entityManager.contains(training)) {
            entityManager.remove(training);
        } else {
            Training merged = entityManager.merge(training);
            entityManager.remove(merged);
        }
    }

    @Loggable
    @MeasureDb(table = "training", operation = "select")
    @Override
    public List<Training> findByTraineeUsernameAndCriteria(
            String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {

        String query = "SELECT t FROM Training t " +
                "JOIN t.trainee tr " +
                "JOIN t.trainer tn " +
                "JOIN t.trainingType tt " +
                "WHERE tr.userName = :traineeUsername ";

        if (fromDate != null) query += "AND t.trainingDate >= :fromDate ";
        if (toDate != null)   query += "AND t.trainingDate <= :toDate ";
        if (trainerName != null) query += "AND tn.userName = :trainerName ";
        if (trainingType != null) query += "AND tt.type = :trainingType ";

        var typedQuery = entityManager.createQuery(query, Training.class)
                .setParameter("traineeUsername", traineeUsername);

        if (fromDate != null)    typedQuery.setParameter("fromDate",    fromDate);
        if (toDate   != null)    typedQuery.setParameter("toDate",      toDate);
        if (trainerName != null) typedQuery.setParameter("trainerName", trainerName);
        if (trainingType != null) {
            try {
                typedQuery.setParameter("trainingType", TrainingType.valueOf(trainingType));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid training type: " + trainingType);
            }
        }

        return typedQuery.getResultList();
    }

    @Loggable
    @MeasureDb(table = "training", operation = "select")
    @Override
    public List<Training> findByTrainerUsernameAndCriteria(
            String trainerUsername, Date fromDate, Date toDate, String traineeName) {

        String query = "SELECT t FROM Training t " +
                "JOIN t.trainer tn " +
                "JOIN t.trainee tr " +
                "WHERE tn.userName = :trainerUsername ";

        if (fromDate != null) query += "AND t.trainingDate >= :fromDate ";
        if (toDate   != null) query += "AND t.trainingDate <= :toDate ";
        if (traineeName != null) query += "AND tr.userName = :traineeName ";

        var typedQuery = entityManager.createQuery(query, Training.class)
                .setParameter("trainerUsername", trainerUsername);

        if (fromDate != null)   typedQuery.setParameter("fromDate",   fromDate);
        if (toDate   != null)   typedQuery.setParameter("toDate",     toDate);
        if (traineeName != null)typedQuery.setParameter("traineeName",traineeName);

        return typedQuery.getResultList();
    }

    @Loggable
    @MeasureDb(table = "training", operation = "select")
    @Override
    public Optional<Training> findByFields(Long trainerId,
                                           Long traineeId,
                                           String trainingName,
                                           Date trainingDate) {
        String jpql = """
            SELECT t FROM Training t
            WHERE t.trainer.trainerId = :trainerId
              AND t.trainee.traineeId = :traineeId
              AND t.trainingName = :trainingName
              AND t.trainingDate = :trainingDate
        """;
        List<Training> results = entityManager.createQuery(jpql, Training.class)
                .setParameter("trainerId",    trainerId)
                .setParameter("traineeId",    traineeId)
                .setParameter("trainingName", trainingName)
                .setParameter("trainingDate", trainingDate)
                .getResultList();

        return results.stream().findFirst();
    }
}