import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.util.Map;

public class ClubsPage {

    private static ClubManager clubManager = new ClubManager();

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f1f6f2, #e3ede6);");

        // Back Button
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("""
                -fx-background-color: #00563F;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 8 16;
                -fx-background-radius: 8;
                """);
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        Label titleLabel = new Label("All Clubs");
        titleLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);

        VBox clubsList = new VBox(12);
        clubsList.setAlignment(Pos.TOP_CENTER);
        clubsList.setMaxWidth(420);

        // Load all clubs
        Map<String, Map<String, Object>> allClubs = clubManager.getAllClubs();

        if (allClubs.isEmpty()) {
            Label noClubsLabel = new Label("No clubs available yet.");
            noClubsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-font-style: italic;");
            clubsList.getChildren().add(noClubsLabel);
        } else {
            for (Map.Entry<String, Map<String, Object>> entry : allClubs.entrySet()) {
                String clubId = entry.getKey();
                Map<String, Object> clubData = entry.getValue();

                String clubName = (String) clubData.getOrDefault("name", "Unknown Club");
                String category = (String) clubData.getOrDefault("category", "General");
                String description = (String) clubData.getOrDefault("description", "");

                VBox clubCard = new VBox(8);
                clubCard.setAlignment(Pos.TOP_LEFT);
                clubCard.setPadding(new Insets(15));
                clubCard.setStyle("""
                        -fx-background-color: white;
                        -fx-background-radius: 12;
                        -fx-border-radius: 12;
                        -fx-border-color: #c3e1cf;
                        -fx-border-width: 1;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.0, 0, 2);
                        """);

                Label nameLabel = new Label(clubName);
                nameLabel.setStyle("""
                        -fx-font-size: 18px;
                        -fx-font-weight: bold;
                        -fx-text-fill: #0b5e2a;
                        """);

                Label categoryLabel = new Label(category);
                categoryLabel.setStyle("""
                        -fx-font-size: 12px;
                        -fx-text-fill: #666;
                        -fx-font-style: italic;
                        """);

                Label descLabel = new Label(description.length() > 80 ? 
                        description.substring(0, 80) + "..." : description);
                descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
                descLabel.setWrapText(true);

                Button viewBtn = new Button("View Details →");
                viewBtn.setStyle("""
                        -fx-background-color: transparent;
                        -fx-text-fill: #0b5e2a;
                        -fx-font-weight: bold;
                        -fx-cursor: hand;
                        """);
                viewBtn.setOnAction(e -> 
                        stage.setScene(ClubDetailsPage.getScene(stage, stage.getScene(), clubId))
                );

                clubCard.getChildren().addAll(nameLabel, categoryLabel, descLabel, viewBtn);
                clubsList.getChildren().add(clubCard);
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15, backBtn, titleLabel, clubsList);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, 450, 650);
    }
}
