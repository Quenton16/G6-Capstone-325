import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {
    public void start(Stage stage) {

        // Main vertical layout
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Load logo image
        Image logoImage = new Image("/ramify_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(300);
        logoView.setPreserveRatio(true);

        // Title text
        Label title = new Label("Welcome to Ramify FSC");
        Label subtitle = new Label("Please login using your FSC credentials");

        // Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username or FSC Email");

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Forgot password link
        Hyperlink forgotPass = new Hyperlink("Forgot Password?");

        // Login button
        Button loginBtn = new Button("Log In");

        // Register button
        Button registerBtn = new Button("Register");

        // Google sign-in message
        HBox googleBox = new HBox(5);
        googleBox.setAlignment(Pos.CENTER);
        Label noAccount = new Label("Or sign in with Google:");
        Hyperlink googleSign = new Hyperlink("Sign in with Google");
        googleBox.getChildren().addAll(noAccount, googleSign);

        // Add everything to layout (logo at top)
        root.getChildren().addAll(
                logoView,
                title,
                subtitle,
                usernameField,
                passwordField,
                forgotPass,
                loginBtn,
                registerBtn,
                googleBox
        );

        // Optional: handle button actions (example)
        loginBtn.setOnAction(e -> System.out.println("Login button clicked"));
        registerBtn.setOnAction(e -> System.out.println("Register button clicked"));
        googleSign.setOnAction(e -> System.out.println("Google Sign-In clicked"));

        // Create scene and show window
        Scene scene = new Scene(root, 350, 500);
        stage.setTitle("Ramify FSC Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
