package com.vetcare360.service;

import com.vetcare360.data.DataStore;
import com.vetcare360.model.Visit;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing visits.
 */
public class VisitService {
    private final DataStore dataStore;

    /**
     * Constructor
     */
    public VisitService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Get all visits
     * @return list of all visits
     */
    public List<Visit> getAllVisits() {
        return dataStore.getAllVisits();
    }

    /**
     * Get visit by ID
     * @param id the visit ID
     * @return the visit with the given ID, or null if not found
     */
    public Visit getVisitById(int id) {
        return dataStore.getVisitById(id);
    }

    /**
     * Save a visit (create or update)
     * @param visit the visit to save
     */
    public void saveVisit(Visit visit) {
        dataStore.saveVisit(visit);
    }

    /**
     * Delete a visit
     * @param id the ID of the visit to delete
     */
    public void deleteVisit(int id) {
        dataStore.deleteVisit(id);
    }

    /**
     * Get visits by pet ID
     * @param petId the pet ID
     * @return list of visits for the given pet
     */
    public List<Visit> getVisitsByPetId(int petId) {
        return dataStore.getAllVisits().stream()
                .filter(visit -> visit.getPet() != null && visit.getPet().getId() == petId)
                .toList();
    }

    /**
     * Get visits by vet ID
     * @param vetId the vet ID
     * @return list of visits for the given vet
     */
    public List<Visit> getVisitsByVetId(int vetId) {
        return dataStore.getAllVisits().stream()
                .filter(visit -> visit.getVet() != null && visit.getVet().getId() == vetId)
                .toList();
    }

    /**
     * Get visits by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of visits within the date range
     */
    public List<Visit> getVisitsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dataStore.getAllVisits().stream()
                .filter(visit -> visit.getDate() != null && 
                        !visit.getDate().isBefore(startDate) && 
                        !visit.getDate().isAfter(endDate))
                .toList();
    }

    /**
     * Validate visit data
     * @param visit the visit to validate
     * @return true if valid, false otherwise
     */
    public boolean validateVisit(Visit visit) {
        // Check for required fields
        if (visit.getDate() == null) {
            return false;
        }
        if (visit.getDescription() == null || visit.getDescription().trim().isEmpty()) {
            return false;
        }
        if (visit.getPet() == null) {
            return false;
        }
        if (visit.getVet() == null) {
            return false;
        }

        // Validate date (not in the future)
        return !visit.getDate().isAfter(LocalDate.now());
    }
}