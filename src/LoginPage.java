import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {

    @Override
    public void start(Stage stage) {
        // Main login page has no previous page, pass null
        stage.setScene(getScene(stage, null));
        stage.setTitle("Ramify FSC Login");
        stage.show();
    }

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        // ---------------- Logo ----------------
        ImageView logoView = null;
        try {
            Image logo = new Image("/ramify_logo.png");
            logoView = new ImageView(logo);
            logoView.setFitWidth(280);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load logo.");
        }

        // ---------------- Title ----------------
        Label titleLabel = new Label("Welcome to Ramify FSC");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ---------------- Username & Password ----------------
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username or FSC Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Hyperlink forgotPass = new Hyperlink("Forgot Password?");
        forgotPass.setOnAction(e -> stage.setScene(ForgotPasswordPage.getScene(stage, stage.getScene())));

        // ---------------- Log In Button ----------------
        Button loginBtn = new Button("Log In");
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-background-color: #006633; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter username and password.", ButtonType.OK);
                alert.showAndWait();
            } else {
                stage.setScene(DashboardPage.getScene(stage, stage.getScene()));
            }
        });

        // ---------------- Register Button ----------------
        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);
        registerBtn.setStyle("-fx-background-color: #004d26; -fx-text-fill: white;");
        registerBtn.setOnAction(e -> stage.setScene(RegisterPage.getScene(stage, stage.getScene())));

        // ---------------- Google Sign-In ----------------
        ImageView googleIcon = null;
        try {
            Image img = new Image("/googleicon.png");
            googleIcon = new ImageView(img);
            googleIcon.setFitHeight(20);
            googleIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load Google icon.");
        }

        Button googleBtn = new Button("Sign in with Google");
        if (googleIcon != null) {
            googleBtn.setGraphic(googleIcon);
            googleBtn.setContentDisplay(ContentDisplay.LEFT);
        }
        googleBtn.setStyle("-fx-background-color: white; -fx-border-color: #dcdcdc; -fx-text-fill: #4285F4; -fx-font-weight: bold;");
        googleBtn.setOnAction(e -> stage.setScene(GoogleLogin.getScene(stage, stage.getScene())));

        HBox googleBox = new HBox(googleBtn);
        googleBox.setAlignment(Pos.CENTER);

        // ---------------- Add all elements ----------------
        if (logoView != null) root.getChildren().add(logoView);

        root.getChildren().addAll(titleLabel, usernameField, passwordField, forgotPass, loginBtn, registerBtn, googleBox);

        return new Scene(root, 380, 580);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

