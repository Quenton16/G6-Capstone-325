package com.ramify.manager;
import com.ramify.service.FirebaseConnection;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.*;

/**
 * Manages Lounge Tracker information in Firebase
 */
public class LoungeManager {
    private Firestore db;
    
    public LoungeManager() {
        this.db = FirebaseConnection.getFirestore();
    }
    
    // Add a new lounge
    public String addLounge(String loungeName, String building, int floor, 
                           int maxCapacity, String facilities) {
        try {
            Map<String, Object> loungeData = new HashMap<>();
            loungeData.put("loungeName", loungeName);
            loungeData.put("building", building);
            loungeData.put("floor", floor);
            loungeData.put("maxCapacity", maxCapacity);
            loungeData.put("currentOccupancy", 0);
            loungeData.put("status", "AVAILABLE"); // AVAILABLE, MODERATE, BUSY, FULL, CLOSED
            loungeData.put("facilities", facilities);
            loungeData.put("lastUpdated", Timestamp.now());
            
            DocumentReference docRef = db.collection("lounges").document();
            docRef.set(loungeData).get();
            
            System.out.println("Lounge added: " + loungeName);
            return docRef.getId();
        } catch (Exception e) {
            System.err.println("Error adding lounge: " + e.getMessage());
            return null;
        }
    }
    
    // Get lounge by ID
    public Map<String, Object> getLounge(String loungeId) {
        try {
            var doc = db.collection("lounges").document(loungeId).get().get();
            if (doc.exists()) {
                return doc.getData();
            }
        } catch (Exception e) {
            System.err.println("Error getting lounge: " + e.getMessage());
        }
        return null;
    }
    
    // Update lounge occupancy
    public boolean updateOccupancy(String loungeId, int occupancy) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("currentOccupancy", occupancy);
            updates.put("lastUpdated", Timestamp.now());
            
            db.collection("lounges").document(loungeId).update(updates).get();
            System.out.println("Lounge occupancy updated: " + loungeId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating occupancy: " + e.getMessage());
            return false;
        }
    }
    
    // Update lounge
    public boolean updateLounge(String loungeId, Map<String, Object> updates) {
        try {
            updates.put("lastUpdated", Timestamp.now());
            db.collection("lounges").document(loungeId).update(updates).get();
            System.out.println("Lounge updated: " + loungeId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating lounge: " + e.getMessage());
            return false;
        }
    }
    
    // Delete lounge
    public boolean deleteLounge(String loungeId) {
        try {
            db.collection("lounges").document(loungeId).delete().get();
            System.out.println("Lounge deleted: " + loungeId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting lounge: " + e.getMessage());
            return false;
        }
    }
    
    // Simplified addLounge method for JavaFX GUI
    public String addLounge(String name, String location, int capacity, int currentOccupancy) {
        String building = location.split(",")[0].trim(); // Extract building from location
        int floor = 1; // default floor
        String facilities = "WiFi, Seating, Study Tables"; // default facilities
        
        return addLounge(name, building, floor, capacity, facilities);
    }
    
    // Update lounge method for JavaFX GUI
    public boolean updateLounge(String loungeId, String name, String location, int capacity, int currentOccupancy) {
        try {
            String building = location.split(",")[0].trim();
            int floor = 1;
            
            Map<String, Object> updates = new HashMap<>();
            updates.put("loungeName", name);
            updates.put("building", building);
            updates.put("floor", floor);
            updates.put("maxCapacity", capacity);
            updates.put("currentOccupancy", currentOccupancy);
            updates.put("lastUpdated", Timestamp.now());
            
            return updateLounge(loungeId, updates);
        } catch (Exception e) {
            System.err.println("Error updating lounge: " + e.getMessage());
            return false;
        }
    }
    
    // List all lounges (JavaFX GUI compatible format)
    public Map<String, Map<String, Object>> getAllLounges() {
        try {
            var docs = db.collection("lounges").get().get().getDocuments();
            Map<String, Map<String, Object>> lounges = new HashMap<>();
            
            for (var doc : docs) {
                Map<String, Object> lounge = new HashMap<>(doc.getData());
                // Rename loungeName to name for GUI compatibility
                lounge.put("name", lounge.get("loungeName"));
                // Combine building and floor into location
                String location = lounge.get("building") + ", Floor " + lounge.get("floor");
                lounge.put("location", location);
                // Rename maxCapacity to capacity for GUI compatibility
                lounge.put("capacity", lounge.get("maxCapacity"));
                lounges.put(doc.getId(), lounge);
            }
            return lounges;
        } catch (Exception e) {
            System.err.println("Error listing lounges: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // Original method (keeping for backwards compatibility)
    public List<Map<String, Object>> getAllLoungesList() {
        try {
            var docs = db.collection("lounges").get().get().getDocuments();
            List<Map<String, Object>> lounges = new ArrayList<>();
            
            for (var doc : docs) {
                Map<String, Object> lounge = new HashMap<>(doc.getData());
                lounge.put("id", doc.getId());
                lounges.add(lounge);
            }
            return lounges;
        } catch (Exception e) {
            System.err.println("Error listing lounges: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
