package com.vetcare360.model;

import java.time.LocalDate;

/**
 * Represents a visit in the VetCare360 system.
 */
public class Visit {
    private int id;
    private LocalDate date;
    private String description;
    private Pet pet;
    private Vet vet;

    /**
     * Default constructor
     */
    public Visit() {
    }

    /**
     * Constructor with all fields except id
     */
    public Visit(LocalDate date, String description, Pet pet, Vet vet) {
        this.date = date;
        this.description = description;
        this.pet = pet;
        this.vet = vet;
    }

    /**
     * Constructor with all fields
     */
    public Visit(int id, LocalDate date, String description, Pet pet, Vet vet) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.pet = pet;
        this.vet = vet;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", pet=" + (pet != null ? pet.getName() : "none") +
                ", vet=" + (vet != null ? vet.getFullName() : "none") +
                '}';
    }
}