package pwr.tiirt.energy.saver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(final String... args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Parent root = FXMLLoader.load(getClass().getResource("/fxml_example.fxml"));
		final Scene scene = new Scene(root, 300, 275);
		primaryStage.setTitle("FXML Welcome");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
		primaryStage.show();
	}
}
