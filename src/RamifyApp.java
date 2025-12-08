import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

public class RamifyApp extends Application {
    
    private UserManager userManager;
    private ClubManager clubManager;
    private EventManager eventManager;
    private ShuttleManager shuttleManager;
    private LoungeManager loungeManager;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize Firebase connection
        FirebaseConnection.initialize();
        
        // Initialize managers
        userManager = new UserManager();
        clubManager = new ClubManager();
        eventManager = new EventManager();
        shuttleManager = new ShuttleManager();
        loungeManager = new LoungeManager();
        
        primaryStage.setTitle("Ramify - Campus Management System");
        
        // Create main container with header
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Create header with logo
        HBox header = createHeader();
        mainLayout.setTop(header);
        
        // Create main tab pane with modern styling
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Create tabs for each entity
        Tab userTab = createUserTab();
        Tab clubTab = createClubTab();
        Tab eventTab = createEventTab();
        Tab shuttleTab = createShuttleTab();
        Tab loungeTab = createLoungeTab();
        
        // Style tabs
        styleTab(userTab, "#00563F");
        styleTab(clubTab, "#00563F");
        styleTab(eventTab, "#00563F");
        styleTab(shuttleTab, "#00563F");
        styleTab(loungeTab, "#00563F");
        
        tabPane.getTabs().addAll(userTab, clubTab, eventTab, shuttleTab, loungeTab);
        
        mainLayout.setCenter(tabPane);
        
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add("data:text/css," + getModernStyles());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #00563F; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
        
        // Try to load logo
        try {
            File logoFile = new File("Images Folder/ramify_logo - Copy.png");
            if (logoFile.exists()) {
                Image logo = new Image(logoFile.toURI().toString());
                ImageView logoView = new ImageView(logo);
                logoView.setFitHeight(50);
                logoView.setPreserveRatio(true);
                header.getChildren().add(logoView);
            }
        } catch (Exception e) {
            // If logo fails to load, just show text
        }
        
        Label titleLabel = new Label("RAMIFY");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        Label subtitleLabel = new Label("Campus Management System");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #B8E6D5; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        header.getChildren().add(titleBox);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().add(spacer);
        
