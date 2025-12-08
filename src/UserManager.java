import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import java.util.*;

/**
 * Manages User information in Firebase
 */
public class UserManager {
    private Firestore db;
    
    public UserManager() {
        this.db = FirebaseConnection.getFirestore();
    }
    
    // Add a new user
    public String addUser(String username, String password, String firstName, String lastName, 
                         String email, String role) {
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("password", password);
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);
            userData.put("email", email);
            userData.put("role", role); // ADMIN, GENERAL_STUDENT, CLUB_OFFICER
            userData.put("createdAt", Timestamp.now());
            userData.put("active", true);
            
            DocumentReference docRef = db.collection("users").document();
            docRef.set(userData).get();
            
            System.out.println("User added: " + username);
            return docRef.getId();
        } catch (Exception e) {
            System.err.println("Error adding user: " + e.getMessage());
            return null;
        }
    }
    
    // Get user by username
    public Map<String, Object> getUser(String username) {
        try {
            var docs = db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .get()
                    .getDocuments();
            
            if (!docs.isEmpty()) {
                return docs.get(0).getData();
            }
        } catch (Exception e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }
    
    // Update user
    public boolean updateUser(String userId, Map<String, Object> updates) {
        try {
            db.collection("users").document(userId).update(updates).get();
            System.out.println("User updated: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    // Delete user (soft delete)
    public boolean deleteUser(String userId) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("active", false);
            db.collection("users").document(userId).update(updates).get();
            System.out.println("User deleted: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // Simplified addUser method for JavaFX GUI
    public String addUser(String name, String email, String role) {
        // Split name into first and last name
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // Generate username from email
        String username = email.split("@")[0];
        String defaultPassword = "password123";
        
        return addUser(username, defaultPassword, firstName, lastName, email, role);
    }
    
    // Update user method for JavaFX GUI
    public boolean updateUser(String userId, String name, String email, String role) {
        try {
            String[] nameParts = name.trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";
            
            Map<String, Object> updates = new HashMap<>();
            updates.put("firstName", firstName);
            updates.put("lastName", lastName);
            updates.put("email", email);
            updates.put("role", role);
            
            return updateUser(userId, updates);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    // List all users (JavaFX GUI compatible format)
    public Map<String, Map<String, Object>> getAllUsers() {
        try {
            var docs = db.collection("users").get().get().getDocuments();
            Map<String, Map<String, Object>> users = new HashMap<>();
            
            for (var doc : docs) {
                Map<String, Object> user = new HashMap<>(doc.getData());
                // Combine first and last name for GUI
                String fullName = user.get("firstName") + " " + user.get("lastName");
                user.put("name", fullName);
                users.put(doc.getId(), user);
            }
            return users;
        } catch (Exception e) {
            System.err.println("Error listing users: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    // Original method (keeping for backwards compatibility)
    public List<Map<String, Object>> getAllUsersList() {
        try {
            var docs = db.collection("users").get().get().getDocuments();
            List<Map<String, Object>> users = new ArrayList<>();
            
            for (var doc : docs) {
                Map<String, Object> user = new HashMap<>(doc.getData());
                user.put("id", doc.getId());
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.err.println("Error listing users: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
