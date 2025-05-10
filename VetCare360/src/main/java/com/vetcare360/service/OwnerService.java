package com.vetcare360.service;

import com.vetcare360.data.DataStore;
import com.vetcare360.model.Owner;

import java.util.List;

/**
 * Service class for managing owners.
 */
public class OwnerService {
    private final DataStore dataStore;

    /**
     * Constructor
     */
    public OwnerService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Get all owners
     * @return list of all owners
     */
    public List<Owner> getAllOwners() {
        return dataStore.getAllOwners();
    }

    /**
     * Get owner by ID
     * @param id the owner ID
     * @return the owner with the given ID, or null if not found
     */
    public Owner getOwnerById(int id) {
        return dataStore.getOwnerById(id);
    }

    /**
     * Find owners by last name
     * @param lastName the last name to search for
     * @return list of owners with matching last name
     */
    public List<Owner> findOwnersByLastName(String lastName) {
        return dataStore.findOwnersByLastName(lastName);
    }

    /**
     * Save an owner (create or update)
     * @param owner the owner to save
     */
    public void saveOwner(Owner owner) {
        dataStore.saveOwner(owner);
    }

    /**
     * Delete an owner
     * @param id the ID of the owner to delete
     */
    public void deleteOwner(int id) {
        dataStore.deleteOwner(id);
    }

    /**
     * Validate owner data
     * @param owner the owner to validate
     * @return true if valid, false otherwise
     */
    public boolean validateOwner(Owner owner) {
        // Check for required fields
        if (owner.getFirstName() == null || owner.getFirstName().trim().isEmpty()) {
            return false;
        }
        if (owner.getLastName() == null || owner.getLastName().trim().isEmpty()) {
            return false;
        }
        if (owner.getAddress() == null || owner.getAddress().trim().isEmpty()) {
            return false;
        }
        if (owner.getCity() == null || owner.getCity().trim().isEmpty()) {
            return false;
        }
        if (owner.getTelephone() == null || owner.getTelephone().trim().isEmpty()) {
            return false;
        }

        // Validate telephone format (simple check for digits only)
        return owner.getTelephone().replaceAll("[^0-9-]", "").length() >= 7;
    }
}