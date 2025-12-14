package com.ramify.manager;
import com.ramify.service.FirebaseConnection;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.*;

/**
 * Manages Event information in Firebase
 */
public class EventManager {
    private final Firestore db;
    
    public EventManager() {
        this.db = FirebaseConnection.getFirestore();
    }
    
    // Add a new event with category
    public String addEvent(String eventName, String description, String clubId, 
                          Date startDateTime, Date endDateTime, String location, 
                          int maxCapacity, String category) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("name", eventName);
            eventData.put("description", description);
            eventData.put("clubId", clubId);
            eventData.put("startDate", Timestamp.of(startDateTime));
            eventData.put("endDate", Timestamp.of(endDateTime));
            eventData.put("location", location);
            eventData.put("maxAttendees", maxCapacity);
            eventData.put("category", category);
            eventData.put("currentAttendees", 0);
            eventData.put("status", "UPCOMING");
            eventData.put("createdAt", Timestamp.now());
            
            DocumentReference docRef = db.collection("events").document();
            docRef.set(eventData).get();
            
            System.out.println("Event added: " + eventName);
            return docRef.getId();
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
            return null;
        }
    }
    
    // Overloaded method for backwards compatibility
    public String addEvent(String eventName, String description, String clubId, 
                          Date startDateTime, Date endDateTime, String location, 
                          int maxCapacity) {
        return addEvent(eventName, description, clubId, startDateTime, endDateTime, location, maxCapacity, "General");
    }
    
    // Get event by ID
    public Map<String, Object> getEvent(String eventId) {
        try {
            var doc = db.collection("events").document(eventId).get().get();
            if (doc.exists()) {
                return doc.getData();
            }
        } catch (Exception e) {
            System.err.println("Error getting event: " + e.getMessage());
        }
        return null;
    }
    
    // Update event
    public boolean updateEvent(String eventId, Map<String, Object> updates) {
        try {
            db.collection("events").document(eventId).update(updates).get();
            System.out.println("Event updated: " + eventId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating event: " + e.getMessage());
            return false;
        }
    }
    
    // Delete event
    public boolean deleteEvent(String eventId) {
        try {
            db.collection("events").document(eventId).delete().get();
            System.out.println("Event deleted: " + eventId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting event: " + e.getMessage());
            return false;
        }
    }
    
    // Simplified addEvent method for JavaFX GUI
    public String addEvent(String name, String description, String location, 
                          java.time.LocalDateTime dateTime, String organizerId) {
        // Convert LocalDateTime to Date
        java.util.Date startDate = java.sql.Timestamp.valueOf(dateTime);
        java.util.Date endDate = new java.util.Date(startDate.getTime() + 3600000); // +1 hour default
        
        return addEvent(name, description, organizerId, startDate, endDate, location, 100);
    }
    
    // Update event method for JavaFX GUI
    public boolean updateEvent(String eventId, String name, String description, 
                              String location, java.time.LocalDateTime dateTime, String organizerId) {
        try {
            java.util.Date startDate = java.sql.Timestamp.valueOf(dateTime);
            java.util.Date endDate = new java.util.Date(startDate.getTime() + 3600000);
            
            Map<String, Object> updates = new HashMap<>();
            updates.put("eventName", name);
            updates.put("description", description);
            updates.put("location", location);
            updates.put("startDateTime", Timestamp.of(startDate));
            updates.put("endDateTime", Timestamp.of(endDate));
            updates.put("organizerId", organizerId);
            
            return updateEvent(eventId, updates);
        } catch (Exception e) {
            System.err.println("Error updating event: " + e.getMessage());
            return false;
        }
    }
    
    // List all events (JavaFX GUI compatible format)
    public Map<String, Map<String, Object>> getAllEvents() {
        try {
            var docs = db.collection("events").get().get().getDocuments();
            Map<String, Map<String, Object>> events = new HashMap<>();
            
            for (var doc : docs) {
                Map<String, Object> event = new HashMap<>(doc.getData());
                // Rename eventName to name for GUI compatibility
                event.put("name", event.get("eventName"));
                // Convert Timestamp back to readable format
                if (event.get("startDateTime") != null) {
                    event.put("dateTime", event.get("startDateTime").toString());
                }
                events.put(doc.getId(), event);
            }
            return events;
        } catch (Exception e) {
            System.err.println("Error listing events: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // Original method (keeping for backwards compatibility)
    public List<Map<String, Object>> getAllEventsList() {
        try {
            var docs = db.collection("events").get().get().getDocuments();
            List<Map<String, Object>> events = new ArrayList<>();
            
            for (var doc : docs) {
                Map<String, Object> event = new HashMap<>(doc.getData());
                event.put("id", doc.getId());
                events.add(event);
            }
            return events;
        } catch (Exception e) {
            System.err.println("Error listing events: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
