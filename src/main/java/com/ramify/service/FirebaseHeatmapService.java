package com.ramify.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHeatmapService {

    // base URL of your RTDB (no path, no .json)
    private static final String BASE_URL =
            "https://csclab7325-default-rtdb.firebaseio.com";

    /**
     * Returns a map: areaName -> number of responses
     * e.g. "Roosevelt Hall" -> 5
     */
    public static Map<String, Integer> getAreaCounts() throws Exception {
        String url = BASE_URL + "/heatmapResponses.json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> res =
                client.send(req, HttpResponse.BodyHandlers.ofString());

        String json = res.body();
        System.out.println("Heatmap JSON: " + json);

        // Very simple parsing: count "area" values with String operations.
        // For a real app you’d use Gson or Jackson, but this works for the project.
        Map<String, Integer> counts = new HashMap<>();

        String key = "\"area\":";
        int idx = 0;
        while ((idx = json.indexOf(key, idx)) != -1) {
            int startQuote = json.indexOf('"', idx + key.length());
            int endQuote = json.indexOf('"', startQuote + 1);
            if (startQuote == -1 || endQuote == -1) break;

            String area = json.substring(startQuote + 1, endQuote);
            counts.merge(area, 1, Integer::sum);

            idx = endQuote + 1;
        }

        return counts;
    }
}
