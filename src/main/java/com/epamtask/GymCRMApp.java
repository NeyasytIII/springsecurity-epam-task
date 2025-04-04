package com.epamtask;

import com.epamtask.facade.CoordinatorFacade;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.security.AuthContextHolder;
import com.epamtask.service.AuthenticationService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;

public class GymCRMApp {

    public void start() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.epamtask");
        CoordinatorFacade facade = context.getBean(CoordinatorFacade.class);
        AuthenticationService authService = context.getBean(AuthenticationService.class);
        System.out.println("\n==== FULL Hibernate FUNCTIONALITY TEST ====\n");
        try {
            initializeProfiles(facade, authService);
            testPasswordChanges(facade);
            testProfileCreationAndRetrieval(facade);
            testProfileUpdates(facade);
            testActivationDeactivation(facade);
            testTraineeDeletion(facade);
            testCascadeDeletion(facade);
            testTrainingFiltering(facade);
            testAddTraining(facade);
            testGetUnassignedTrainers(facade);
            testUpdateTraineeTrainers(facade);
            printAllUsers(facade);
        } catch (Exception e) {
            System.out.println("\uD83D\uDCA5 Operations error: " + e.getMessage());
        } finally {
            context.close();
        }
        System.out.println("\n====  ALL TESTS COMPLETED ====");
    }

    private void initializeProfiles(CoordinatorFacade facade, AuthenticationService authService) {
        createTrainerIfNotExists(facade, authService, "Michael", "Scott", "origin123", "STRENGTH");
        createTrainerIfNotExists(facade, authService, "Dwight", "Schrute", "dwightpass", "CROSSFIT");
        createTraineeIfNotExists(facade, authService, "Pam", "Beesly", "start321", "123 Main St");
        createTraineeIfNotExists(facade, authService, "Jim", "Halpert", "jimpass", "456 Elm St");
    }

    private void createTrainerIfNotExists(CoordinatorFacade facade, AuthenticationService authService, String first, String last, String password, String spec) {
        String username = first + "." + last;
        if (!authService.checkCredentialsWithoutAuth(username, password)) {
            facade.getTrainerFacade().createTrainer(first, last, spec);
            facade.getTrainerFacade().setInitialPassword(username, password);
            System.out.println(" Created Trainer: " + username);
        }
    }

    private void createTraineeIfNotExists(CoordinatorFacade facade, AuthenticationService authService, String first, String last, String password, String address) {
        String username = first + "." + last;
        if (!authService.checkCredentialsWithoutAuth(username, password)) {
            facade.getTraineeFacade().createTrainee(first, last, address, new Date());
            facade.getTraineeFacade().setInitialPassword(username, password);
            System.out.println(" Created Trainee: " + username);
        }
    }

    private void testPasswordChanges(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Michael.Scott", "origin123");
        facade.getTrainerFacade().updatePassword("Michael.Scott", "secure456");
        AuthContextHolder.clear();

        AuthContextHolder.setCredentials("Michael.Scott", "secure456");
        Trainer updatedTrainer = facade.getTrainerFacade().getTrainerByUsername("Michael.Scott").orElseThrow();
        System.out.println(" Trainer password in DB: " + updatedTrainer.getPassword());
        AuthContextHolder.clear();

        AuthContextHolder.setCredentials("Pam.Beesly", "start321");
        facade.getTraineeFacade().updatePassword("Pam.Beesly", "newpass789");
        AuthContextHolder.clear();

        AuthContextHolder.setCredentials("Pam.Beesly", "newpass789");
        Trainee updatedTrainee = facade.getTraineeFacade().getTraineeByUsername("Pam.Beesly").orElseThrow();
        System.out.println(" Trainee password in DB: " + updatedTrainee.getPassword());
        AuthContextHolder.clear();
    }

    private void testProfileCreationAndRetrieval(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Michael.Scott", "secure456");
        Trainer trainer = facade.getTrainerFacade().getTrainerByUsername("Michael.Scott").orElseThrow();
        AuthContextHolder.clear();
        System.out.println("Trainer: " + trainer);

        AuthContextHolder.setCredentials("Pam.Beesly", "newpass789");
        Trainee trainee = facade.getTraineeFacade().getTraineeByUsername("Pam.Beesly").orElseThrow();
        AuthContextHolder.clear();
        System.out.println("Trainee: " + trainee);
    }

    private void testProfileUpdates(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Michael.Scott", "secure456");
        Trainer t = facade.getTrainerFacade().getTrainerByUsername("Michael.Scott").orElseThrow();
        t.setLastName("Scott-Updated");
        facade.getTrainerFacade().updateTrainer(t);
        AuthContextHolder.clear();

        AuthContextHolder.setCredentials("Pam.Beesly", "newpass789");
        Trainee tr = facade.getTraineeFacade().getTraineeByUsername("Pam.Beesly").orElseThrow();
        tr.setLastName("Beesly-Updated");
        facade.getTraineeFacade().updateTrainee(tr);
        AuthContextHolder.clear();
    }

    private void testActivationDeactivation(CoordinatorFacade facade) {
        AuthContextHolder.setCredentials("Jim.Halpert", "jimpass");
        facade.getTrainerFacade().deactivateUser("Michael.Scott");
        facade.getTraineeFacade().deactivateTrainee("Pam.Beesly");
        AuthContextHolder.clear();

        System.out.println(" Michael.Scott and Pam.Beesly have been deactivated.");
    }

    private void testTraineeDeletion(CoordinatorFacade facade) throws Exception {
        facade.getTraineeFacade().createTrainee("Temp", "Trainee", "Temp St", new Date());
        facade.getTraineeFacade().setInitialPassword("Temp.Trainee", "temppass");
        AuthContextHolder.setCredentials("Temp.Trainee", "temppass");
        Trainee temp = facade.getTraineeFacade().getTraineeByUsername("Temp.Trainee").orElseThrow();
        facade.getTraineeFacade().deleteTrainee("Temp.Trainee");
        AuthContextHolder.clear();
    }

    private void testCascadeDeletion(CoordinatorFacade facade) throws Exception {
        facade.getTraineeFacade().createTrainee("Cascade", "Trainee", "Cascade St", new Date());
        facade.getTraineeFacade().setInitialPassword("Cascade.Trainee", "cascadepass");

        AuthContextHolder.setCredentials("Cascade.Trainee", "cascadepass");
        Trainee cascade = facade.getTraineeFacade().getTraineeByUsername("Cascade.Trainee").orElseThrow();

        Trainer trainer = facade.getTrainerFacade().getTrainerByUsername("Michael.Scott").orElseThrow();
        facade.getTrainingFacade().createTraining(
                1001L,
                trainer.getTrainerId(),
                cascade.getTraineeId(),
                "Yoga Session",
                TrainingType.YOGA,
                new Date(),
                "60"
        );

        facade.getTraineeFacade().deleteTrainee("Cascade.Trainee");
        AuthContextHolder.clear();
    }

    private void testTrainingFiltering(CoordinatorFacade facade) {
        AuthContextHolder.setCredentials("Jim.Halpert", "jimpass");
        List<Training> filtered = facade.getTrainingFacade().getTrainingsByTraineeUsernameAndCriteria(
                "Jim.Halpert", null, null, "Dwight.Schrute", "YOGA");
        AuthContextHolder.clear();
        System.out.println("Filtered trainings count: " + filtered.size());
    }

    private void testAddTraining(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Jim.Halpert", "jimpass");
        Trainer dwight = facade.getTrainerFacade().getTrainerByUsername("Dwight.Schrute").orElseThrow();
        Trainee jim = facade.getTraineeFacade().getTraineeByUsername("Jim.Halpert").orElseThrow();
        facade.getTrainingFacade().createTraining(1L, dwight.getTrainerId(), jim.getTraineeId(), "Evening Stretch", TrainingType.YOGA, new Date(), "60");
        AuthContextHolder.clear();
    }

    private void testGetUnassignedTrainers(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Jim.Halpert", "jimpass");
        List<Trainer> unassigned = facade.getTrainerFacade().getTrainersNotAssignedToTrainee("Jim.Halpert");
        AuthContextHolder.clear();
        for (Trainer t : unassigned) {
            System.out.println("Unassigned Trainer: " + t);
        }
        System.out.println("Number of trainers not assigned to Jim.Halpert: " + unassigned.size());
    }

    private void testUpdateTraineeTrainers(CoordinatorFacade facade) throws Exception {
        AuthContextHolder.setCredentials("Jim.Halpert", "jimpass");
        facade.getTraineeFacade().assignTrainersToTrainee("Jim.Halpert", List.of("Dwight.Schrute"));
        Trainee updated = facade.getTraineeFacade().getTraineeByUsername("Jim.Halpert").orElseThrow();
        AuthContextHolder.clear();
        System.out.println("Trainee's trainers list updated: " + updated.getTrainers());
    }

    private void printAllUsers(CoordinatorFacade facade) {
        AuthContextHolder.setCredentials("Michael.Scott", "secure456");
        System.out.println("\n--- Current Trainers ---");
        for (Trainer t : facade.getTrainerFacade().getAllTrainers()) {
            System.out.println(t);
        }
        AuthContextHolder.clear();

        AuthContextHolder.setCredentials("Pam.Beesly", "newpass789");
        System.out.println("\n--- Current Trainees ---");
        for (Trainee t : facade.getTraineeFacade().getAllTrainees()) {
            System.out.println(t);
        }
        AuthContextHolder.clear();
    }
}