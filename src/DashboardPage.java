import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardPage {

    private static void openLink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scene getScene(Stage stage, Scene previousScene) {

        // ============================================================
        // HEADER
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
        // SIDEBAR
        // ============================================================
        VBox sideMenu = new VBox(20);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setAlignment(Pos.TOP_LEFT);
        sideMenu.setStyle("-fx-background-color: #0b5e2a;");
        sideMenu.setPrefWidth(200);
        sideMenu.setVisible(false);

        Button closeMenuBtn = new Button("×");
        closeMenuBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 28px; -fx-text-fill: white;");
        closeMenuBtn.setOnAction(e -> sideMenu.setVisible(false));

        Label academicCalendar = new Label("Academic Calendar");
        Label fscShuttle = new Label("FSC Shuttle");
        Label instagram = new Label("OSA Instagram");
        Label osaManual = new Label("OSA Manual");
        Label careers = new Label("Careers & Internships");
        Label releaseNotes = new Label("Release Notes");
        Label privacy = new Label("Privacy");

        Label[] links = {
                academicCalendar, fscShuttle, instagram, osaManual,
                careers, releaseNotes, privacy
        };

        for (Label lbl : links) {
            lbl.setStyle("-fx-font-size: 17px; -fx-text-fill: white;");
            lbl.setCursor(Cursor.HAND);
        }

        academicCalendar.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu/calendar/academic/"));
        fscShuttle.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu/shuttle/"));
        instagram.setOnMouseClicked(e -> openLink("https://www.instagram.com/farmingdalestudentactivities/"));
        osaManual.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu/student-activities/"));
        careers.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu/nexus-center/"));
        releaseNotes.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu"));
        privacy.setOnMouseClicked(e -> openLink("https://www.farmingdale.edu"));

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
                logoBox
        );

        // ============================================================
        // MAIN CONTENT
        // ============================================================
        VBox contentRoot = new VBox(20);
        contentRoot.setAlignment(Pos.TOP_CENTER);
        contentRoot.setPadding(new Insets(20));

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

        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 20; -fx-padding: 8 15;");
        searchBox.getChildren().add(searchField);

        Label upcomingLabel = new Label("Upcoming Events");
        upcomingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ImageView flyerImage = new ImageView();
        try { flyerImage.setImage(new Image("/Ramchella.png")); } catch (Exception ignored) {}
        flyerImage.setFitWidth(380);
        flyerImage.setFitHeight(120);

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

        // ============================================================
        // CLUBS FOR YOU
        // ============================================================
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

        // ============================================================
        // YOU MAY LIKE
        // ============================================================
        Label likeLabel = new Label("You May Like");
        likeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox likeList = new VBox(20);
        likeList.setAlignment(Pos.TOP_LEFT);
        likeList.setPadding(new Insets(0, 0, 0, 20));

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
            eventRow.setAlignment(Pos.CENTER_LEFT);

            likeList.getChildren().add(eventRow);
        }

        VBox dashboardSection = new VBox(20,
                searchBox, upcomingBox, clubsSection,
                likeLabel, likeList
        );

        dashboardSection.setAlignment(Pos.TOP_LEFT);

        contentRoot.getChildren().add(dashboardSection);

        ScrollPane scrollPane = new ScrollPane(contentRoot);
        scrollPane.setFitToWidth(true);

        Pane overlay = new Pane();
        overlay.setVisible(false);
        overlay.setOnMouseClicked(e -> {
            sideMenu.setVisible(false);
            overlay.setVisible(false);
        });

        hamburgerBtn.setOnAction(e -> {
            boolean showing = !sideMenu.isVisible();
            sideMenu.setVisible(showing);
            overlay.setVisible(showing);
        });

        Scene scene = new Scene(new StackPane(), 450, 650);

        StackPane stack = new StackPane();
        VBox mainLayout = new VBox(header, scrollPane);
        stack.getChildren().addAll(mainLayout, overlay, sideMenu);

        StackPane.setAlignment(sideMenu, Pos.TOP_LEFT);

        scene.setRoot(stack);
        return scene;
    }
}
