package com.vetcare360.service;

import com.vetcare360.data.DataStore;
import com.vetcare360.model.Pet;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing pets.
 */
public class PetService {
    private final DataStore dataStore;

    /**
     * Constructor
     */
    public PetService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Get all pets
     * @return list of all pets
     */
    public List<Pet> getAllPets() {
        return dataStore.getAllPets();
    }

    /**
     * Get pet by ID
     * @param id the pet ID
     * @return the pet with the given ID, or null if not found
     */
    public Pet getPetById(int id) {
        return dataStore.getPetById(id);
    }

    /**
     * Save a pet (create or update)
     * @param pet the pet to save
     */
    public void savePet(Pet pet) {
        dataStore.savePet(pet);
    }

    /**
     * Delete a pet
     * @param id the ID of the pet to delete
     */
    public void deletePet(int id) {
        Pet pet = dataStore.getPetById(id);
        if (pet != null && pet.getOwner() != null) {
            pet.getOwner().getPets().removeIf(p -> p.getId() == id);
            dataStore.saveOwner(pet.getOwner());
        }
        dataStore.deletePet(id);
    }

    /**
     * Get pets by owner ID
     * @param ownerId the owner ID
     * @return list of pets belonging to the owner
     */
    public List<Pet> getPetsByOwnerId(int ownerId) {
        return dataStore.getAllPets().stream()
                .filter(pet -> pet.getOwner() != null && pet.getOwner().getId() == ownerId)
                .toList();
    }

    /**
     * Validate pet data
     * @param pet the pet to validate
     * @return true if valid, false otherwise
     */
    public boolean validatePet(Pet pet) {
        // Check for required fields
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            return false;
        }
        if (pet.getType() == null || pet.getType().trim().isEmpty()) {
            return false;
        }
        if (pet.getBirthDate() == null) {
            return false;
        }
        if (pet.getOwner() == null) {
            return false;
        }

        // Validate birth date (not in the future)
        return !pet.getBirthDate().isAfter(LocalDate.now());
    }
}