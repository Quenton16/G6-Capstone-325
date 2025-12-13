package com.ramify.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseAuthService {

    private static final String API_KEY = "AIzaSyBIk4vUtLBoYMy45i-72RziW0SMOFJRXzo";

    // ---------------- LOGIN ----------------
    public static boolean login(String email, String password) throws Exception {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

        String json = """
                {
                    "email": "%s",
                    "password": "%s",
                    "returnSecureToken": true
                }
                """.formatted(email, password);

        HttpResponse<String> response = sendRequest(url, json);
        return response.statusCode() == 200;
    }

    // ---------------- REGISTER ----------------
    public static boolean register(String email, String password) throws Exception {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

        String json = """
                {
                    "email": "%s",
                    "password": "%s",
                    "returnSecureToken": true
                }
                """.formatted(email, password);

        HttpResponse<String> response = sendRequest(url, json);
        return response.statusCode() == 200;
    }

    // ---------------- INTERNAL REQUEST HANDLER ----------------
    private static HttpResponse<String> sendRequest(String url, String jsonBody) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Firebase Response (" + response.statusCode() + "): " + response.body());
        return response;
    }
}
