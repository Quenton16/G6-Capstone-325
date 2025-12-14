package com.ramify.ui;

import com.ramify.service.FirebaseHeatmapService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HeatmapPage {

    public static Scene getScene(Stage stage, Scene previousScene) {
        BorderPane root = new BorderPane();

        // Top bar with back button + title
        Button backBtn = new Button("<");
        backBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 18px;
                -fx-text-fill: #555555;
                """);
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        Label title = new Label("Campus Activity Heatmap");
        title.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);

        HBox topBar = new HBox(10, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("""
                -fx-background-color: linear-gradient(to right, #ffffff, #f6faf7);
                -fx-border-color: #d6e2da;
                -fx-border-width: 0 0 1 0;
                """);

        // WebView that loads your heatmap
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        // Enable debugging
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            System.out.println("Heatmap WebView state: " + newState);
            if (newState == javafx.concurrent.Worker.State.FAILED) {
                System.err.println("Failed to load heatmap!");
                Throwable ex = engine.getLoadWorker().getException();
                if (ex != null) ex.printStackTrace();
            } else if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                System.out.println("Heatmap page loaded successfully!");
                // Load data from Firebase after page loads
                loadHeatmapData(engine);
            }
        });

        try {
            // Get resource URLs
            String cssUrl = HeatmapPage.class.getResource("/css/heatmap.css").toExternalForm();
            String imageUrl = HeatmapPage.class.getResource("/images/campus-map.png").toExternalForm();

            System.out.println("Loading heatmap resources:");
            System.out.println("  CSS: " + cssUrl);
            System.out.println("  Image: " + imageUrl);

            // Load HTML and replace paths
            String html = loadResourceAsString("/heatmap.html");
            html = html.replace("href=\"/css/heatmap.css\"", "href=\"" + cssUrl + "\"");
            html = html.replace("src=\"/images/campus-map.png\"", "src=\"" + imageUrl + "\"");

            engine.loadContent(html);

        } catch (Exception ex) {
            ex.printStackTrace();
            engine.loadContent("<h2 style='font-family:sans-serif;color:#b00020'>"
                    + "Unable to load heatmap: " + ex.getMessage() + "</h2>");
        }

        root.setTop(topBar);
        root.setCenter(webView);

        return new Scene(root, 450, 650);
    }

    private static void loadHeatmapData(WebEngine engine) {
        // Fetch data in background thread
        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() throws Exception {
                return FirebaseHeatmapService.getAreaCounts();
            }
        };

        task.setOnSucceeded(event -> {
            Map<String, Integer> areaCounts = task.getValue();
            System.out.println("Loaded heatmap data: " + areaCounts);

            // Convert to JSON and pass to JavaScript
            Gson gson = new Gson();
            String json = gson.toJson(areaCounts);

            // Call JavaScript function to render the heatmap
            Platform.runLater(() -> {
                String script = "if (typeof renderHeatmapFromJava === 'function') { "
                        + "renderHeatmapFromJava(" + json + "); "
                        + "} else { "
                        + "console.error('renderHeatmapFromJava function not found'); "
                        + "}";
                engine.executeScript(script);
            });
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load heatmap data: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private static String loadResourceAsString(String path) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        HeatmapPage.class.getResourceAsStream(path),
                        StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}