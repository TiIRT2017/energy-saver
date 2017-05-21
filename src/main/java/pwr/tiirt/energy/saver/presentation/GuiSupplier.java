package pwr.tiirt.energy.saver.presentation;

import com.google.common.collect.Lists;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class GuiSupplier {

    private int boardWidth;
    private int boardHeight;
    private int translate;
    private int maxRange;
    private List<Antenna> antennas;
    private double coverage;

    public static GuiSupplier create(final String dataFilePath) throws IOException, AntennaOutOfBoundException {
        final String filePath = Objects.isNull(dataFilePath) ? GuiSupplier.class.getResource("/sample_topology.json").getFile() : dataFilePath;
        final List<Integer> coord = new JSONFileReader().getBoardCoordinates(filePath);
        final List<Antenna> antennas = new JSONFileReader().getAntennaData(filePath, coord.get(0), coord.get(1), coord.get(2));
        return new GuiSupplier(coord.get(0), coord.get(1), (int) (coord.get(2) * 1.5), coord.get(2), antennas, 0.0);
    }

    public List<AntennaWithRadius> drawAntennas(final GraphicsContext gc) {
        final List<AntennaWithRadius> antennas = process(this.antennas, boardWidth, boardHeight);
        displayAntennaData(gc, antennas);
        return antennas;
    }

    private void displayAntennaData(final GraphicsContext gc, final List<AntennaWithRadius> antennas) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        antennas.stream().filter(AntennaWithRadius::isActive).forEach(a ->
                gc.strokeOval(translate - maxRange + a.getX() - a.getR(), translate - maxRange + a.getY() - a.getR(),
                        a.getR() * 2, a.getR() * 2));
    }

    private Rectangle calcRectangle(final int width, final int height) {
        final List<Integer> x = Lists.newArrayList(maxRange, maxRange + width, maxRange + width, maxRange);
        final List<Integer> y = Lists.newArrayList(maxRange + height, maxRange + height, maxRange, maxRange);
        return new Rectangle(x, y);
    }

    private List<AntennaWithRadius> process(final List<Antenna> antennas, final int width, final int height) {
        final Rectangle rectangle = calcRectangle(width, height);
        List<AntennaWithRadius> antennasWithRadius = Collections.emptyList();
        coverage = Double.NEGATIVE_INFINITY;
        while (coverage < 0) {
            final Algorithm a = new Algorithm(antennas, rectangle, 100, 20, 0.1, 0.3, maxRange);
            a.solve();
            final List<Genotype> bestGenotypes = a.getBestGenotypes();
            final int[] bestRanges = bestGenotypes.get(bestGenotypes.size() - 1).ranges;
            antennasWithRadius = AntennaWithRadius.antennaToAntennaWithRadius(antennas, bestRanges);
            coverage = getCoverage(rectangle, antennasWithRadius);
        }
        return antennasWithRadius;
    }

    private double getCoverage(final Rectangle rectangle, final List<AntennaWithRadius> antennasWithRadius) {
        final PercentageAreaChecker p = new PercentageAreaChecker(rectangle, antennasWithRadius);
        return p.calculateCoverage();
    }

    public void drawRectangle(final GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeRect(translate, translate, boardWidth, boardHeight);
    }
}
