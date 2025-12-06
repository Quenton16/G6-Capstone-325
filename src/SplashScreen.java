import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen extends Application {

    @Override
    public void start(Stage stage) {
        // ----- Logo -----
        ImageView logoView = new ImageView();
        try {
            // same logo path you already use
            Image logo = new Image("/ramify_logo.png");
            logoView.setImage(logo);
            logoView.setFitWidth(240);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load logo for splash.");
        }
        logoView.getStyleClass().add("splash-logo");

        // ----- Title / Subtitle -----
        Label title = new Label("RAMIFY");
        title.getStyleClass().add("splash-title");

        Label subtitle = new Label("Farmingdale State College");
        subtitle.getStyleClass().add("splash-subtitle");

        VBox content = new VBox(10, logoView, title, subtitle);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("splash-content");

        StackPane root = new StackPane(content);
        root.getStyleClass().add("splash-root");

        Scene scene = new Scene(root, 450, 650);
        scene.getStylesheets().add(
                getClass().getResource("/splash.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Ramify FSC");
        stage.show();

        // ----- Animations -----
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), content);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(1500), content);
        scaleIn.setFromX(0.85);
        scaleIn.setFromY(0.85);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        ParallelTransition intro = new ParallelTransition(fadeIn, scaleIn);
        intro.setOnFinished(e -> {
            // Short pause then go to LoginPage
            FadeTransition fadeOut = new FadeTransition(Duration.millis(600), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev ->
                    stage.setScene(LoginPage.getScene(stage, null))
            );
            fadeOut.play();
        });

        intro.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
