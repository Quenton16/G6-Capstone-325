import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardPage {

    // Helper: open URL inside the app using WebViewPage
    private static void openInApp(Stage stage, String url, String title) {
        Scene previous = stage.getScene();
        stage.setScene(WebViewPage.getScene(stage, previous, url, title));
    }

    public static Scene getScene(Stage stage, Scene previousScene) {

        // ============================================================
        // HEADER
        // ============================================================
        Button hamburgerBtn = new Button("☰");
        hamburgerBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 22px;
                -fx-text-fill: #0b5e2a;
                -fx-cursor: hand;
                """);

        Button backBtn = new Button("<");
        backBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 20px;
                -fx-text-fill: #555555;
                """);
        if (previousScene != null) {
            backBtn.setOnAction(e -> stage.setScene(previousScene));
        }

        HBox topLeftBox = new HBox(10, hamburgerBtn, backBtn);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.setPadding(new Insets(10));

        HBox topRightBox = new HBox(10);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        topRightBox.setPadding(new Insets(10));

        ImageView settingsIcon = null;
        try {
            settingsIcon = new ImageView(new Image("/settingsicon.png"));
            settingsIcon.setFitHeight(22);
            settingsIcon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Button settingsBtn = new Button();
        if (settingsIcon != null) settingsBtn.setGraphic(settingsIcon);
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        ImageView logoutIcon = null;
        try {
            logoutIcon = new ImageView(new Image("/logouticon.png"));
            logoutIcon.setFitHeight(22);
            logoutIcon.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Button logoutBtn = new Button();
        if (logoutIcon != null) logoutBtn.setGraphic(logoutIcon);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> stage.setScene(LoginPage.getScene(stage, stage.getScene())));

        topRightBox.getChildren().addAll(settingsBtn, logoutBtn);

        BorderPane header = new BorderPane();
        header.setLeft(topLeftBox);
        header.setRight(topRightBox);
        header.setPrefHeight(60);
        header.setStyle("""
                -fx-background-color: linear-gradient(to right, #ffffff, #f6faf7);
                -fx-border-color: #d6e2da;
                -fx-border-width: 0 0 1 0;
                """);

        // ============================================================
        // SIDEBAR
        // ============================================================
        VBox sideMenu = new VBox(20);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setAlignment(Pos.TOP_LEFT);
        sideMenu.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 16, 0.0, 4, 0);
                """);
        sideMenu.setPrefWidth(230);
        sideMenu.setVisible(false);

        Button closeMenuBtn = new Button("×");
        closeMenuBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 26px;
                -fx-text-fill: white;
                """);
        closeMenuBtn.setOnAction(e -> sideMenu.setVisible(false));

        Label academicCalendar = new Label("Academic Calendar");
        Label fscShuttle = new Label("FSC Shuttle");
        Label instagram = new Label("OSA Instagram");
        Label osaManual = new Label("OSA Manual");
        Label careers = new Label("Careers & Internships");
        Label releaseNotes = new Label("Release Notes");
        Label privacy = new Label("Privacy");
        Label heatmapLink = new Label("Campus Heatmap");  // NEW

        Label[] links = {
                academicCalendar, fscShuttle, instagram, osaManual,
                careers, releaseNotes, privacy, heatmapLink
        };

        for (Label lbl : links) {
            lbl.setStyle("""
                    -fx-font-size: 16px;
                    -fx-text-fill: white;
                    -fx-font-weight: normal;
                    """);
            lbl.setCursor(Cursor.HAND);
        }

        // Open these links INSIDE the app WebViewPage
        academicCalendar.setOnMouseClicked(e ->
                openInApp(stage, "https://www.farmingdale.edu/calendar/academic/", "Academic Calendar"));
        fscShuttle.setOnMouseClicked(e ->
                openInApp(stage, "https://www.farmingdale.edu/shuttle/", "FSC Shuttle"));
        instagram.setOnMouseClicked(e ->
                openInApp(stage, "https://www.instagram.com/farmingdalestudentactivities/", "OSA Instagram"));
        osaManual.setOnMouseClicked(e ->
                openInApp(stage, "https://www.farmingdale.edu/student-activities/", "OSA Manual"));
        careers.setOnMouseClicked(e ->
                openInApp(stage, "https://www.farmingdale.edu/nexus-center/", "Careers & Internships"));
        releaseNotes.setOnMouseClicked(e ->
                openInApp(stage, "https://www.farmingdale.edu", "Release Notes"));
        privacy.setOnMouseClicked(e ->
                openInApp(stage, "https://farmingdale.campuslabs.com/engage/privacy", "Privacy"));

        // NEW: open the JavaFX HeatmapPage screen
        heatmapLink.setOnMouseClicked(e ->
                stage.setScene(HeatmapPage.getScene(stage, stage.getScene()))
        );

        ImageView sideLogo = null;
        try {
            sideLogo = new ImageView(new Image("/logosidebar.jpg"));
            sideLogo.setFitWidth(150);
            sideLogo.setPreserveRatio(true);
        } catch (Exception ignored) {}

        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.BOTTOM_RIGHT);
        if (sideLogo != null) logoBox.getChildren().add(sideLogo);
        logoBox.setPadding(new Insets(20, 10, 10, 10));

        VBox.setVgrow(logoBox, Priority.ALWAYS);

        sideMenu.getChildren().addAll(
                closeMenuBtn,
                academicCalendar,
                fscShuttle,
                instagram,
                osaManual,
                careers,
                releaseNotes,
                privacy,
                heatmapLink,   // NEW item in sidebar
                logoBox
        );

        // ============================================================
        // MAIN CONTENT ROOT (background)
        // ============================================================
        VBox contentRoot = new VBox();
        contentRoot.setAlignment(Pos.TOP_CENTER);
        contentRoot.setPadding(new Insets(20));
        contentRoot.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #f1f6f2, #e3ede6);
                """);

        // Card that holds the actual dashboard widgets
        VBox dashboardCard = new VBox(20);
        dashboardCard.setAlignment(Pos.TOP_CENTER);
        dashboardCard.setPadding(new Insets(20));
        dashboardCard.setMaxWidth(420);
        dashboardCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 18;
                -fx-border-radius: 18;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 18, 0.0, 0, 4);
                """);

        // LOGO + WELCOME
        ImageView logoView = null;
        try {
            logoView = new ImageView(new Image("/ramify_logo.png"));
            logoView.setFitWidth(160);
            logoView.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label welcomeLabel = new Label("Welcome to Ramify FSC");
        welcomeLabel.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);

        Label subtitleLabel = new Label("Discover clubs, events, and campus resources in one place.");
        subtitleLabel.setWrapText(true);
        subtitleLabel.setStyle("""
                -fx-font-size: 12px;
                -fx-text-fill: #6e7b72;
                """);

        VBox headerBox = new VBox(6);
        headerBox.setAlignment(Pos.CENTER);
        if (logoView != null) headerBox.getChildren().add(logoView);
        headerBox.getChildren().addAll(welcomeLabel, subtitleLabel);

        // SEARCH
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10, 0, 0, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Search clubs, events, or resources");
        searchField.setPrefWidth(330);
        searchField.setStyle("""
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-padding: 8 15;
                -fx-background-color: #f5faf6;
                -fx-border-color: #c6d8cc;
                """);
        searchBox.getChildren().add(searchField);

        // UPCOMING EVENTS
        VBox upcomingCard = new VBox(10);
        upcomingCard.setAlignment(Pos.TOP_LEFT);
        upcomingCard.setPadding(new Insets(12));
        upcomingCard.setStyle("""
                -fx-background-color: #f7fff8;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #c3e1cf;
                -fx-border-width: 1;
                """);

        Label upcomingLabel = new Label("Upcoming Events");
        upcomingLabel.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-text-fill: #23422f;
                """);

        // Button to open heatmap from the main content as well
        Button heatmapBtn = new Button("Open Campus Heatmap");
        heatmapBtn.setStyle("-fx-background-color: #006633; -fx-text-fill: white;");
        heatmapBtn.setOnAction(e ->
                stage.setScene(HeatmapPage.getScene(stage, stage.getScene()))
        );

        ImageView flyerImage = new ImageView();
        try {
            flyerImage.setImage(new Image("/Ramchella.png"));
        } catch (Exception ignored) {}
        flyerImage.setFitWidth(360);
        flyerImage.setFitHeight(130);
        flyerImage.setPreserveRatio(false);

        Button flyerButton = new Button();
        flyerButton.setGraphic(flyerImage);
        flyerButton.setPrefSize(360, 130);
        flyerButton.setStyle("""
                -fx-background-color: transparent;
                -fx-border-color: #0b5e2a;
                -fx-border-width: 2;
                -fx-border-radius: 16;
                -fx-background-radius: 16;
        """);

        upcomingCard.getChildren().addAll(upcomingLabel, flyerButton, heatmapBtn);

        // CLUBS FOR YOU
        VBox clubsCard = new VBox(14);
        clubsCard.setAlignment(Pos.TOP_LEFT);
        clubsCard.setPadding(new Insets(12));
        clubsCard.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #dde6df;
                -fx-border-width: 1;
                """);

        HBox clubsHeader = new HBox();
        clubsHeader.setAlignment(Pos.CENTER_LEFT);
        clubsHeader.setSpacing(10);

        Label clubsLabel = new Label("Clubs For You");
        clubsLabel.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-text-fill: #23422f;
                """);

        Region clubsSpacer = new Region();
        HBox.setHgrow(clubsSpacer, Priority.ALWAYS);

        Button viewMoreBtn = new Button("View More →");
        viewMoreBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #0b5e2a;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """);

        clubsHeader.getChildren().addAll(clubsLabel, clubsSpacer, viewMoreBtn);

        HBox clubsRow = new HBox(18);
        clubsRow.setAlignment(Pos.CENTER);

        String[] clubImages = { "/cooksandcrookslogo.jpg", "/cricketclublogo.jpg", "/esportsicon.png" };
        String[] clubNames = { "Cooks 'N' Crooks", "Farmingdale Cricket Club", "Esports Club" };

        String[] clubUrls = {
                "https://farmingdale.campuslabs.com/engage/organization/cooks",
                "https://farmingdale.campuslabs.com/engage/organization/cricket",
                "https://farmingdale.campuslabs.com/engage/organization/farmingdale-esports"
        };

        for (int i = 0; i < 3; i++) {
            ImageView clubImage = new ImageView(new Image(clubImages[i]));
            clubImage.setFitWidth(70);
            clubImage.setFitHeight(70);

            Button clubBtn = new Button();
            clubBtn.setGraphic(clubImage);
            clubBtn.setPrefSize(80, 80);
            clubBtn.setStyle("""
                    -fx-background-color: #e6f7ea;
                    -fx-border-color: #0b5e2a;
                    -fx-border-width: 1.5;
                    -fx-background-radius: 40;
                    -fx-border-radius: 40;
            """);

            final String clubName = clubNames[i];
            final String clubUrl = clubUrls[i];

            clubBtn.setOnAction(e ->
                    openInApp(stage, clubUrl, clubName)
            );

            Button seeDetailsBtn = new Button("See Details");
            seeDetailsBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-text-fill: #0b5e2a;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    """);
            seeDetailsBtn.setOnAction(e ->
                    openInApp(stage, clubUrl, clubName)
            );

            Label clubNameLabel = new Label(clubNames[i]);
            clubNameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2d3a33;");

            VBox clubBox = new VBox(5, clubBtn, clubNameLabel, seeDetailsBtn);
            clubBox.setAlignment(Pos.CENTER);
            clubsRow.getChildren().add(clubBox);
        }

        clubsCard.getChildren().addAll(clubsHeader, clubsRow);

        // YOU MAY LIKE
        VBox likeCard = new VBox(14);
        likeCard.setAlignment(Pos.TOP_LEFT);
        likeCard.setPadding(new Insets(12));
        likeCard.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #dde6df;
                -fx-border-width: 1;
                """);

        Label likeLabel = new Label("You May Like");
        likeLabel.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-text-fill: #23422f;
                """);

        VBox likeList = new VBox(16);
        likeList.setAlignment(Pos.TOP_LEFT);

        String[] eventNames = {
                "Winter Tournament @ 3-6 PM on 12/1",
                "Chips 'N' Dip @ 11 AM - 12 PM on 12/2"
        };
        String[] eventClubs = {
                "Farmingdale Cricket Club",
                "Cooks 'N' Crooks"
        };
        String[] eventImages = {
                "/cricketclublogo.jpg",
                "/cooksandcrookslogo.jpg"
        };

        String[] eventUrls = {
                clubUrls[1],
                clubUrls[0]
        };

        for (int i = 0; i < 2; i++) {
            ImageView eventImg = new ImageView(new Image(eventImages[i]));
            eventImg.setFitWidth(55);
            eventImg.setFitHeight(55);

            Label eventLabel = new Label(eventNames[i]);
            eventLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #26332b;");

            Label clubLabel = new Label(eventClubs[i]);
            clubLabel.setStyle("-fx-text-fill: #5f6e64; -fx-font-size: 11px;");

            final String eventUrl = eventUrls[i];
            final String eventTitle = eventNames[i];

            Button detailBtn = new Button("> See Details");
            detailBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-text-fill: #0b5e2a;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    """);
            detailBtn.setOnAction(e ->
                    openInApp(stage, eventUrl, eventTitle)
            );

            VBox textCol = new VBox(4, eventLabel, clubLabel, detailBtn);
            textCol.setAlignment(Pos.CENTER_LEFT);

            HBox eventRow = new HBox(12, eventImg, textCol);
            eventRow.setAlignment(Pos.CENTER_LEFT);
            eventRow.setStyle("""
                    -fx-background-color: #f7faf8;
                    -fx-background-radius: 10;
                    -fx-padding: 8 10 8 10;
                    """);

            likeList.getChildren().add(eventRow);
        }

        likeCard.getChildren().addAll(likeLabel, likeList);

        // COMPOSE DASHBOARD CARD
        dashboardCard.getChildren().addAll(
                headerBox,
                searchBox,
                upcomingCard,
                clubsCard,
                likeCard
        );

        contentRoot.getChildren().add(dashboardCard);

        ScrollPane scrollPane = new ScrollPane(contentRoot);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // OVERLAY + SIDEMENU TOGGLE
        Pane overlay = new Pane();
        overlay.setVisible(false);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.25);");
        overlay.setOnMouseClicked(e -> {
            sideMenu.setVisible(false);
            overlay.setVisible(false);
        });

        hamburgerBtn.setOnAction(e -> {
            boolean showing = !sideMenu.isVisible();
            sideMenu.setVisible(showing);
            overlay.setVisible(showing);
        });

        VBox mainLayout = new VBox(header, scrollPane);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(mainLayout, overlay, sideMenu);
        StackPane.setAlignment(sideMenu, Pos.TOP_LEFT);

        Scene scene = new Scene(stack, 450, 650);
        return scene;
    }
}