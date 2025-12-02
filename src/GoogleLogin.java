import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GoogleLogin {

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        // ---------------- Back Button ----------------
        Button backBtn = new Button("< Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        HBox topBox = new HBox(backBtn);
        topBox.setAlignment(Pos.TOP_LEFT);

        // ---------------- Google Icon ----------------
        ImageView googleIcon = null;
        try {
            Image img = new Image("/googleicon.png");
            googleIcon = new ImageView(img);
            googleIcon.setFitHeight(50);
            googleIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load Google icon.");
        }

        // ---------------- Page Title ----------------
        Label title = new Label("Sign in with Google");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ---------------- Email & Password ----------------
        TextField email = new TextField();
        email.setPromptText("Email");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");

        // ---------------- Sign In Button ----------------
        Button signInBtn = new Button("Sign In");
        signInBtn.setPrefWidth(200);
        signInBtn.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white; -fx-font-weight: bold;");
        signInBtn.setOnAction(e -> stage.setScene(DashboardPage.getScene(stage, stage.getScene())));

        // ---------------- Add elements ----------------
        root.getChildren().add(topBox);
        if (googleIcon != null) root.getChildren().add(googleIcon);
        root.getChildren().addAll(title, email, pass, signInBtn);

        return new Scene(root, 330, 300);
    }
}