        return header;
    }
    
    private void styleTab(Tab tab, String color) {
        tab.setStyle("-fx-background-color: " + color + "; -fx-text-base-color: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }
    
    private String getModernStyles() {
        return ".tab-pane { -fx-tab-min-height: 45px; } " +
               ".tab { -fx-background-color: #00563F; -fx-background-radius: 8 8 0 0; -fx-border-color: transparent; } " +
               ".tab:selected { -fx-background-color: #007A5E; } " +
               ".tab .tab-label { -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; } " +
               ".button { -fx-cursor: hand; } ";
    }
    
    private Tab createUserTab() {
        Tab tab = new Tab("👤 Users");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("User Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        // User form in a card
        VBox formCard = createFormCard();
        
        GridPane userForm = new GridPane();
        userForm.setHgap(15);
        userForm.setVgap(15);
        userForm.setPadding(new Insets(20));
        userForm.setAlignment(Pos.CENTER);
        
        TextField userIdField = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("ADMIN", "GENERAL_STUDENT", "CLUB_OFFICER");
        roleBox.setValue("GENERAL_STUDENT");
        
        userForm.add(createFormLabel("User ID:"), 0, 0);
        userForm.add(userIdField, 1, 0);
        userForm.add(createFormLabel("Name:"), 0, 1);
        userForm.add(nameField, 1, 1);
        userForm.add(createFormLabel("Email:"), 0, 2);
        userForm.add(emailField, 1, 2);
        userForm.add(createFormLabel("Role:"), 0, 3);
        userForm.add(roleBox, 1, 3);
        
        // Style the form fields
        styleFormElements(userIdField, nameField, emailField, roleBox);
        
        formCard.getChildren().add(userForm);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button addButton = createStyledButton("✓ Add User", "#00A86B");
        Button updateButton = createStyledButton("✎ Update User", "#FF8C00");
        Button deleteButton = createStyledButton("✕ Delete User", "#E53935");
        Button viewButton = createStyledButton("👁 View All Users", "#1976D2");
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);
        
        // Text area for results in a card
        VBox resultCard = createFormCard();
        Label resultLabel = new Label("Results");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-padding: 0 0 10 0;");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: #fafafa; -fx-control-inner-background: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        resultCard.getChildren().addAll(resultLabel, resultArea);
        
        // Button actions
        addButton.setOnAction(e -> {
            try {
                String userId = userManager.addUser(nameField.getText(), emailField.getText(), roleBox.getValue());
                resultArea.setText("User added successfully with ID: " + userId);
                clearUserFields(userIdField, nameField, emailField, roleBox);
            } catch (Exception ex) {
                resultArea.setText("Error adding user: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                userManager.updateUser(userIdField.getText(), nameField.getText(), emailField.getText(), roleBox.getValue());
                resultArea.setText("User updated successfully!");
            } catch (Exception ex) {
                resultArea.setText("Error updating user: " + ex.getMessage());
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                userManager.deleteUser(userIdField.getText());
                resultArea.setText("User deleted successfully!");
                clearUserFields(userIdField, nameField, emailField, roleBox);
            } catch (Exception ex) {
                resultArea.setText("Error deleting user: " + ex.getMessage());
            }
        });
        
        viewButton.setOnAction(e -> {
            try {
                Map<String, Map<String, Object>> users = userManager.getAllUsers();
                StringBuilder sb = new StringBuilder("All Users:\n\n");
                users.forEach((id, data) -> {
                    sb.append("ID: ").append(id).append("\n");
                    sb.append("Name: ").append(data.get("name")).append("\n");
                    sb.append("Email: ").append(data.get("email")).append("\n");
                    sb.append("Role: ").append(data.get("role")).append("\n");
                    sb.append("Created: ").append(data.get("createdAt")).append("\n");
                    sb.append("---\n");
                });
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                resultArea.setText("Error retrieving users: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, formCard, buttonBox, resultCard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        tab.setContent(scrollPane);
        return tab;
    }
    
    private Tab createClubTab() {
        Tab tab = new Tab("🏆 Clubs");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Club Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        VBox formCard = createFormCard();
        
        GridPane clubForm = new GridPane();
        clubForm.setHgap(15);
        clubForm.setVgap(15);
        clubForm.setPadding(new Insets(20));
        clubForm.setAlignment(Pos.CENTER);
        
        TextField clubIdField = new TextField();
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField presidentIdField = new TextField();
        
        clubForm.add(createFormLabel("Club ID:"), 0, 0);
        clubForm.add(clubIdField, 1, 0);
        clubForm.add(createFormLabel("Name:"), 0, 1);
        clubForm.add(nameField, 1, 1);
        clubForm.add(createFormLabel("Description:"), 0, 2);
        clubForm.add(descriptionField, 1, 2);
        clubForm.add(createFormLabel("President ID:"), 0, 3);
        clubForm.add(presidentIdField, 1, 3);
        
        styleFormElements(clubIdField, nameField, descriptionField, presidentIdField);
        formCard.getChildren().add(clubForm);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button addButton = createStyledButton("✓ Add Club", "#00A86B");
        Button updateButton = createStyledButton("✎ Update Club", "#FF8C00");
        Button deleteButton = createStyledButton("✕ Delete Club", "#E53935");
        Button viewButton = createStyledButton("👁 View All Clubs", "#1976D2");
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);
        
        VBox resultCard = createFormCard();
        Label resultLabel = new Label("Results");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-padding: 0 0 10 0;");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: #fafafa; -fx-control-inner-background: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        resultCard.getChildren().addAll(resultLabel, resultArea);
        
        // Button actions
        addButton.setOnAction(e -> {
            try {
                String clubId = clubManager.addClub(nameField.getText(), descriptionField.getText(), presidentIdField.getText());
                resultArea.setText("Club added successfully with ID: " + clubId);
                clearClubFields(clubIdField, nameField, descriptionField, presidentIdField);
            } catch (Exception ex) {
                resultArea.setText("Error adding club: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                clubManager.updateClub(clubIdField.getText(), nameField.getText(), descriptionField.getText(), presidentIdField.getText());
                resultArea.setText("Club updated successfully!");
            } catch (Exception ex) {
                resultArea.setText("Error updating club: " + ex.getMessage());
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                clubManager.deleteClub(clubIdField.getText());
                resultArea.setText("Club deleted successfully!");
                clearClubFields(clubIdField, nameField, descriptionField, presidentIdField);
            } catch (Exception ex) {
                resultArea.setText("Error deleting club: " + ex.getMessage());
            }
        });
        
        viewButton.setOnAction(e -> {
            try {
                Map<String, Map<String, Object>> clubs = clubManager.getAllClubs();
                StringBuilder sb = new StringBuilder("All Clubs:\n\n");
                clubs.forEach((id, data) -> {
                    sb.append("ID: ").append(id).append("\n");
                    sb.append("Name: ").append(data.get("name")).append("\n");
                    sb.append("Description: ").append(data.get("description")).append("\n");
                    sb.append("President ID: ").append(data.get("presidentId")).append("\n");
                    sb.append("Created: ").append(data.get("createdAt")).append("\n");
                    sb.append("---\n");
                });
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                resultArea.setText("Error retrieving clubs: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, formCard, buttonBox, resultCard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        tab.setContent(scrollPane);
        return tab;
    }
    
    private Tab createEventTab() {
        Tab tab = new Tab("📅 Events");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Event Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        VBox formCard = createFormCard();
        
        GridPane eventForm = new GridPane();
        eventForm.setHgap(15);
        eventForm.setVgap(15);
        eventForm.setPadding(new Insets(20));
        eventForm.setAlignment(Pos.CENTER);
        
        TextField eventIdField = new TextField();
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField locationField = new TextField();
        TextField dateTimeField = new TextField();
        dateTimeField.setPromptText("YYYY-MM-DDTHH:MM");
        TextField organizerIdField = new TextField();
        
        eventForm.add(createFormLabel("Event ID:"), 0, 0);
        eventForm.add(eventIdField, 1, 0);
        eventForm.add(createFormLabel("Name:"), 0, 1);
        eventForm.add(nameField, 1, 1);
        eventForm.add(createFormLabel("Description:"), 0, 2);
        eventForm.add(descriptionField, 1, 2);
        eventForm.add(createFormLabel("Location:"), 0, 3);
        eventForm.add(locationField, 1, 3);
        eventForm.add(createFormLabel("Date/Time:"), 0, 4);
        eventForm.add(dateTimeField, 1, 4);
        eventForm.add(createFormLabel("Organizer ID:"), 0, 5);
        eventForm.add(organizerIdField, 1, 5);
        
        styleFormElements(eventIdField, nameField, descriptionField, locationField, dateTimeField, organizerIdField);
        formCard.getChildren().add(eventForm);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button addButton = createStyledButton("✓ Add Event", "#00A86B");
        Button updateButton = createStyledButton("✎ Update Event", "#FF8C00");
        Button deleteButton = createStyledButton("✕ Delete Event", "#E53935");
        Button viewButton = createStyledButton("👁 View All Events", "#1976D2");
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);
        
        VBox resultCard = createFormCard();
        Label resultLabel = new Label("Results");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-padding: 0 0 10 0;");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: #fafafa; -fx-control-inner-background: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        resultCard.getChildren().addAll(resultLabel, resultArea);
        
        // Button actions
        addButton.setOnAction(e -> {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText());
                String eventId = eventManager.addEvent(nameField.getText(), descriptionField.getText(), 
                    locationField.getText(), dateTime, organizerIdField.getText());
                resultArea.setText("Event added successfully with ID: " + eventId);
                clearEventFields(eventIdField, nameField, descriptionField, locationField, dateTimeField, organizerIdField);
            } catch (Exception ex) {
                resultArea.setText("Error adding event: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText());
                eventManager.updateEvent(eventIdField.getText(), nameField.getText(), descriptionField.getText(),
                    locationField.getText(), dateTime, organizerIdField.getText());
                resultArea.setText("Event updated successfully!");
            } catch (Exception ex) {
                resultArea.setText("Error updating event: " + ex.getMessage());
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                eventManager.deleteEvent(eventIdField.getText());
                resultArea.setText("Event deleted successfully!");
                clearEventFields(eventIdField, nameField, descriptionField, locationField, dateTimeField, organizerIdField);
            } catch (Exception ex) {
                resultArea.setText("Error deleting event: " + ex.getMessage());
            }
        });
        
        viewButton.setOnAction(e -> {
            try {
                Map<String, Map<String, Object>> events = eventManager.getAllEvents();
                StringBuilder sb = new StringBuilder("All Events:\n\n");
                events.forEach((id, data) -> {
                    sb.append("ID: ").append(id).append("\n");
                    sb.append("Name: ").append(data.get("name")).append("\n");
                    sb.append("Description: ").append(data.get("description")).append("\n");
                    sb.append("Location: ").append(data.get("location")).append("\n");
                    sb.append("Date/Time: ").append(data.get("dateTime")).append("\n");
                    sb.append("Organizer ID: ").append(data.get("organizerId")).append("\n");
                    sb.append("Created: ").append(data.get("createdAt")).append("\n");
                    sb.append("---\n");
                });
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                resultArea.setText("Error retrieving events: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, formCard, buttonBox, resultCard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        tab.setContent(scrollPane);
        return tab;
    }
    
    private Tab createShuttleTab() {
        Tab tab = new Tab("🚌 Shuttles");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Shuttle Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        VBox formCard = createFormCard();
        
        GridPane shuttleForm = new GridPane();
        shuttleForm.setHgap(15);
        shuttleForm.setVgap(15);
        shuttleForm.setPadding(new Insets(20));
        shuttleForm.setAlignment(Pos.CENTER);
        
        TextField shuttleIdField = new TextField();
        TextField routeNameField = new TextField();
        TextField currentLocationField = new TextField();
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("ACTIVE", "INACTIVE", "MAINTENANCE");
        statusBox.setValue("ACTIVE");
        
        shuttleForm.add(createFormLabel("Shuttle ID:"), 0, 0);
        shuttleForm.add(shuttleIdField, 1, 0);
        shuttleForm.add(createFormLabel("Route Name:"), 0, 1);
        shuttleForm.add(routeNameField, 1, 1);
        shuttleForm.add(createFormLabel("Current Location:"), 0, 2);
        shuttleForm.add(currentLocationField, 1, 2);
        shuttleForm.add(createFormLabel("Status:"), 0, 3);
        shuttleForm.add(statusBox, 1, 3);
        
        styleFormElements(shuttleIdField, routeNameField, currentLocationField, statusBox);
        formCard.getChildren().add(shuttleForm);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button addButton = createStyledButton("✓ Add Shuttle", "#00A86B");
        Button updateButton = createStyledButton("✎ Update Shuttle", "#FF8C00");
        Button deleteButton = createStyledButton("✕ Delete Shuttle", "#E53935");
        Button viewButton = createStyledButton("👁 View All Shuttles", "#1976D2");
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);
        
        VBox resultCard = createFormCard();
        Label resultLabel = new Label("Results");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-padding: 0 0 10 0;");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: #fafafa; -fx-control-inner-background: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        resultCard.getChildren().addAll(resultLabel, resultArea);
        
        // Button actions
        addButton.setOnAction(e -> {
            try {
                String shuttleId = shuttleManager.addShuttle(routeNameField.getText(), currentLocationField.getText(), statusBox.getValue());
                resultArea.setText("Shuttle added successfully with ID: " + shuttleId);
                clearShuttleFields(shuttleIdField, routeNameField, currentLocationField, statusBox);
            } catch (Exception ex) {
                resultArea.setText("Error adding shuttle: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                shuttleManager.updateShuttle(shuttleIdField.getText(), routeNameField.getText(), currentLocationField.getText(), statusBox.getValue());
                resultArea.setText("Shuttle updated successfully!");
            } catch (Exception ex) {
                resultArea.setText("Error updating shuttle: " + ex.getMessage());
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                shuttleManager.deleteShuttle(shuttleIdField.getText());
                resultArea.setText("Shuttle deleted successfully!");
                clearShuttleFields(shuttleIdField, routeNameField, currentLocationField, statusBox);
            } catch (Exception ex) {
                resultArea.setText("Error deleting shuttle: " + ex.getMessage());
            }
        });
        
        viewButton.setOnAction(e -> {
            try {
                Map<String, Map<String, Object>> shuttles = shuttleManager.getAllShuttles();
                StringBuilder sb = new StringBuilder("All Shuttles:\n\n");
                shuttles.forEach((id, data) -> {
                    sb.append("ID: ").append(id).append("\n");
                    sb.append("Route: ").append(data.get("routeName")).append("\n");
                    sb.append("Location: ").append(data.get("currentLocation")).append("\n");
                    sb.append("Status: ").append(data.get("status")).append("\n");
                    sb.append("Updated: ").append(data.get("lastUpdated")).append("\n");
                    sb.append("---\n");
                });
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                resultArea.setText("Error retrieving shuttles: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, formCard, buttonBox, resultCard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        tab.setContent(scrollPane);
        return tab;
    }
    
    private Tab createLoungeTab() {
        Tab tab = new Tab("🛋 Lounges");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Lounge Management");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        VBox formCard = createFormCard();
        
        GridPane loungeForm = new GridPane();
        loungeForm.setHgap(15);
        loungeForm.setVgap(15);
        loungeForm.setPadding(new Insets(20));
        loungeForm.setAlignment(Pos.CENTER);
        
        TextField loungeIdField = new TextField();
        TextField nameField = new TextField();
        TextField locationField = new TextField();
        TextField capacityField = new TextField();
        TextField currentOccupancyField = new TextField();
        
        loungeForm.add(createFormLabel("Lounge ID:"), 0, 0);
        loungeForm.add(loungeIdField, 1, 0);
        loungeForm.add(createFormLabel("Name:"), 0, 1);
        loungeForm.add(nameField, 1, 1);
        loungeForm.add(createFormLabel("Location:"), 0, 2);
        loungeForm.add(locationField, 1, 2);
        loungeForm.add(createFormLabel("Capacity:"), 0, 3);
        loungeForm.add(capacityField, 1, 3);
        loungeForm.add(createFormLabel("Current Occupancy:"), 0, 4);
        loungeForm.add(currentOccupancyField, 1, 4);
        
        styleFormElements(loungeIdField, nameField, locationField, capacityField, currentOccupancyField);
        formCard.getChildren().add(loungeForm);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        
        Button addButton = createStyledButton("✓ Add Lounge", "#00A86B");
        Button updateButton = createStyledButton("✎ Update Lounge", "#FF8C00");
        Button deleteButton = createStyledButton("✕ Delete Lounge", "#E53935");
        Button viewButton = createStyledButton("👁 View All Lounges", "#1976D2");
        
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton);
        
        VBox resultCard = createFormCard();
        Label resultLabel = new Label("Results");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #00563F; -fx-padding: 0 0 10 0;");
        
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-background-color: #fafafa; -fx-control-inner-background: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        
        resultCard.getChildren().addAll(resultLabel, resultArea);
        
        // Button actions
        addButton.setOnAction(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());
                int occupancy = Integer.parseInt(currentOccupancyField.getText());
                String loungeId = loungeManager.addLounge(nameField.getText(), locationField.getText(), capacity, occupancy);
                resultArea.setText("Lounge added successfully with ID: " + loungeId);
                clearLoungeFields(loungeIdField, nameField, locationField, capacityField, currentOccupancyField);
            } catch (Exception ex) {
                resultArea.setText("Error adding lounge: " + ex.getMessage());
            }
        });
        
        updateButton.setOnAction(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());
                int occupancy = Integer.parseInt(currentOccupancyField.getText());
                loungeManager.updateLounge(loungeIdField.getText(), nameField.getText(), locationField.getText(), capacity, occupancy);
                resultArea.setText("Lounge updated successfully!");
            } catch (Exception ex) {
                resultArea.setText("Error updating lounge: " + ex.getMessage());
            }
        });
        
        deleteButton.setOnAction(e -> {
            try {
                loungeManager.deleteLounge(loungeIdField.getText());
                resultArea.setText("Lounge deleted successfully!");
                clearLoungeFields(loungeIdField, nameField, locationField, capacityField, currentOccupancyField);
            } catch (Exception ex) {
                resultArea.setText("Error deleting lounge: " + ex.getMessage());
            }
        });
        
        viewButton.setOnAction(e -> {
            try {
                Map<String, Map<String, Object>> lounges = loungeManager.getAllLounges();
                StringBuilder sb = new StringBuilder("All Lounges:\n\n");
                lounges.forEach((id, data) -> {
                    sb.append("ID: ").append(id).append("\n");
                    sb.append("Name: ").append(data.get("name")).append("\n");
                    sb.append("Location: ").append(data.get("location")).append("\n");
                    sb.append("Capacity: ").append(data.get("capacity")).append("\n");
                    sb.append("Occupancy: ").append(data.get("currentOccupancy")).append("\n");
                    sb.append("Updated: ").append(data.get("lastUpdated")).append("\n");
                    sb.append("---\n");
                });
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                resultArea.setText("Error retrieving lounges: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, formCard, buttonBox, resultCard);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        tab.setContent(scrollPane);
        return tab;
    }
    
    private VBox createFormCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 12px; " +
                     "-fx-border-color: #e0e0e0; " +
                     "-fx-border-width: 1px; " +
                     "-fx-border-radius: 12px; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                     "-fx-padding: 25px;");
        return card;
    }
    
    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #424242; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        return label;
    }
    
    private void styleFormElements(Control... controls) {
        for (Control control : controls) {
            if (control instanceof TextField) {
                control.setStyle("-fx-background-color: #fafafa; " +
                               "-fx-border-color: #d0d0d0; " +
                               "-fx-border-width: 1.5px; " +
                               "-fx-border-radius: 8px; " +
                               "-fx-background-radius: 8px; " +
                               "-fx-padding: 10px; " +
                               "-fx-font-size: 13px; " +
                               "-fx-font-family: 'Segoe UI', Arial, sans-serif;");
                ((TextField) control).setPrefWidth(250);
                
                // Add focus effect
                control.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) {
                        control.setStyle(control.getStyle().replace("-fx-border-color: #d0d0d0;", "-fx-border-color: #00A86B;"));
                    } else {
                        control.setStyle(control.getStyle().replace("-fx-border-color: #00A86B;", "-fx-border-color: #d0d0d0;"));
                    }
                });
            } else if (control instanceof ComboBox) {
                control.setStyle("-fx-background-color: #fafafa; " +
                               "-fx-border-color: #d0d0d0; " +
                               "-fx-border-width: 1.5px; " +
                               "-fx-border-radius: 8px; " +
                               "-fx-background-radius: 8px; " +
                               "-fx-padding: 5px; " +
                               "-fx-font-size: 13px; " +
                               "-fx-font-family: 'Segoe UI', Arial, sans-serif;");
                ((ComboBox<?>) control).setPrefWidth(250);
            }
        }
    }
    
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        String baseStyle = "-fx-background-color: " + color + "; " +
                          "-fx-text-fill: white; " +
                          "-fx-font-weight: bold; " +
                          "-fx-font-size: 13px; " +
                          "-fx-padding: 12px 24px; " +
                          "-fx-border-radius: 8px; " +
                          "-fx-background-radius: 8px; " +
                          "-fx-cursor: hand; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2); " +
                          "-fx-font-family: 'Segoe UI', Arial, sans-serif;";
        
        button.setStyle(baseStyle);
        button.setPrefHeight(40);
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + "-fx-opacity: 0.9; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle);
        });
        
        // Press effect
        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle + "-fx-opacity: 0.8; -fx-scale-x: 0.98; -fx-scale-y: 0.98;");
        });
        
        button.setOnMouseReleased(e -> {
            button.setStyle(baseStyle + "-fx-opacity: 0.9;");
        });
        
        return button;
    }
    
    private void clearUserFields(TextField userId, TextField name, TextField email, ComboBox<String> role) {
        userId.clear();
        name.clear();
        email.clear();
        role.setValue("GENERAL_STUDENT");
    }
    
    private void clearClubFields(TextField clubId, TextField name, TextField description, TextField presidentId) {
        clubId.clear();
        name.clear();
        description.clear();
        presidentId.clear();
    }
    
    private void clearEventFields(TextField eventId, TextField name, TextField description, 
                                 TextField location, TextField dateTime, TextField organizerId) {
        eventId.clear();
        name.clear();
        description.clear();
        location.clear();
        dateTime.clear();
        organizerId.clear();
    }
    
    private void clearShuttleFields(TextField shuttleId, TextField routeName, TextField location, ComboBox<String> status) {
        shuttleId.clear();
        routeName.clear();
        location.clear();
        status.setValue("ACTIVE");
    }
    
    private void clearLoungeFields(TextField loungeId, TextField name, TextField location, 
                                  TextField capacity, TextField occupancy) {
        loungeId.clear();
        name.clear();
        location.clear();
        capacity.clear();
        occupancy.clear();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}