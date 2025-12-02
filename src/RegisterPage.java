import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        TextField usernameField = new TextField();
        usernameField.setPromptText("Email");

        TextField emailField = new TextField();
        emailField.setPromptText("Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Confirm Password");

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);
        registerBtn.setStyle("-fx-background-color: #004d26; -fx-text-fill: white;");
        registerBtn.setOnAction(e -> {
            // For now just return to previous scene
            stage.setScene(previousScene);
        });

        root.getChildren().addAll(topBox, title, usernameField, emailField, passwordField, registerBtn);
        return new Scene(root, 380, 500);
    }
}
