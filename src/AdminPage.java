import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Date;
import java.util.Map;

public class AdminPage {

    private static UserManager userManager = new UserManager();
    private static ClubManager clubManager = new ClubManager();
    private static EventManager eventManager = new EventManager();
    private static ShuttleManager shuttleManager = new ShuttleManager();
    private static LoungeManager loungeManager = new LoungeManager();

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

        Label titleLabel = new Label("⚙️ Admin Panel");
        titleLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);

        Label subtitleLabel = new Label("Manage clubs, events, shuttles, and lounges");
        subtitleLabel.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #666;
                -fx-font-style: italic;
                """);

        // Create Tab Pane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setMaxWidth(420);

        // Clubs Tab
        Tab clubsTab = new Tab("Clubs", createClubsTab(stage));
        
        // Events Tab
        Tab eventsTab = new Tab("Events", createEventsTab(stage));
        
        // Shuttles Tab
        Tab shuttlesTab = new Tab("Shuttles", createShuttlesTab(stage));
        
        // Lounges Tab
        Tab loungesTab = new Tab("Lounges", createLoungesTab(stage));

        tabPane.getTabs().addAll(clubsTab, eventsTab, shuttlesTab, loungesTab);

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15, backBtn, titleLabel, subtitleLabel, tabPane);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, 450, 650);
    }

    private static VBox createClubsTab(Stage stage) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Add New Club");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b5e2a;");

        TextField nameField = new TextField();
        nameField.setPromptText("Club Name");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category (e.g., Academic, Sports)");
        
        TextField presidentField = new TextField();
        presidentField.setPromptText("President User ID");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Contact Email");

        Button addBtn = new Button("➕ Add Club");
        addBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                """);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String desc = descArea.getText().trim();
            String category = categoryField.getText().trim();
            String presidentId = presidentField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {
                statusLabel.setText("❌ Name and category are required!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
                return;
            }

            try {
                String clubId = clubManager.addClub(name, desc, category, presidentId, email);
                statusLabel.setText("✅ Club added successfully! ID: " + clubId);
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12px;");
                
                // Clear fields
                nameField.clear();
                descArea.clear();
                categoryField.clear();
                presidentField.clear();
                emailField.clear();
            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            }
        });

        // View All Clubs button
        Button viewAllBtn = new Button("📋 View All Clubs");
        viewAllBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #0b5e2a;
                -fx-font-weight: bold;
                -fx-border-color: #0b5e2a;
                -fx-border-width: 1;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 10 20;
                """);
        viewAllBtn.setOnAction(e -> stage.setScene(ClubsPage.getScene(stage, stage.getScene())));

        content.getChildren().addAll(
                titleLabel, nameField, descArea, categoryField, 
                presidentField, emailField, addBtn, statusLabel, viewAllBtn
        );

        return content;
    }

    private static VBox createEventsTab(Stage stage) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Add New Event");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b5e2a;");

        TextField nameField = new TextField();
        nameField.setPromptText("Event Name");
        
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category (e.g., Social, Academic, Sports)");
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        
        TextField locationField = new TextField();
        locationField.setPromptText("Location (e.g., Campus Center, Room 203)");
        
        Label dateTimeLabel = new Label("Start Date & Time:");
        dateTimeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555; -fx-font-weight: bold;");
        
        TextField startDateField = new TextField();
        startDateField.setPromptText("Start Date (MM/DD/YYYY)");
        
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Start Time (e.g., 2:00 PM)");
        
        Label endDateTimeLabel = new Label("End Date & Time:");
        endDateTimeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555; -fx-font-weight: bold;");
        
        TextField endDateField = new TextField();
        endDateField.setPromptText("End Date (MM/DD/YYYY)");
        
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("End Time (e.g., 4:00 PM)");
        
        TextField clubIdField = new TextField();
        clubIdField.setPromptText("Club ID (hosting this event, optional)");
        
        TextField maxAttendeesField = new TextField();
        maxAttendeesField.setPromptText("Max Attendees (optional)");

        Button addBtn = new Button("➕ Add Event");
        addBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                """);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");
        statusLabel.setWrapText(true);

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String desc = descArea.getText().trim();
            String location = locationField.getText().trim();
            String startDateStr = startDateField.getText().trim();
            String startTimeStr = startTimeField.getText().trim();
            String endDateStr = endDateField.getText().trim();
            String endTimeStr = endTimeField.getText().trim();
            String clubId = clubIdField.getText().trim();
            String maxAttendeesStr = maxAttendeesField.getText().trim();

            if (name.isEmpty() || location.isEmpty()) {
                statusLabel.setText("❌ Event name and location are required!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
                return;
            }

            try {
                Date startDate;
                Date endDate;
                
                // Parse dates if provided, otherwise use defaults
                if (!startDateStr.isEmpty() && !startTimeStr.isEmpty()) {
                    String startDateTime = startDateStr + " " + startTimeStr;
                    startDate = parseDateTime(startDateTime);
                } else {
                    startDate = new Date(); // Default to now
                }
                
                if (!endDateStr.isEmpty() && !endTimeStr.isEmpty()) {
                    String endDateTime = endDateStr + " " + endTimeStr;
                    endDate = parseDateTime(endDateTime);
                } else {
                    endDate = new Date(startDate.getTime() + 7200000); // +2 hours default
                }
                
                int maxAttendees = maxAttendeesStr.isEmpty() ? 100 : Integer.parseInt(maxAttendeesStr);
                String eventCategory = category.isEmpty() ? "General" : category;
                
                String eventId = eventManager.addEvent(name, desc, clubId, startDate, endDate, 
                                                       location, maxAttendees, eventCategory);
                statusLabel.setText("✅ Event added successfully! View in Dashboard → Events");
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12px;");
                
                // Clear fields
                nameField.clear();
                categoryField.clear();
                descArea.clear();
                locationField.clear();
                startDateField.clear();
                startTimeField.clear();
                endDateField.clear();
                endTimeField.clear();
                clubIdField.clear();
                maxAttendeesField.clear();
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Max attendees must be a number!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            }
        });

        Label noteLabel = new Label("Tip: Use formats like 12/15/2025 for date and 2:00 PM for time");
        noteLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888; -fx-font-style: italic;");
        noteLabel.setWrapText(true);

        content.getChildren().addAll(
                titleLabel, nameField, categoryField, descArea, locationField,
                dateTimeLabel, startDateField, startTimeField,
                endDateTimeLabel, endDateField, endTimeField,
                clubIdField, maxAttendeesField, noteLabel, addBtn, statusLabel
        );

        return content;
    }
    
    // Helper method to parse date/time strings
    private static Date parseDateTime(String dateTimeStr) {
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy h:mm a");
            return format.parse(dateTimeStr);
        } catch (Exception e) {
            // If parsing fails, return current date
            return new Date();
        }
    }

    private static VBox createShuttlesTab(Stage stage) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Add Shuttle Schedule");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b5e2a;");

        Label infoLabel = new Label("Enter departure times for the three locations:");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-font-style: italic;");
        infoLabel.setWrapText(true);

        TextField farmingdaleField = new TextField();
        farmingdaleField.setPromptText("Departs Farmingdale LIRR (e.g., 8:00 AM)");
        
        TextField laffinField = new TextField();
        laffinField.setPromptText("Departs Laffin Hall (e.g., 8:15 AM)");
        
        TextField aviationField = new TextField();
        aviationField.setPromptText("Departs Aviation Center (e.g., 8:30 AM)");
        
        Label optionalLabel = new Label("Optional fields (for internal tracking):");
        optionalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888; -fx-font-style: italic; -fx-padding: 10 0 0 0;");
        
        TextField vehicleField = new TextField();
        vehicleField.setPromptText("Vehicle ID (optional, e.g., BUS-001)");
        
        TextField driverField = new TextField();
        driverField.setPromptText("Driver Name (optional)");
        
        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity (optional, default: 40)");

        Button addBtn = new Button("➕ Add Schedule");
        addBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                """);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");
        statusLabel.setWrapText(true);

        addBtn.setOnAction(e -> {
            String farmingdale = farmingdaleField.getText().trim();
            String laffin = laffinField.getText().trim();
            String aviation = aviationField.getText().trim();

            if (farmingdale.isEmpty() && laffin.isEmpty() && aviation.isEmpty()) {
                statusLabel.setText("❌ Please enter at least one departure time!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
                return;
            }

            try {
                String vehicle = vehicleField.getText().trim();
                if (vehicle.isEmpty()) {
                    vehicle = "SHUTTLE-" + (System.currentTimeMillis() % 1000);
                }
                
                String driver = driverField.getText().trim();
                if (driver.isEmpty()) {
                    driver = "On Duty";
                }
                
                String capacityStr = capacityField.getText().trim();
                int capacity = capacityStr.isEmpty() ? 40 : Integer.parseInt(capacityStr);
                
                String location = "Route Active";
                String routeName = "Campus Shuttle";
                
                String shuttleId = shuttleManager.addShuttle(routeName, vehicle, driver, capacity, location, 
                                                             farmingdale, laffin, aviation);
                statusLabel.setText("✅ Shuttle schedule added successfully! Times will appear in the schedule.");
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12px;");
                
                // Clear fields
                farmingdaleField.clear();
                laffinField.clear();
                aviationField.clear();
                vehicleField.clear();
                driverField.clear();
                capacityField.clear();
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Capacity must be a number!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            }
        });

        // View Shuttles button
        Button viewShuttlesBtn = new Button("🚌 View Shuttle Schedule");
        viewShuttlesBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #0b5e2a;
                -fx-font-weight: bold;
                -fx-border-color: #0b5e2a;
                -fx-border-width: 1;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 10 20;
                """);
        viewShuttlesBtn.setOnAction(e -> stage.setScene(ShuttleTrackingPage.getScene(stage, stage.getScene())));

        content.getChildren().addAll(
                titleLabel, infoLabel, farmingdaleField, laffinField, aviationField,
                optionalLabel, vehicleField, driverField, capacityField, 
                addBtn, statusLabel, viewShuttlesBtn
        );

        return content;
    }

    private static VBox createLoungesTab(Stage stage) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Add New Lounge");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b5e2a;");

        TextField nameField = new TextField();
        nameField.setPromptText("Lounge Name (e.g., Study Lounge A)");
        
        TextField buildingField = new TextField();
        buildingField.setPromptText("Building Name");
        
        TextField floorField = new TextField();
        floorField.setPromptText("Floor Number");
        
        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity (number)");
        
        TextField amenitiesField = new TextField();
        amenitiesField.setPromptText("Amenities (e.g., WiFi, Whiteboard)");

        Button addBtn = new Button("➕ Add Lounge");
        addBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                """);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String building = buildingField.getText().trim();
            String floorStr = floorField.getText().trim();
            String capacityStr = capacityField.getText().trim();
            String amenities = amenitiesField.getText().trim();

            if (name.isEmpty() || building.isEmpty()) {
                statusLabel.setText("❌ Name and building are required!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
                return;
            }

            try {
                int floor = floorStr.isEmpty() ? 1 : Integer.parseInt(floorStr);
                int capacity = capacityStr.isEmpty() ? 0 : Integer.parseInt(capacityStr);
                
                String loungeId = loungeManager.addLounge(name, building, floor, capacity, amenities);
                statusLabel.setText("✅ Lounge added successfully! ID: " + loungeId);
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 12px;");
                
                // Clear fields
                nameField.clear();
                buildingField.clear();
                floorField.clear();
                capacityField.clear();
                amenitiesField.clear();
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Floor and capacity must be numbers!");
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            } catch (Exception ex) {
                statusLabel.setText("❌ Error: " + ex.getMessage());
                statusLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            }
        });

        Label noteLabel = new Label("Note: Initial occupancy is set to 0. Update via Firebase or add update feature.");
        noteLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888; -fx-font-style: italic;");
        noteLabel.setWrapText(true);

        content.getChildren().addAll(
                titleLabel, nameField, buildingField, floorField, 
                capacityField, amenitiesField, noteLabel, addBtn, statusLabel
        );

        return content;
    }
}
