package com.ramify.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;

/**
 * Simple Firebase connection manager
 */
public class FirebaseConnection {
    private static Firestore firestore;
    
    public static void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream("ramify-key.json");
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            
            firestore = FirestoreClient.getFirestore();
            System.out.println("Firebase connected successfully");
        } catch (Exception e) {
            System.err.println("Firebase connection failed: " + e.getMessage());
        }
    }
    
    public static Firestore getFirestore() {
        if (firestore == null) {
            initialize();
        }
        return firestore;
    }
}
