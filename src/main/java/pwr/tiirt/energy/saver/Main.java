package pwr.tiirt.energy.saver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(final String... args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("/gui.fxml"));
        primaryStage.setTitle("Energy saver");
        primaryStage.setScene(new Scene(root, Color.ALICEBLUE));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
