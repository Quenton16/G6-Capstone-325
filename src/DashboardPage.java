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
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        // ---------------- Back Button ----------------
        Button backBtn = new Button("< Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px;");
        if (previousScene != null) backBtn.setOnAction(e -> stage.setScene(previousScene));

        // ---------------- Top-right buttons container ----------------
        HBox topRightBox = new HBox(10);
        topRightBox.setAlignment(Pos.TOP_RIGHT);

        // ---------------- Settings Icon ----------------
        ImageView settingsIcon = null;
        try {
            Image img = new Image("/settingsicon.png");
            settingsIcon = new ImageView(img);
            settingsIcon.setFitHeight(25);
            settingsIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load settings icon.");
        }

        Button settingsBtn = new Button();
        if (settingsIcon != null) settingsBtn.setGraphic(settingsIcon);
        settingsBtn.setStyle("-fx-background-color: transparent;");

        // ---------------- Logout Icon ----------------
        ImageView logoutIcon = null;
        try {
            Image img = new Image("/logouticon.png");
            logoutIcon = new ImageView(img);
            logoutIcon.setFitHeight(25);
            logoutIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load logout icon.");
        }

        Button logoutBtn = new Button();
        if (logoutIcon != null) logoutBtn.setGraphic(logoutIcon);
        logoutBtn.setStyle("-fx-background-color: transparent;");
        logoutBtn.setOnAction(e -> stage.setScene(LoginPage.getScene(stage, stage.getScene())));

        topRightBox.getChildren().addAll(settingsBtn, logoutBtn);

        // ---------------- Header ----------------
        StackPane header = new StackPane();
        header.setPrefHeight(60);

        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(backBox, Pos.TOP_LEFT);

        StackPane.setAlignment(topRightBox, Pos.TOP_RIGHT);
        header.getChildren().addAll(backBox, topRightBox);

        // ---------------- Logo ----------------
        ImageView logoView = null;
        try {
            Image logo = new Image("/ramify_logo.png");
            logoView = new ImageView(logo);
            logoView.setFitWidth(200);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load logo.");
        }

        Label welcomeLabel = new Label("Welcome to Ramify FSC");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        root.getChildren().add(header);
        if (logoView != null) root.getChildren().add(logoView);
        root.getChildren().add(welcomeLabel);

        // ================= DASHBOARD =================

        // ---------------- Search Bar ----------------
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 20; -fx-padding: 8 15;");
        searchBox.getChildren().add(searchField);

        // ---------------- Upcoming Events (IMAGE) ----------------
        Label upcomingLabel = new Label("Upcoming Events");
        upcomingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ImageView flyerImage = new ImageView();
        try {
            flyerImage.setImage(new Image("/Ramchella.png"));
        } catch (Exception e) {
            System.out.println("Could not load flyer image.");
        }

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

        flyerButton.setOnAction(e ->
                System.out.println("Ramchella flyer clicked")
        );

        VBox upcomingBox = new VBox(10, upcomingLabel, flyerButton);
        upcomingBox.setAlignment(Pos.CENTER);

        // ---------------- Clubs For You ----------------
        HBox clubsHeader = new HBox();
        Label clubsLabel = new Label("Clubs For You");
        clubsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button viewMoreBtn = new Button("View More →");
        viewMoreBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");
        clubsHeader.getChildren().addAll(clubsLabel, viewMoreBtn);
        clubsHeader.setSpacing(160);

        HBox clubsRow = new HBox(25);
        clubsRow.setAlignment(Pos.CENTER);

        String[] clubImages = {
                "/cooksandcrookslogo.jpg",
                "/cricketclublogo.jpg",
                "/esportsicon.png"
        };

        String[] clubNames = {
                "Cooks & Crooks",
                "Cricket Club",
                "Esports Club"
        };

        for (int i = 0; i < 3; i++) {
            ImageView clubImage = new ImageView(new Image(clubImages[i]));
            clubImage.setFitWidth(80);
            clubImage.setFitHeight(80);
            clubImage.setPreserveRatio(true);

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

            int index = i;
            clubBtn.setOnAction(e -> System.out.println(clubNames[index] + " clicked"));

            VBox clubBox = new VBox(5);
            clubBox.setAlignment(Pos.CENTER);

            Label nameLabel = new Label(clubNames[i]);
            Button detailsBtn = new Button("See Details");
            detailsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");

            clubBox.getChildren().addAll(clubBtn, nameLabel, detailsBtn);
            clubsRow.getChildren().add(clubBox);
        }

        VBox clubsSection = new VBox(10, clubsHeader, clubsRow);

        // ---------------- YOU MAY LIKE ----------------
        Label likeLabel = new Label("You May Like");
        likeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox likeList = new VBox(15);

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
            HBox eventRow = new HBox(15);

            ImageView eventImg = new ImageView(new Image(eventImages[i]));
            eventImg.setFitWidth(70);
            eventImg.setFitHeight(70);
            eventImg.setPreserveRatio(true);

            VBox eventInfo = new VBox(5);
            Label eventName = new Label(eventNames[i]);
            eventName.setStyle("-fx-font-weight: bold;");
            Label clubName = new Label(eventClubs[i]);

            Button detailsBtn = new Button("> See Details");
            detailsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: green;");
            int index = i;
            detailsBtn.setOnAction(e ->
                    System.out.println(eventNames[index] + " clicked")
            );

            eventInfo.getChildren().addAll(eventName, clubName, detailsBtn);
            eventRow.getChildren().addAll(eventImg, eventInfo);
            likeList.getChildren().add(eventRow);
        }

        VBox dashboardSection = new VBox(20);
        dashboardSection.getChildren().addAll(
                searchBox,
                upcomingBox,
                clubsSection,
                likeLabel,
                likeList
        );

        root.getChildren().add(dashboardSection);

        return new Scene(root, 450, 650);
    }
}
