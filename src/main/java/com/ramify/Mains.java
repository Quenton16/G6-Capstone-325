package com.ramify;

import java.util.*;

/**
 * Simple demo showing how to use the Firebase managers
 */
public class Mains {
    
    public static void main(String[] args) {
        // Initialize Firebase connection
        FirebaseConnection.initialize();
        
        // Create manager instances
        UserManager userManager = new UserManager();
        ClubManager clubManager = new ClubManager();
        EventManager eventManager = new EventManager();
        ShuttleManager shuttleManager = new ShuttleManager();
        LoungeManager loungeManager = new LoungeManager();
        
        System.out.println("=== Ramify Firebase Demo ===\n");
        
        // Example 1: Add a user
        System.out.println("1. Adding a user...");
        String userId = userManager.addUser(
            "john_doe", 
            "password123", 
            "John", 
            "Doe", 
            "john@example.com", 
            "GENERAL_STUDENT"
        );
        System.out.println("User ID: " + userId + "\n");
        
        // Example 2: Add a club
        System.out.println("2. Adding a club...");
        String clubId = clubManager.addClub(
            "Computer Science Club", 
            "A club for CS enthusiasts", 
            "Academic", 
            userId, 
            "csclub@example.com"
        );
        System.out.println("Club ID: " + clubId + "\n");
        
        // Example 3: Add an event
        System.out.println("3. Adding an event...");
        String eventId = eventManager.addEvent(
            "Hackathon 2024", 
            "Annual coding competition", 
            clubId, 
            new Date(), 
            new Date(System.currentTimeMillis() + 86400000), // Tomorrow
            "Main Auditorium", 
            100
        );
        System.out.println("Event ID: " + eventId + "\n");
        
        // Example 4: Add a shuttle
        System.out.println("4. Adding a shuttle...");
        String shuttleId = shuttleManager.addShuttle(
            "Campus Loop", 
            "BUS-001", 
            "Jane Smith", 
            40, 
            "Main Gate"
        );
        System.out.println("Shuttle ID: " + shuttleId + "\n");
        
        // Example 5: Add a lounge
        System.out.println("5. Adding a lounge...");
        String loungeId = loungeManager.addLounge(
            "Study Lounge A", 
            "Main Building", 
            3, 
            50, 
            "WiFi, Whiteboard, Coffee Machine"
        );
        System.out.println("Lounge ID: " + loungeId + "\n");
        
        // Example 6: Update lounge occupancy
        System.out.println("6. Updating lounge occupancy...");
        loungeManager.updateOccupancy(loungeId, 25);
        System.out.println("Occupancy updated\n");
        
        // Example 7: Retrieve and display data
        System.out.println("7. Retrieving all users...");
        Map<String, Map<String, Object>> users = userManager.getAllUsers();
        System.out.println("Total users: " + users.size() + "\n");
        
        System.out.println("=== Demo Complete ===");
    }
}
