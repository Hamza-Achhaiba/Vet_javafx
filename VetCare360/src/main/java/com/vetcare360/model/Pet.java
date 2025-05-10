package com.vetcare360.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pet in the VetCare360 system.
 */
public class Pet {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String type;
    private Owner owner;
    private List<Visit> visits;

    /**
     * Default constructor
     */
    public Pet() {
        this.visits = new ArrayList<>();
    }

    /**
     * Constructor with all fields except id and visits
     */
    public Pet(String name, LocalDate birthDate, String type, Owner owner) {
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.owner = owner;
        this.visits = new ArrayList<>();
    }

    /**
     * Constructor with all fields except visits
     */
    public Pet(int id, String name, LocalDate birthDate, String type, Owner owner) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.owner = owner;
        this.visits = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    /**
     * Add a visit to this pet
     * @param visit the visit to add
     */
    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setPet(this);
    }

    /**
     * Calculate the age of the pet in years
     * @return the age in years
     */
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", type='" + type + '\'' +
                ", owner=" + (owner != null ? owner.getFullName() : "none") +
                ", visits=" + visits.size() +
                '}';
    }
}