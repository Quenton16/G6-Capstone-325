import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.*;

/**
 * Manages Club information in Firebase
 */
public class ClubManager {
    private Firestore db;
    
    public ClubManager() {
        this.db = FirebaseConnection.getFirestore();
    }
    
    // Add a new club
    public String addClub(String clubName, String description, String category, 
                         String presidentId, String email) {
        try {
            Map<String, Object> clubData = new HashMap<>();
            clubData.put("clubName", clubName);
            clubData.put("description", description);
            clubData.put("category", category);
            clubData.put("presidentId", presidentId);
            clubData.put("email", email);
            clubData.put("memberCount", 0);
            clubData.put("createdAt", Timestamp.now());
            clubData.put("active", true);
            
            DocumentReference docRef = db.collection("clubs").document();
            docRef.set(clubData).get();
            
            System.out.println("Club added: " + clubName);
            return docRef.getId();
        } catch (Exception e) {
            System.err.println("Error adding club: " + e.getMessage());
            return null;
        }
    }
    
    // Get club by ID
    public Map<String, Object> getClub(String clubId) {
        try {
            var doc = db.collection("clubs").document(clubId).get().get();
            if (doc.exists()) {
                return doc.getData();
            }
        } catch (Exception e) {
            System.err.println("Error getting club: " + e.getMessage());
        }
        return null;
    }
    
    // Update club
    public boolean updateClub(String clubId, Map<String, Object> updates) {
        try {
            db.collection("clubs").document(clubId).update(updates).get();
            System.out.println("Club updated: " + clubId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating club: " + e.getMessage());
            return false;
        }
    }
    
    // Delete club
    public boolean deleteClub(String clubId) {
        try {
            db.collection("clubs").document(clubId).delete().get();
            System.out.println("Club deleted: " + clubId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting club: " + e.getMessage());
            return false;
        }
    }
    
    // Simplified addClub method for JavaFX GUI
    public String addClub(String name, String description, String presidentId) {
        return addClub(name, description, "General", presidentId, name.toLowerCase().replace(" ", "") + "@club.edu");
    }
    
    // Update club method for JavaFX GUI
    public boolean updateClub(String clubId, String name, String description, String presidentId) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("clubName", name);
            updates.put("description", description);
            updates.put("presidentId", presidentId);
            return updateClub(clubId, updates);
        } catch (Exception e) {
            System.err.println("Error updating club: " + e.getMessage());
            return false;
        }
    }
    
    // List all clubs (JavaFX GUI compatible format)
    public Map<String, Map<String, Object>> getAllClubs() {
        try {
            var docs = db.collection("clubs").get().get().getDocuments();
            Map<String, Map<String, Object>> clubs = new HashMap<>();
            
            for (var doc : docs) {
                Map<String, Object> club = new HashMap<>(doc.getData());
                // Rename clubName to name for GUI compatibility
                club.put("name", club.get("clubName"));
                clubs.put(doc.getId(), club);
            }
            return clubs;
        } catch (Exception e) {
            System.err.println("Error listing clubs: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // Original method (keeping for backwards compatibility)
    public List<Map<String, Object>> getAllClubsList() {
        try {
            var docs = db.collection("clubs").get().get().getDocuments();
            List<Map<String, Object>> clubs = new ArrayList<>();
            
            for (var doc : docs) {
                Map<String, Object> club = new HashMap<>(doc.getData());
                club.put("id", doc.getId());
                clubs.add(club);
            }
            return clubs;
        } catch (Exception e) {
            System.err.println("Error listing clubs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
