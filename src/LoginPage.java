import javafx.application.Application;
import javafx.concurrent.Task;
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
        Scene scene = getScene(stage, null);
        stage.setScene(scene);
        stage.setTitle("Ramify FSC Login");
        stage.show();
    }

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30, 24, 24, 24));
        root.getStyleClass().add("login-root");

        // ---------------- Logo ----------------
        ImageView logoView = null;
        try {
            Image logo = new Image("/ramify_logo.png");
            logoView = new ImageView(logo);
            logoView.setFitWidth(220);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load logo.");
        }

        // ---------------- Title ----------------
        Label titleLabel = new Label("Welcome to Ramify FSC");
        titleLabel.getStyleClass().add("login-title");

        // ---------------- Username & Password ----------------
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username or FSC Email");
        usernameField.getStyleClass().add("ramify-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("ramify-field");

        Hyperlink forgotPass = new Hyperlink("Forgot Password?");
        forgotPass.getStyleClass().add("forgot-link");
        forgotPass.setOnAction(e ->
                stage.setScene(ForgotPasswordPage.getScene(stage, stage.getScene()))
        );

        // ---------------- Log In Button ----------------
        Button loginBtn = new Button("Log In");
        loginBtn.setPrefWidth(220);
        loginBtn.getStyleClass().add("primary-btn");

        loginBtn.setOnAction(e -> {
            String email = usernameField.getText().trim();
            String pass = passwordField.getText().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        "Please enter username and password.",
                        ButtonType.OK).showAndWait();
                return;
            }

            loginBtn.setDisable(true);

            Task<Boolean> loginTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return FirebaseAuthService.login(email, pass);
                }
            };

            loginTask.setOnSucceeded(ev -> {
                loginBtn.setDisable(false);

                if (loginTask.getValue()) {
                    stage.setScene(DashboardPage.getScene(stage, stage.getScene()));
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Invalid email or password.",
                            ButtonType.OK).showAndWait();
                }
            });

            loginTask.setOnFailed(ev -> {
                loginBtn.setDisable(false);
                new Alert(Alert.AlertType.ERROR,
                        "Error contacting Firebase.",
                        ButtonType.OK).showAndWait();
            });

            new Thread(loginTask).start();
        });

        // ---------------- Register Button ----------------
        Button registerBtn = new Button("Create Account");
        registerBtn.setPrefWidth(220);
        registerBtn.getStyleClass().add("secondary-btn");
        registerBtn.setOnAction(e ->
                stage.setScene(RegisterPage.getScene(stage, stage.getScene()))
        );

        // ---------------- Google Sign-In ----------------
        ImageView googleIcon = null;
        try {
            Image img = new Image("/googleicon.png");
            googleIcon = new ImageView(img);
            googleIcon.setFitHeight(18);
            googleIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load Google icon.");
        }

        Button googleBtn = new Button("Sign in with Google");
        if (googleIcon != null) {
            googleBtn.setGraphic(googleIcon);
            googleBtn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        }
        googleBtn.setPrefWidth(220);
        googleBtn.getStyleClass().add("google-btn");
        googleBtn.setOnAction(e ->
                stage.setScene(GoogleLogin.getScene(stage, stage.getScene()))
        );

        HBox googleBox = new HBox(googleBtn);
        googleBox.setAlignment(Pos.CENTER);

        // ---------------- Layout groups ----------------
        VBox formBox = new VBox(10,
                usernameField,
                passwordField,
                forgotPass,
                loginBtn,
                registerBtn,
                googleBox
        );
        formBox.setAlignment(Pos.CENTER);

        if (logoView != null) root.getChildren().add(logoView);
        root.getChildren().addAll(titleLabel, formBox);

        Scene scene = new Scene(root, 420, 640);

        // Attach CSS
        scene.getStylesheets().add(
                LoginPage.class.getResource("/ramifylogin.css").toExternalForm()
        );

        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}