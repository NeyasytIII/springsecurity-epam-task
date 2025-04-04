package com.epamtask.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
@MappedSuperclass
public abstract class User {

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    @JsonProperty("active")
    private boolean isActive;


    public User(String firstName, String lastName, boolean isActive) {
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
