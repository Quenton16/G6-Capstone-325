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

public class WebViewPage {

    public static Scene getScene(Stage stage, Scene previousScene, String url, String title) {
        BorderPane root = new BorderPane();

        // Top bar with back button + title
        Button backBtn = new Button("<");
        backBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-text-fill: white;");
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox topBar = new HBox(10, backBtn, titleLabel);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #0b5e2a;");

        // WebView
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load(url);

        root.setTop(topBar);
        root.setCenter(webView);

        return new Scene(root, 450, 650);
    }
}
