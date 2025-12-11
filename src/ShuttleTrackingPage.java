import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;
import java.util.Map;

public class ShuttleTrackingPage {

    private static ShuttleManager shuttleManager = new ShuttleManager();

    public static Scene getScene(Stage stage, Scene previousScene) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f1f6f2, #e3ede6);");

        // Back Button
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("""
                -fx-background-color: #00563F;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-padding: 8 16;
                -fx-background-radius: 8;
                """);
        backBtn.setOnAction(e -> stage.setScene(previousScene));

        Label titleLabel = new Label("🚌 FSC Shuttle Schedule");
        titleLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #0b5e2a;
                """);

        Label subtitleLabel = new Label("Real-time departure times from campus locations");
        subtitleLabel.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #666;
                -fx-font-style: italic;
                """);

        VBox scheduleContainer = new VBox(15);
        scheduleContainer.setAlignment(Pos.TOP_CENTER);
        scheduleContainer.setMaxWidth(420);

        // Load all shuttles
        Map<String, Map<String, Object>> allShuttles = shuttleManager.getAllShuttles();

        if (allShuttles.isEmpty()) {
            Label noShuttlesLabel = new Label("No shuttle schedules available at this time.");
            noShuttlesLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-font-style: italic;");
            scheduleContainer.getChildren().add(noShuttlesLabel);
        } else {
            // Create schedule table card
            VBox tableCard = new VBox(12);
            tableCard.setAlignment(Pos.TOP_LEFT);
            tableCard.setPadding(new Insets(15));
            tableCard.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 12;
                    -fx-border-radius: 12;
                    -fx-border-color: #c3e1cf;
                    -fx-border-width: 1;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.0, 0, 2);
                    """);

            // Table Header
            GridPane headerGrid = new GridPane();
            headerGrid.setHgap(15);
            headerGrid.setVgap(8);
            headerGrid.setStyle("-fx-background-color: #0b5e2a; -fx-padding: 10; -fx-background-radius: 8;");

            // Set equal column widths
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(33.33);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(33.33);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(33.33);
            headerGrid.getColumnConstraints().addAll(col1, col2, col3);

            Label header1 = new Label("DEPARTS\nFARMINGDALE LIRR");
            Label header2 = new Label("DEPARTS\nLAFFIN HALL");
            Label header3 = new Label("DEPARTS\nAVIATION CENTER");

            for (Label header : new Label[]{header1, header2, header3}) {
                header.setStyle("""
                        -fx-text-fill: white;
                        -fx-font-size: 11px;
                        -fx-font-weight: bold;
                        -fx-text-alignment: center;
                        """);
                header.setWrapText(true);
                header.setMaxWidth(Double.MAX_VALUE);
                header.setAlignment(Pos.CENTER);
            }

            headerGrid.add(header1, 0, 0);
            headerGrid.add(header2, 1, 0);
            headerGrid.add(header3, 2, 0);

            tableCard.getChildren().add(headerGrid);

            // Table Rows
            for (Map.Entry<String, Map<String, Object>> entry : allShuttles.entrySet()) {
                Map<String, Object> shuttleData = entry.getValue();

                String departsFarmingdale = (String) shuttleData.getOrDefault("departsFarmingdale", "—");
                String departsLaffinHall = (String) shuttleData.getOrDefault("departsLaffinHall", "—");
                String departsAviationCenter = (String) shuttleData.getOrDefault("departsAviationCenter", "—");

                GridPane rowGrid = new GridPane();
                rowGrid.setHgap(15);
                rowGrid.setVgap(8);
                rowGrid.setPadding(new Insets(8, 10, 8, 10));
                rowGrid.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

                // Set equal column widths to match header
                ColumnConstraints rowCol1 = new ColumnConstraints();
                rowCol1.setPercentWidth(33.33);
                ColumnConstraints rowCol2 = new ColumnConstraints();
                rowCol2.setPercentWidth(33.33);
                ColumnConstraints rowCol3 = new ColumnConstraints();
                rowCol3.setPercentWidth(33.33);
                rowGrid.getColumnConstraints().addAll(rowCol1, rowCol2, rowCol3);

                Label time1 = new Label(departsFarmingdale);
                Label time2 = new Label(departsLaffinHall);
                Label time3 = new Label(departsAviationCenter);

                for (Label timeLabel : new Label[]{time1, time2, time3}) {
                    timeLabel.setStyle("""
                            -fx-font-size: 14px;
                            -fx-text-fill: #333;
                            -fx-font-weight: bold;
                            -fx-text-alignment: center;
                            """);
                    timeLabel.setMaxWidth(Double.MAX_VALUE);
                    timeLabel.setAlignment(Pos.CENTER);
                }

                rowGrid.add(time1, 0, 0);
                rowGrid.add(time2, 1, 0);
                rowGrid.add(time3, 2, 0);

                tableCard.getChildren().add(rowGrid);
            }

            scheduleContainer.getChildren().add(tableCard);
        }

        // Refresh Button
        Button refreshBtn = new Button("🔄 Refresh Schedule");
        refreshBtn.setStyle("""
                -fx-background-color: #0b5e2a;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 8;
                """);
        refreshBtn.setOnAction(e -> {
            // Reload the page to refresh shuttle data
            stage.setScene(getScene(stage, previousScene));
        });

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15, backBtn, titleLabel, subtitleLabel, scheduleContainer, refreshBtn);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, 450, 650);
    }
}
