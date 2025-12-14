package com.ramify.ui;
import com.ramify.manager.ClubManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.util.Map;

public class ClubDetailsPage {

    private static ClubManager clubManager = new ClubManager();

    public static Scene getScene(Stage stage, Scene previousScene, String clubId) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f1f6f2, #e3ede6);");

        // Back Button
        Button backBtn = new Button("← Back");
        backBtn.setStyle("""
                -fx-background-color: #00563F;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 8 16;
                -fx-background-radius: 8;
                """);
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        HBox topBar = new HBox(backBtn);
        topBar.setAlignment(Pos.TOP_LEFT);

        // Load club data
        Map<String, Object> clubData = clubManager.getClub(clubId);

        if (clubData == null) {
            Label errorLabel = new Label("Club not found.");
            errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #d32f2f;");
            root.getChildren().addAll(topBar, errorLabel);
            return new Scene(root, 450, 650);
        }

        String clubName = (String) clubData.getOrDefault("clubName", "Unknown Club");
        String description = (String) clubData.getOrDefault("description", "No description available.");
        String category = (String) clubData.getOrDefault("category", "General");
        String contactEmail = (String) clubData.getOrDefault("email", "N/A");
        String presidentId = (String) clubData.getOrDefault("presidentId", "N/A");

        // Club Image - Smart matching based on club name
        ImageView clubImageView = null;
        try {
            String imagePath = "/images/ramify_logo.png"; // default
            String lowerName = clubName.toLowerCase();
            
            // Match specific club names to their images
            if (lowerName.contains("fashion")) {
                imagePath = "/images/Fashion Club.jpg";
            } else if (lowerName.contains("film") || lowerName.contains("movie")) {
                imagePath = "/images/Film Club.jpg";
            } else if (lowerName.contains("gaming") || lowerName.contains("game")) {
                imagePath = "/images/Gaming Club.jpg";
            } else if (lowerName.contains("horticulture") || lowerName.contains("garden")) {
                imagePath = "/images/Horticulture Club.jpg";
            } else if (lowerName.contains("motorsport") || lowerName.contains("motor") || lowerName.contains("racing")) {
                imagePath = "/images/Ram Motorsports Club.jpg";
            } else if (lowerName.contains("rock climbing") || lowerName.contains("climbing")) {
                imagePath = "/images/Rocking Climbing Club.jpg";
            } else if (lowerName.contains("cook") || lowerName.contains("crook") || lowerName.contains("culinary")) {
                imagePath = "/images/cooksandcrookslogo.jpg";
            } else if (lowerName.contains("cricket") || lowerName.contains("sport")) {
                imagePath = "/images/Cricketclublogo.jpg";
            } else if (lowerName.contains("esport") || lowerName.contains("e-sport")) {
                imagePath = "/images/EsportsIcon.png";
            } else if (lowerName.contains("music") || lowerName.contains("concert") || lowerName.contains("ramchella")) {
                imagePath = "/images/Ramchella.png";
            }
            
            clubImageView = new ImageView(new Image(imagePath));
            clubImageView.setFitWidth(120);
            clubImageView.setFitHeight(120);
            clubImageView.setPreserveRatio(true);
            clubImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.0, 0, 2);");
        } catch (Exception e) {
            // Image not found
        }

        // Club Info Card
        VBox infoCard = new VBox(15);
        infoCard.setAlignment(Pos.TOP_LEFT);
        infoCard.setPadding(new Insets(20));
        infoCard.setMaxWidth(420);
        infoCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 16;
                -fx-border-radius: 16;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 18, 0.0, 0, 4);
                """);

        Label nameLabel = new Label(clubName);
        nameLabel.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);
        nameLabel.setWrapText(true);

        Label categoryLabel = new Label("Category: " + category);
        categoryLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #555;
                -fx-font-style: italic;
                """);

        Label descLabel = new Label(description);
        descLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #333;
                -fx-line-spacing: 2;
                """);
        descLabel.setWrapText(true);

        Label emailLabel = new Label("Contact: " + contactEmail);
        emailLabel.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #0b5e2a;
                """);

        Label presidentLabel = new Label("President ID: " + presidentId);
        presidentLabel.setStyle("""
                -fx-font-size: 12px;
                -fx-text-fill: #666;
                """);

        if (clubImageView != null) {
            VBox imageBox = new VBox(clubImageView);
            imageBox.setAlignment(Pos.CENTER);
            imageBox.setPadding(new Insets(0, 0, 10, 0));
            infoCard.getChildren().add(imageBox);
        }
        
        infoCard.getChildren().addAll(nameLabel, categoryLabel, descLabel, emailLabel, presidentLabel);

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15, topBar, infoCard);
        content.setPadding(new Insets(10));
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, 450, 650);
    }
}
