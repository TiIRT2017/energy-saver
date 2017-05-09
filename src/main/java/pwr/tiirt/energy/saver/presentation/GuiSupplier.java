package pwr.tiirt.energy.saver.presentation;

import com.google.common.collect.Lists;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pwr.tiirt.energy.saver.Algorithm;
import pwr.tiirt.energy.saver.Antenna;
import pwr.tiirt.energy.saver.Genotype;
import pwr.tiirt.energy.saver.model.AntennaOutOfBoundException;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;
import pwr.tiirt.energy.saver.parser.JSONFileReader;
import pwr.tiirt.energy.saver.resolver.PercentageAreaChecker;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class GuiSupplier {
    private int boardWidth;
    private int boardHeight;
    private List<Antenna> antennas;


    public static GuiSupplier create(String dataFilePath) throws IOException, AntennaOutOfBoundException {
        String filePath = Objects.isNull(dataFilePath) ? GuiSupplier.class.getResource("/sample_topology.json").getFile() : dataFilePath;
        List<Integer> coord = new JSONFileReader().getBoardCoordinates(filePath);
        List<Antenna> antennas = new JSONFileReader().getAntennaData(filePath, coord.get(0), coord.get(1));
        return new GuiSupplier(coord.get(0), coord.get(1), antennas);
    }

    public Canvas getCanvasWithDim() {
        return new Canvas(boardWidth, boardHeight);
    }

    public void drawAntennas(GraphicsContext gc) {
        List<AntennaWithRadius> antennasWithRadius = process(antennas, boardWidth, boardHeight);
        displayAntennaData(gc, antennasWithRadius);
    }

    private void displayAntennaData(GraphicsContext gc, List<AntennaWithRadius> antennas) {
        gc.setFill(Color.GRAY);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        for (AntennaWithRadius a : antennas) {
            gc.strokeOval(a.getX() - a.getR(), a.getY() - a.getR(), a.getR() * 2, a.getR() * 2);

        }
    }

    //todo: extract
    private Rectangle calcRectangle(int width, int height) {
        List<Integer> x = Lists.newArrayList(0, width, width, 0);
        List<Integer> y = Lists.newArrayList(height, height, 0, 0);
        return new Rectangle(x, y);
    }

    private List<AntennaWithRadius> process(List<Antenna> antennas, int width, int height) {
        Rectangle rectangle = calcRectangle(width, height);
        final Algorithm a = new Algorithm(antennas, rectangle, 1000, 100, 0.1, 0.2, 700);
        a.solve();
        List<Genotype> bestGenotypes = a.getBestGenotypes();
        int[] bestRanges = bestGenotypes.get(bestGenotypes.size() - 1).ranges;
        List<AntennaWithRadius> antennasWithRadius = AntennaWithRadius.antennaToAntennaWithRadius(antennas, bestRanges);
        System.out.println("ANTENNAS BEFORE: " + antennas.toString());
        System.out.println("ANTENNAS AFTER: " + antennasWithRadius.toString());
        System.out.println("COVERAGE:" + getCoverage(rectangle, antennasWithRadius));
        return antennasWithRadius;

    }

    private double getCoverage(Rectangle rectangle, List<AntennaWithRadius> antennasWithRadius) {
        PercentageAreaChecker p = new PercentageAreaChecker(rectangle, antennasWithRadius);
        return p.calculateCoverage();
    }

}
