package pwr.tiirt.energy.saver;

import com.google.common.collect.Lists;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;
import pwr.tiirt.energy.saver.parser.JSONFileReader;

import java.util.List;

public class Main extends Application {

    public static void main(final String... args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Group root = new Group();
        List<Integer> coord = new JSONFileReader().getBoardCoordinates(JSONFileReader.DEFAULT_TOPOLOGY_FILE_PATH);
        List<Antenna> antennas = new JSONFileReader().getAntennaData(JSONFileReader.DEFAULT_TOPOLOGY_FILE_PATH, coord.get(0), coord.get(1));
        Canvas canvas = new Canvas(coord.get(0), coord.get(1));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<AntennaWithRadius> antennasWithRadius = process(antennas, coord);
        drawAntennas(gc, antennasWithRadius);
        root.getChildren().add(canvas);
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(new Scene(root, coord.get(0) + 200, coord.get(1) + 100, Color.ALICEBLUE));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();
    }

    private void drawAntennas(GraphicsContext gc, List<AntennaWithRadius> antennas) {
        gc.setFill(Color.GRAY);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        for (AntennaWithRadius a : antennas) {
            gc.strokeOval(a.getX(), a.getY(), a.getR(), a.getR());

        }
    }

    //todo: extract 
    private Rectangle calcRectangle(List<Integer> coord) {
        List<Integer> x = Lists.newArrayList(0, coord.get(0), coord.get(0), 0);
        List<Integer> y = Lists.newArrayList(coord.get(1), coord.get(1), 0, 0);
        return new Rectangle(x, y);
    }

    private List<AntennaWithRadius> process(List<Antenna> antennas, List<Integer> coord) {
        Rectangle rectangle = calcRectangle(coord);
        final Algorithm a = new Algorithm(antennas, rectangle, 10, 50, 0.1, 0.2, 900);
        a.solve();
        List<Genotype> bestGenotypes = a.getBestGenotypes();
        int[] bestRanges = bestGenotypes.get(bestGenotypes.size() - 1).ranges;
        List<AntennaWithRadius> antennasWithRadius = AntennaWithRadius.antennaToAntennaWithRadius(antennas, bestRanges);
        System.out.println("ANTENNAS BEFORE: " + antennas.toString());
        System.out.println("ANTENNAS AFTER: " + antennasWithRadius.toString());
        return antennasWithRadius;

    }
}
