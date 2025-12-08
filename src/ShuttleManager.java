import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.*;

/**
 * Manages Shuttle information in Firebase
 */
public class ShuttleManager {
    private Firestore db;
    
    public ShuttleManager() {
        this.db = FirebaseConnection.getFirestore();
    }
    
    // Add a new shuttle
    public String addShuttle(String routeName, String vehicleNumber, String driverName, 
                            int capacity, String currentLocation) {
        try {
            Map<String, Object> shuttleData = new HashMap<>();
            shuttleData.put("routeName", routeName);
            shuttleData.put("vehicleNumber", vehicleNumber);
            shuttleData.put("driverName", driverName);
            shuttleData.put("capacity", capacity);
            shuttleData.put("currentPassengers", 0);
            shuttleData.put("status", "AVAILABLE"); // AVAILABLE, IN_TRANSIT, MAINTENANCE, OUT_OF_SERVICE
            shuttleData.put("currentLocation", currentLocation);
            
            DocumentReference docRef = db.collection("shuttles").document();
            docRef.set(shuttleData).get();
            
            System.out.println("Shuttle added: " + vehicleNumber);
            return docRef.getId();
        } catch (Exception e) {
            System.err.println("Error adding shuttle: " + e.getMessage());
            return null;
        }
    }
    
    // Get shuttle by ID
    public Map<String, Object> getShuttle(String shuttleId) {
        try {
            var doc = db.collection("shuttles").document(shuttleId).get().get();
            if (doc.exists()) {
                return doc.getData();
            }
        } catch (Exception e) {
            System.err.println("Error getting shuttle: " + e.getMessage());
        }
        return null;
    }
    
    // Update shuttle
    public boolean updateShuttle(String shuttleId, Map<String, Object> updates) {
        try {
            db.collection("shuttles").document(shuttleId).update(updates).get();
            System.out.println("Shuttle updated: " + shuttleId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating shuttle: " + e.getMessage());
            return false;
        }
    }
    
    // Delete shuttle
    public boolean deleteShuttle(String shuttleId) {
        try {
            db.collection("shuttles").document(shuttleId).delete().get();
            System.out.println("Shuttle deleted: " + shuttleId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting shuttle: " + e.getMessage());
            return false;
        }
    }
    
    // Simplified addShuttle method for JavaFX GUI
    public String addShuttle(String routeName, String currentLocation, String status) {
        String vehicleNumber = "SHUTTLE-" + System.currentTimeMillis() % 1000;
        String driverName = "Unassigned";
        int capacity = 40; // default capacity
        
        return addShuttle(routeName, vehicleNumber, driverName, capacity, currentLocation);
    }
    
    // Update shuttle method for JavaFX GUI
    public boolean updateShuttle(String shuttleId, String routeName, String currentLocation, String status) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("routeName", routeName);
            updates.put("currentLocation", currentLocation);
            updates.put("status", status);
            
            return updateShuttle(shuttleId, updates);
        } catch (Exception e) {
            System.err.println("Error updating shuttle: " + e.getMessage());
            return false;
        }
    }
    
    // List all shuttles (JavaFX GUI compatible format)
    public Map<String, Map<String, Object>> getAllShuttles() {
        try {
            var docs = db.collection("shuttles").get().get().getDocuments();
            Map<String, Map<String, Object>> shuttles = new HashMap<>();
            
            for (var doc : docs) {
                Map<String, Object> shuttle = new HashMap<>(doc.getData());
                shuttles.put(doc.getId(), shuttle);
            }
            return shuttles;
        } catch (Exception e) {
            System.err.println("Error listing shuttles: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // Original method (keeping for backwards compatibility)
    public List<Map<String, Object>> getAllShuttlesList() {
        try {
            var docs = db.collection("shuttles").get().get().getDocuments();
            List<Map<String, Object>> shuttles = new ArrayList<>();
            
            for (var doc : docs) {
                Map<String, Object> shuttle = new HashMap<>(doc.getData());
                shuttle.put("id", doc.getId());
                shuttles.add(shuttle);
            }
            return shuttles;
        } catch (Exception e) {
            System.err.println("Error listing shuttles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
