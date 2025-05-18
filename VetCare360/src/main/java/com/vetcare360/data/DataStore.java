package com.vetcare360.data;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.model.Vet;
import com.vetcare360.model.Visit;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * In-memory data store for the VetCare360 application.
 * This class manages all the data for the application.
 * In a real application, this would be replaced with a database.
 */
public class DataStore {
    private static DataStore instance;
    private static final String DATA_DIR = "data";
    private static final String OWNERS_FILE = DATA_DIR + "/owners.csv";
    private static final String PETS_FILE = DATA_DIR + "/pets.csv";
    private static final String VETS_FILE = DATA_DIR + "/vets.csv";
    private static final String VISITS_FILE = DATA_DIR + "/visits.csv";
    
    private final Map<Integer, Owner> owners;
    private final Map<Integer, Pet> pets;
    private final Map<Integer, Vet> vets;
    private final Map<Integer, Visit> visits;
    
    private int nextOwnerId = 1;
    private int nextPetId = 1;
    private int nextVetId = 1;
    private int nextVisitId = 1;
    
    /**
     * Private constructor to enforce singleton pattern
     */
    private DataStore() {
        owners = new HashMap<>();
        pets = new HashMap<>();
        vets = new HashMap<>();
        visits = new HashMap<>();
        loadAll();
        if (owners.isEmpty() && pets.isEmpty() && vets.isEmpty() && visits.isEmpty()) {
        initializeSampleData();
            saveAll();
        }
    }
    
