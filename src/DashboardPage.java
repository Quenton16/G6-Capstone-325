import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardPage {

    public static Scene getScene(Stage stage, Scene previousScene) {

        // ============================================================
        // HEADER (fixed at top)
        // ============================================================
        Button hamburgerBtn = new Button("☰");
        hamburgerBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-cursor: hand;");

        Button backBtn = new Button("<");
        backBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 25px;");
        if (previousScene != null) backBtn.setOnAction(e -> stage.setScene(previousScene));

        HBox topLeftBox = new HBox(10, hamburgerBtn, backBtn);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.setPadding(new Insets(10));

        HBox topRightBox = new HBox(10);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        topRightBox.setPadding(new Insets(10));

        ImageView settingsIcon = null;
        try {
            settingsIcon = new ImageView(new Image("/settingsicon.png"));
            settingsIcon.setFitHeight(25);
            settingsIcon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Button settingsBtn = new Button();
        if (settingsIcon != null) settingsBtn.setGraphic(settingsIcon);
        settingsBtn.setStyle("-fx-background-color: transparent;");

        ImageView logoutIcon = null;
        try {
            logoutIcon = new ImageView(new Image("/logouticon.png"));
            logoutIcon.setFitHeight(25);
            logoutIcon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Button logoutBtn = new Button();
        if (logoutIcon != null) logoutBtn.setGraphic(logoutIcon);
        logoutBtn.setStyle("-fx-background-color: transparent;");
        logoutBtn.setOnAction(e -> stage.setScene(LoginPage.getScene(stage, stage.getScene())));

        topRightBox.getChildren().addAll(settingsBtn, logoutBtn);

        BorderPane header = new BorderPane();
        header.setLeft(topLeftBox);
        header.setRight(topRightBox);
        header.setPrefHeight(60);
        header.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-width: 0 0 1 0;");

        // ============================================================
        // SIDE MENU (overlay)
        // ============================================================
        VBox sideMenu = new VBox(15);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setAlignment(Pos.TOP_LEFT);
        sideMenu.setStyle("-fx-background-color: #e8e8e8;");
        sideMenu.setPrefWidth(200);
        sideMenu.setVisible(false);

        // Add close button at top
        Button closeMenuBtn = new Button("×");
        closeMenuBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 25px;");
        closeMenuBtn.setOnAction(e -> sideMenu.setVisible(false));
        sideMenu.getChildren().add(closeMenuBtn);

        Button menu1 = new Button("Home");
        Button menu2 = new Button("Profile");
        Button menu3 = new Button("Shuttle");
        Button menu4 = new Button("Settings");
        Button menu5 = new Button("Logout");

        for (Button b : new Button[]{menu1, menu2, menu3, menu4, menu5}) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");
        }
        sideMenu.getChildren().addAll(menu1, menu2, menu3, menu4, menu5);

        // ============================================================
        // DASHBOARD CONTENT
        // ============================================================
        VBox contentRoot = new VBox(20);
        contentRoot.setAlignment(Pos.TOP_CENTER);
        contentRoot.setPadding(new Insets(20));

        // Logo + Welcome
        ImageView logoView = null;
        try {
            logoView = new ImageView(new Image("/ramify_logo.png"));
            logoView.setFitWidth(200);
            logoView.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label welcomeLabel = new Label("Welcome to Ramify FSC");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        if (logoView != null) contentRoot.getChildren().add(logoView);
        contentRoot.getChildren().add(welcomeLabel);

        // Search bar
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 20; -fx-padding: 8 15;");
        searchBox.getChildren().add(searchField);

        // Upcoming Events
        Label upcomingLabel = new Label("Upcoming Events");
        upcomingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ImageView flyerImage = new ImageView();
        try { flyerImage.setImage(new Image("/Ramchella.png")); } catch (Exception ignored) {}
        flyerImage.setFitWidth(380);
        flyerImage.setFitHeight(120);
        flyerImage.setPreserveRatio(false);

        Button flyerButton = new Button();
        flyerButton.setGraphic(flyerImage);
        flyerButton.setPrefSize(380, 120);
        flyerButton.setStyle("""
                -fx-background-color: transparent;
                -fx-border-color: #0b5e2a;
                -fx-border-width: 3;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
        """);

        VBox upcomingBox = new VBox(10, upcomingLabel, flyerButton);
        upcomingBox.setAlignment(Pos.CENTER);

        // Clubs Section
        HBox clubsHeader = new HBox();
        clubsHeader.setAlignment(Pos.CENTER);
        clubsHeader.setSpacing(160);

        Label clubsLabel = new Label("Clubs For You");
        clubsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button viewMoreBtn = new Button("View More →");
        viewMoreBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");

        clubsHeader.getChildren().addAll(clubsLabel, viewMoreBtn);

        HBox clubsRow = new HBox(25);
        clubsRow.setAlignment(Pos.CENTER);

        String[] clubImages = { "/cooksandcrookslogo.jpg", "/cricketclublogo.jpg", "/esportsicon.png" };
        String[] clubNames = { "Cooks 'N' Crooks", "Farmingdale Cricket Club", "Esports Club" };

        for (int i = 0; i < 3; i++) {
            ImageView clubImage = new ImageView(new Image(clubImages[i]));
            clubImage.setFitWidth(80);
            clubImage.setFitHeight(80);

            Button clubBtn = new Button();
            clubBtn.setGraphic(clubImage);
            clubBtn.setPrefSize(90, 90);
            clubBtn.setStyle("""
                    -fx-background-color: #ccffcc;
                    -fx-border-color: #0b5e2a;
                    -fx-border-width: 2;
                    -fx-background-radius: 45;
                    -fx-border-radius: 45;
            """);

            VBox clubBox = new VBox(5, clubBtn, new Label(clubNames[i]), new Button("See Details"));
            clubBox.setAlignment(Pos.CENTER);
            ((Button) clubBox.getChildren().get(2)).setStyle("-fx-background-color: transparent; -fx-text-fill: green;");

            clubsRow.getChildren().add(clubBox);
        }

        VBox clubsSection = new VBox(10, clubsHeader, clubsRow);
        clubsSection.setAlignment(Pos.CENTER);

        // You May Like Section
        Label likeLabel = new Label("You May Like");
        likeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox likeList = new VBox(15);
        likeList.setAlignment(Pos.CENTER);

        String[] eventNames = { "Winter Tournament @ 3-6 PM on 12/1", "Chips 'N' Dip @ 11 AM - 12 PM on 12/2" };
        String[] eventClubs = { "Farmingdale Cricket Club", "Cooks 'N' Crooks" };
        String[] eventImages = { "/cricketclublogo.jpg", "/cooksandcrookslogo.jpg" };

        for (int i = 0; i < 2; i++) {
            ImageView eventImg = new ImageView(new Image(eventImages[i]));
            eventImg.setFitWidth(70);
            eventImg.setFitHeight(70);

            Label eventLabel = new Label(eventNames[i]);
            eventLabel.setStyle("-fx-font-weight: bold;");
            Label clubLabel = new Label(eventClubs[i]);

            Button detailBtn = new Button("> See Details");
            detailBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");

            VBox textCol = new VBox(5, eventLabel, clubLabel, detailBtn);
            textCol.setAlignment(Pos.CENTER_LEFT);

            HBox eventRow = new HBox(15, eventImg, textCol);
            eventRow.setAlignment(Pos.CENTER);

            likeList.getChildren().add(eventRow);
        }

        VBox dashboardSection = new VBox(20, searchBox, upcomingBox, clubsSection, likeLabel, likeList);
        dashboardSection.setAlignment(Pos.CENTER);
        contentRoot.getChildren().add(dashboardSection);

        // ============================================================
        // SCROLLABLE CONTENT
        // ============================================================
        ScrollPane scrollPane = new ScrollPane(contentRoot);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ============================================================
        // TRANSPARENT OVERLAY FOR CLICK-TO-CLOSE SIDE MENU
        // ============================================================
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.0);"); // fully transparent
        overlay.setVisible(false);
        overlay.setPickOnBounds(true);
        overlay.setOnMouseClicked(e -> {
            sideMenu.setVisible(false);
            overlay.setVisible(false);
        });

        hamburgerBtn.setOnAction(e -> {
            boolean showing = !sideMenu.isVisible();
            sideMenu.setVisible(showing);
            overlay.setVisible(showing);
        });

        // ============================================================
        // FINAL STACK
        // ============================================================
        StackPane stack = new StackPane();
        VBox mainLayout = new VBox(header, scrollPane);
        stack.getChildren().addAll(mainLayout, overlay, sideMenu);
        StackPane.setAlignment(sideMenu, Pos.CENTER_LEFT);

        return new Scene(stack, 450, 650);
    }
}
