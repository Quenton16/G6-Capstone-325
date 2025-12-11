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
import java.util.Date;
import java.text.SimpleDateFormat;

public class EventDetailsPage {

    private static EventManager eventManager = new EventManager();
    private static ClubManager clubManager = new ClubManager();

    public static Scene getScene(Stage stage, Scene previousScene, String eventId) {
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

        // Load event data
        Map<String, Object> eventData = eventManager.getEvent(eventId);

        if (eventData == null) {
            Label errorLabel = new Label("Event not found.");
            errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #d32f2f;");
            root.getChildren().addAll(topBar, errorLabel);
            return new Scene(root, 450, 650);
        }

        String eventName = (String) eventData.getOrDefault("name", "Unknown Event");
        String description = (String) eventData.getOrDefault("description", "No description available.");
        String location = (String) eventData.getOrDefault("location", "TBD");
        String category = (String) eventData.getOrDefault("category", "General");
        String clubId = (String) eventData.getOrDefault("clubId", "");
        
        Object maxAttendeesObj = eventData.get("maxAttendees");
        int maxAttendees = maxAttendeesObj instanceof Number ? ((Number) maxAttendeesObj).intValue() : 0;
        
        // Get club name
        String clubName = "Unknown Organizer";
        if (!clubId.isEmpty()) {
            Map<String, Object> club = clubManager.getClub(clubId);
            if (club != null) {
                clubName = (String) club.getOrDefault("name", "Unknown Organizer");
            }
        }

        // Format dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String startDateStr = "TBD";
        String startTimeStr = "";
        String endDateStr = "TBD";
        String endTimeStr = "";
        
        Object startDateObj = eventData.get("startDate");
        Object endDateObj = eventData.get("endDate");
        
        if (startDateObj instanceof com.google.cloud.Timestamp) {
            Date startDate = ((com.google.cloud.Timestamp) startDateObj).toDate();
            startDateStr = dateFormat.format(startDate);
            startTimeStr = timeFormat.format(startDate);
        } else if (startDateObj instanceof Date) {
            startDateStr = dateFormat.format((Date) startDateObj);
            startTimeStr = timeFormat.format((Date) startDateObj);
        }
        
        if (endDateObj instanceof com.google.cloud.Timestamp) {
            Date endDate = ((com.google.cloud.Timestamp) endDateObj).toDate();
            endDateStr = dateFormat.format(endDate);
            endTimeStr = timeFormat.format(endDate);
        } else if (endDateObj instanceof Date) {
            endDateStr = dateFormat.format((Date) endDateObj);
            endTimeStr = timeFormat.format((Date) endDateObj);
        }

        // Event Info Card
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

        Label nameLabel = new Label(eventName);
        nameLabel.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);
        nameLabel.setWrapText(true);

        Label categoryLabel = new Label("📂 " + category);
        categoryLabel.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: white;
                -fx-background-color: #0b5e2a;
                -fx-padding: 4 12;
                -fx-background-radius: 12;
                """);

        Label clubLabel = new Label("Hosted by: " + clubName);
        clubLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #555;
                -fx-font-style: italic;
                """);

        HBox headerInfo = new HBox(10, categoryLabel);
        headerInfo.setAlignment(Pos.CENTER_LEFT);

        Label descLabel = new Label(description);
        descLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #333;
                -fx-line-spacing: 2;
                """);
        descLabel.setWrapText(true);

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(15));
        detailsBox.setStyle("""
                -fx-background-color: #f7fff8;
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-color: #c3e1cf;
                -fx-border-width: 1;
                """);

        Label whenLabel = new Label("📅 WHEN");
        whenLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0b5e2a; -fx-font-weight: bold;");
        
        Label whenValueLabel = new Label(startDateStr + " at " + startTimeStr + " - " + endTimeStr);
        whenValueLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333; -fx-padding: 0 0 0 10;");
        whenValueLabel.setWrapText(true);

        Label whereLabel = new Label("📍 WHERE");
        whereLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0b5e2a; -fx-font-weight: bold;");
        
        Label whereValueLabel = new Label(location);
        whereValueLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333; -fx-padding: 0 0 0 10;");
        whereValueLabel.setWrapText(true);

        Label capacityLabel = new Label("👥 CAPACITY");
        capacityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0b5e2a; -fx-font-weight: bold;");
        
        Label capacityValueLabel = new Label(maxAttendees > 0 ? maxAttendees + " attendees max" : "Unlimited");
        capacityValueLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333; -fx-padding: 0 0 0 10;");

        detailsBox.getChildren().addAll(
            whenLabel, whenValueLabel,
            whereLabel, whereValueLabel,
            capacityLabel, capacityValueLabel
        );

        // RSVP Button
        Button rsvpBtn = new Button("✓ RSVP to Event");
        rsvpBtn.setPrefWidth(200);
        rsvpBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-padding: 12 24;
                -fx-background-radius: 8;
                """);
        rsvpBtn.setOnAction(e -> {
            Label confirmLabel = new Label("✓ You're registered for this event!");
            confirmLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold; -fx-font-size: 14px;");
            infoCard.getChildren().add(confirmLabel);
            rsvpBtn.setDisable(true);
            rsvpBtn.setText("✓ Registered");
        });

        infoCard.getChildren().addAll(nameLabel, headerInfo, clubLabel, descLabel, detailsBox, rsvpBtn);

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
