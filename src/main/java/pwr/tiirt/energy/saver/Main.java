package pwr.tiirt.energy.saver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pwr.tiirt.energy.saver.presentation.GuiSupplier;

public class Main extends Application {

    public static void main(final String... args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Group root = new Group();
        final GuiSupplier guiSupplier = GuiSupplier.create(null);
        final Canvas canvas = guiSupplier.getCanvasWithDim();
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        guiSupplier.drawAntennas(gc);
        guiSupplier.drawRectangle(gc);
        root.getChildren().add(canvas);
        primaryStage.setTitle("Energy saver");
        primaryStage.setScene(new Scene(root, Color.ALICEBLUE));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();
    }
}