    /**
     * Get the singleton instance of DataStore
     * @return the DataStore instance
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    private void loadAll() {
        new File(DATA_DIR).mkdirs();
        loadOwners();
        loadPets();
        loadVets();
        loadVisits();
        // Re-link relationships
        for (Pet pet : pets.values()) {
            Owner owner = owners.get(pet.getOwner() != null ? pet.getOwner().getId() : 0);
            if (owner != null) {
                pet.setOwner(owner);
                owner.addPet(pet);
            }
        }
        for (Visit visit : visits.values()) {
            Pet pet = pets.get(visit.getPet() != null ? visit.getPet().getId() : 0);
            Vet vet = vets.get(visit.getVet() != null ? visit.getVet().getId() : 0);
            if (pet != null) {
                visit.setPet(pet);
                pet.addVisit(visit);
            }
            if (vet != null) {
                visit.setVet(vet);
                vet.addVisit(visit);
            }
        }
    }
    private void saveAll() {
        saveOwners();
        savePets();
        saveVets();
        saveVisits();
    }
    // CSV helpers
    private String escape(String s) { return s == null ? "" : s.replace("\"", "\"\""); }
    private String unescape(String s) { return s == null ? "" : s.replace("\"\"", "\""); }
    // Owners
    private void loadOwners() {
        owners.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(OWNERS_FILE))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] f = line.split(",", -1);
                if (f.length < 6) continue;
                try {
                    int id = Integer.parseInt(f[0]);
                    Owner o = new Owner(id, unescape(f[1]), unescape(f[2]), unescape(f[3]), unescape(f[4]), unescape(f[5]));
                    owners.put(id, o);
                    nextOwnerId = Math.max(nextOwnerId, id + 1);
                } catch (NumberFormatException e) {
                    System.err.println("[WARNING] Skipping invalid owner entry at line " + lineNum + ": " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException ignored) {}
    }
    private void saveOwners() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(OWNERS_FILE))) {
            for (Owner o : owners.values()) {
                pw.println(o.getId() + "," + escape(o.getFirstName()) + "," + escape(o.getLastName()) + "," + escape(o.getAddress()) + "," + escape(o.getCity()) + "," + escape(o.getTelephone()));
            }
        } catch (IOException ignored) {}
    }
    // Pets
    private void loadPets() {
        pets.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(PETS_FILE))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] f = line.split(",", -1);
                if (f.length < 6) continue;
                try {
                    int id = Integer.parseInt(f[0]);
                    String name = unescape(f[1]);
                    LocalDate birthDate = f[2].isEmpty() ? null : LocalDate.parse(f[2]);
                    String type = unescape(f[3]);
                    // ownerId is not used here; owner will be linked after all loaded
                    Pet p = new Pet(id, name, birthDate, type, null);
                    pets.put(id, p);
                    nextPetId = Math.max(nextPetId, id + 1);
                    // owner will be linked after all loaded
                } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                    System.err.println("[WARNING] Skipping invalid pet entry at line " + lineNum + ": " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException ignored) {}
    }
    private void savePets() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PETS_FILE))) {
            for (Pet p : pets.values()) {
                pw.println(p.getId() + "," + escape(p.getName()) + "," + (p.getBirthDate() != null ? p.getBirthDate() : "") + "," + escape(p.getType()) + "," + (p.getOwner() != null ? p.getOwner().getId() : ""));
            }
        } catch (IOException ignored) {}
    }
    // Vets
    private void loadVets() {
        vets.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(VETS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", -1);
                if (f.length < 4) continue;
                int id = Integer.parseInt(f[0]);
                Vet v = new Vet(id, unescape(f[1]), unescape(f[2]), unescape(f[3]));
                vets.put(id, v);
                nextVetId = Math.max(nextVetId, id + 1);
            }
        } catch (IOException ignored) {}
    }
    private void saveVets() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VETS_FILE))) {
            for (Vet v : vets.values()) {
                pw.println(v.getId() + "," + escape(v.getFirstName()) + "," + escape(v.getLastName()) + "," + escape(v.getSpecialization()));
            }
        } catch (IOException ignored) {}
    }
    // Visits
    private void loadVisits() {
        visits.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(VISITS_FILE))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                String[] f = line.split(",", -1);
                if (f.length < 6) continue;
                try {
                    int id = Integer.parseInt(f[0]);
                    LocalDate date = f[1].isEmpty() ? null : LocalDate.parse(f[1]);
                    String description = unescape(f[2]);
                    // int petId = f[3].isEmpty() ? 0 : Integer.parseInt(f[3]);
                    // int vetId = f[4].isEmpty() ? 0 : Integer.parseInt(f[4]);
                    Visit v = new Visit(id, date, description, null, null);
                    visits.put(id, v);
                    nextVisitId = Math.max(nextVisitId, id + 1);
                    // pet/vet will be linked after all loaded
                } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                    System.err.println("[WARNING] Skipping invalid visit entry at line " + lineNum + ": " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException ignored) {}
    }
    private void saveVisits() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VISITS_FILE))) {
            for (Visit v : visits.values()) {
                pw.println(v.getId() + "," + (v.getDate() != null ? v.getDate() : "") + "," + escape(v.getDescription()) + "," + (v.getPet() != null ? v.getPet().getId() : "") + "," + (v.getVet() != null ? v.getVet().getId() : ""));
            }
        } catch (IOException ignored) {}
    }
    // CRUD methods (save/delete) call saveAll after each change
    public List<Owner> getAllOwners() { return new ArrayList<>(owners.values()); }
    public Owner getOwnerById(int id) { return owners.get(id); }
    public List<Owner> findOwnersByLastName(String lastName) {
        List<Owner> result = new ArrayList<>();
        for (Owner o : owners.values()) {
            if (o.getLastName().toLowerCase().contains(lastName.toLowerCase())) result.add(o);
        }
        return result;
    }
    public void saveOwner(Owner owner) {
        if (owner.getId() == 0) owner.setId(nextOwnerId++);
        owners.put(owner.getId(), owner);
        saveAll();
    }
    public void deleteOwner(int id) { owners.remove(id); saveAll(); }
    public List<Pet> getAllPets() { return new ArrayList<>(pets.values()); }
    public Pet getPetById(int id) { return pets.get(id); }
    public void savePet(Pet pet) {
        if (pet.getId() == 0) pet.setId(nextPetId++);
        pets.put(pet.getId(), pet);
        saveAll();
    }
    public void deletePet(int id) { pets.remove(id); saveAll(); }
    public List<Vet> getAllVets() { return new ArrayList<>(vets.values()); }
    public Vet getVetById(int id) { return vets.get(id); }
    public void saveVet(Vet vet) {
        if (vet.getId() == 0) vet.setId(nextVetId++);
        vets.put(vet.getId(), vet);
        saveAll();
    }
    public void deleteVet(int id) { vets.remove(id); saveAll(); }
    public List<Visit> getAllVisits() { return new ArrayList<>(visits.values()); }
    public Visit getVisitById(int id) { return visits.get(id); }
    public void saveVisit(Visit visit) {
        if (visit.getId() == 0) visit.setId(nextVisitId++);
        visits.put(visit.getId(), visit);
        saveAll();
    }
    public void deleteVisit(int id) { visits.remove(id); saveAll(); }
    
    /**
     * Initialize the data store with sample data
     */
    private void initializeSampleData() {
        // Create sample owners
        Owner owner1 = new Owner(nextOwnerId++, "John", "Doe", "123 Main St", "New York", "555-1234");
        Owner owner2 = new Owner(nextOwnerId++, "Jane", "Smith", "456 Oak Ave", "Los Angeles", "555-5678");
        Owner owner3 = new Owner(nextOwnerId++, "Robert", "Johnson", "789 Pine Rd", "Chicago", "555-9012");
        
        owners.put(owner1.getId(), owner1);
        owners.put(owner2.getId(), owner2);
        owners.put(owner3.getId(), owner3);
        
        // Create sample vets
        Vet vet1 = new Vet(nextVetId++, "Alice", "Williams", "Surgery");
        Vet vet2 = new Vet(nextVetId++, "Bob", "Brown", "Dentistry");
        Vet vet3 = new Vet(nextVetId++, "Carol", "Davis", "Cardiology");
        
        vets.put(vet1.getId(), vet1);
        vets.put(vet2.getId(), vet2);
        vets.put(vet3.getId(), vet3);
        
        // Create sample pets
        Pet pet1 = new Pet(nextPetId++, "Max", LocalDate.of(2018, 5, 15), "Dog", owner1);
        Pet pet2 = new Pet(nextPetId++, "Bella", LocalDate.of(2019, 3, 10), "Cat", owner1);
        Pet pet3 = new Pet(nextPetId++, "Charlie", LocalDate.of(2020, 7, 22), "Bird", owner2);
        Pet pet4 = new Pet(nextPetId++, "Luna", LocalDate.of(2017, 11, 5), "Dog", owner3);
        
        pets.put(pet1.getId(), pet1);
        pets.put(pet2.getId(), pet2);
        pets.put(pet3.getId(), pet3);
        pets.put(pet4.getId(), pet4);
        
        owner1.addPet(pet1);
        owner1.addPet(pet2);
        owner2.addPet(pet3);
        owner3.addPet(pet4);
        
        // Create sample visits
        Visit visit1 = new Visit(nextVisitId++, LocalDate.of(2023, 1, 15), "Annual checkup", pet1, vet1);
        Visit visit2 = new Visit(nextVisitId++, LocalDate.of(2023, 2, 20), "Vaccination", pet2, vet2);
        Visit visit3 = new Visit(nextVisitId++, LocalDate.of(2023, 3, 10), "Wing clipping", pet3, vet3);
        Visit visit4 = new Visit(nextVisitId++, LocalDate.of(2023, 4, 5), "Dental cleaning", pet4, vet2);
        
        visits.put(visit1.getId(), visit1);
        visits.put(visit2.getId(), visit2);
        visits.put(visit3.getId(), visit3);
        visits.put(visit4.getId(), visit4);
        
        pet1.addVisit(visit1);
        pet2.addVisit(visit2);
        pet3.addVisit(visit3);
        pet4.addVisit(visit4);
        
        vet1.addVisit(visit1);
        vet2.addVisit(visit2);
        vet3.addVisit(visit3);
        vet2.addVisit(visit4);
    }
}