package com.vetcare360.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a veterinarian in the VetCare360 system.
 */
public class Vet {
    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<Visit> visits;

    /**
     * Default constructor
     */
    public Vet() {
        this.visits = new ArrayList<>();
    }

    /**
     * Constructor with all fields except id and visits
     */
    public Vet(String firstName, String lastName, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.visits = new ArrayList<>();
    }

    /**
     * Constructor with all fields except visits
     */
    public Vet(int id, String firstName, String lastName, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.visits = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    /**
     * Add a visit to this vet
     * @param visit the visit to add
     */
    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setVet(this);
    }

    /**
     * Get the full name of the vet
     * @return the full name (first name + last name)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Vet{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", visits=" + visits.size() +
                '}';
    }
}