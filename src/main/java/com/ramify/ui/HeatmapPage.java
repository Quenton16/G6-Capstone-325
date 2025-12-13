package com.ramify.ui;

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

public class HeatmapPage {

    // If your heatmap is a local HTML file in resources (src/main/resources/heatmap.html)
    private static final String LOCAL_HEATMAP_PATH =
            HeatmapPage.class.getResource("/heatmap.html").toExternalForm();



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

        try {
            engine.load(LOCAL_HEATMAP_PATH);
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.loadContent("<h2 style='font-family:sans-serif;color:#b00020'>"
                    + "Unable to load heatmap page.</h2>");
        }

        root.setTop(topBar);
        root.setCenter(webView);

        return new Scene(root, 450, 650);
    }
}
