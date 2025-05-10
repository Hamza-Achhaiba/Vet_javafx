package com.vetcare360.service;

import com.vetcare360.data.DataStore;
import com.vetcare360.model.Vet;

import java.util.List;

/**
 * Service class for managing veterinarians.
 */
public class VetService {
    private final DataStore dataStore;

    /**
     * Constructor
     */
    public VetService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Get all vets
     * @return list of all vets
     */
    public List<Vet> getAllVets() {
        return dataStore.getAllVets();
    }

    /**
     * Get vet by ID
     * @param id the vet ID
     * @return the vet with the given ID, or null if not found
     */
    public Vet getVetById(int id) {
        return dataStore.getVetById(id);
    }

    /**
     * Save a vet (create or update)
     * @param vet the vet to save
     */
    public void saveVet(Vet vet) {
        dataStore.saveVet(vet);
    }

    /**
     * Delete a vet
     * @param id the ID of the vet to delete
     */
    public void deleteVet(int id) {
        dataStore.deleteVet(id);
    }

    /**
     * Get vets by specialization
     * @param specialization the specialization to filter by
     * @return list of vets with the given specialization
     */
    public List<Vet> getVetsBySpecialization(String specialization) {
        return dataStore.getAllVets().stream()
                .filter(vet -> vet.getSpecialization().equalsIgnoreCase(specialization))
                .toList();
    }

    /**
     * Validate vet data
     * @param vet the vet to validate
     * @return true if valid, false otherwise
     */
    public boolean validateVet(Vet vet) {
        // Check for required fields
        if (vet.getFirstName() == null || vet.getFirstName().trim().isEmpty()) {
            return false;
        }
        if (vet.getLastName() == null || vet.getLastName().trim().isEmpty()) {
            return false;
        }
        
        // Specialization is optional, but if provided, it shouldn't be empty
        return vet.getSpecialization() == null || !vet.getSpecialization().trim().isEmpty();
    }
}