import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterPage {

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        // ---------------- Back Button ----------------
        Button backBtn = new Button("<");
        backBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        HBox topBox = new HBox(backBtn);
        topBox.setAlignment(Pos.TOP_LEFT);

        // ---------------- Page Title ----------------
        Label title = new Label("Register New Account");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ---------------- Fields ----------------
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm Password");

        // ---------------- Register Button ----------------
        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);
        registerBtn.setStyle("-fx-background-color: #004d26; -fx-text-fill: white;");

        registerBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String confirm = confirmField.getText().trim();

            // Validation
            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        "Please fill in all fields.", ButtonType.OK).showAndWait();
                return;
            }

            if (!password.equals(confirm)) {
                new Alert(Alert.AlertType.ERROR,
                        "Passwords do not match.", ButtonType.OK).showAndWait();
                return;
            }

            if (password.length() < 6) {
                new Alert(Alert.AlertType.ERROR,
                        "Password must be at least 6 characters.", ButtonType.OK).showAndWait();
                return;
            }

            registerBtn.setDisable(true);

            // Background task for Firebase call
            Task<Boolean> registerTask = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return FirebaseAuthService.register(email, password);
                }
            };

            registerTask.setOnSucceeded(ev -> {
                registerBtn.setDisable(false);

                if (registerTask.getValue()) {
                    new Alert(Alert.AlertType.INFORMATION,
                            "Account created successfully!\nYou can now log in.",
                            ButtonType.OK).showAndWait();
                    stage.setScene(previousScene);
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Registration failed.\nEmail may already be in use.",
                            ButtonType.OK).showAndWait();
                }
            });

            registerTask.setOnFailed(ev -> {
                registerBtn.setDisable(false);
                new Alert(Alert.AlertType.ERROR,
                        "Error contacting Firebase.",
                        ButtonType.OK).showAndWait();
            });

            new Thread(registerTask).start();
        });

        root.getChildren().addAll(
                topBox,
                title,
                emailField,
                passwordField,
                confirmField,
                registerBtn
        );

        return new Scene(root, 380, 500);
    }
}