package pwr.tiirt.energy.saver.resolver;

import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kasia on 04.05.2017.
 */
public class PolygonResolver {

    private final List<AntennaWithRadius> antennas;
    private double lastMaxY = 0;
    private double lastMinY = 0;

    public PolygonResolver(final List<AntennaWithRadius> antennas) {
        this.antennas = antennas;
    }

    /**
     * Calculate polygon area
     *
     * @param x the x - x coordinates given CLOCKWISE
     * @param y the y - y coordinates given CLOCKWISE
     * @return the double
     */
    public double calculatePolygonArea(final List<Double> x, final List<Double> y) {
        double area = 0;
        int j = x.size() - 1;
        for (int i = 0; i < x.size(); i++) {
            area += (x.get(j) + x.get(i)) * (y.get(j) - y.get(i));
            j = i;
        }
        return area / 2;
    }

    public List<Double> getXCoordinates() {
        return getShapeAsListOfPoints().stream().map(Point::getX).collect(Collectors.toList());
    }

    public List<Double> getYCoordinates() {
        return getShapeAsListOfPoints().stream().map(Point::getY).collect(Collectors.toList());
    }

    private List<Point> getShapeAsListOfPoints() {
        final double minX = getCircleWithSmallestX().getSmallestX();
        final double maxX = getCircleWithBiggestX().getBiggestX();
        final List<Point> result = new ArrayList<>();
        getUpperPartOThePolygon(result, minX, maxX);
        getLowerPartOfThePolygon(result, minX, maxX);
        return result.stream().distinct().collect(Collectors.toList());
    }

    private AntennaWithRadius getCircleWithSmallestX() {
        final Optional<AntennaWithRadius> circleWithSmallestX = antennas.stream().min(Comparator.comparingDouble(AntennaWithRadius::getSmallestX));
        return circleWithSmallestX.orElse(null);
    }

    private AntennaWithRadius getCircleWithBiggestX() {
        final Optional<AntennaWithRadius> circleWithBiggestX = antennas.stream().max(Comparator.comparingDouble(AntennaWithRadius::getBiggestX));
        return circleWithBiggestX.orElse(null);
    }

    private void getLowerPartOfThePolygon(final List<Point> result, final double minX, final double maxX) {
        for (double nextX = maxX; nextX >= minX; nextX--) {
            double minY = Integer.MAX_VALUE;
            for (final AntennaWithRadius antenna : antennas) {
                minY = getMinY(nextX, minY, antenna);
                if (minY == Integer.MAX_VALUE) {
                    minY = lastMinY;
                }
            }
            final Point point = new Point(nextX, minY);
            lastMinY = minY;
            result.add(point);
        }
    }

    private double getMinY(final double nextX, double minY, final AntennaWithRadius antenna) {
        try {
            final double smallerY = getSmallerYForXAndCircle(nextX, antenna);
            minY = (smallerY <= minY ? smallerY: minY);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return minY;
    }

    private void getUpperPartOThePolygon(final List<Point> result, final double minX, final double maxX) {
        for (double nextX = minX; nextX <= maxX; nextX ++) {
            double maxY = Integer.MIN_VALUE;
            for (final AntennaWithRadius antenna : antennas) {
                maxY = getMaxY(nextX, maxY, antenna);
                if (maxY == Integer.MIN_VALUE) {
                    maxY = lastMaxY;
                }
            }
            final Point point = new Point(nextX, maxY);
            lastMaxY = maxY;
            result.add(point);
        }
    }

    private double getMaxY(final double nextX, double maxY, final AntennaWithRadius antenna) {
        try {
            final double biggerY = getBiggerYForXAndCircle(nextX, antenna);
            maxY = (biggerY >= maxY ? biggerY : maxY);
        } catch (final NegativeValueException e) {
            e.printStackTrace();
        }
        return maxY;
    }

    private double getBiggerYForXAndCircle(final double x, final AntennaWithRadius antenna) throws NegativeValueException {
        final double underSqrt = Math.sqrt(antenna.getR() * antenna.getR() - (x - antenna.getX()) * (x - antenna.getX()));
        if (underSqrt < 0) {
            throw new NegativeValueException("Negative value");
        }
        return antenna.getY() + underSqrt;
    }

    private double getSmallerYForXAndCircle(final double x, final AntennaWithRadius antenna) throws NegativeValueException {
        final double underSqrt = Math.sqrt(antenna.getR() * antenna.getR() - (x - antenna.getX()) * (x - antenna.getX()));
        if (underSqrt < 0) {
            throw new NegativeValueException("Negative value");
        }
        return antenna.getY() - underSqrt;
    }
}
