package pwr.tiirt.energy.saver.resolver;


import pwr.tiirt.energy.saver.model.Antenna;
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

    private List<Antenna> antennas;
    private double lastMaxY = 0;
    private double lastMinY = 0;

    public PolygonResolver(List<Antenna> antennas) {
        this.antennas = antennas;
    }


    /**
     * Calculate polygon area
     *
     * @param x the x - x coordinates given CLOCKWISE
     * @param y the y - y coordinates given CLOCKWISE
     * @return the double
     */
    public double calculatePolygonArea(List<Double> x, List<Double> y) {
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
        double minX = getCircleWithSmallestX().getSmallestX();
        double maxX = getCircleWithBiggestX().getBiggestX();
        List<Point> result = new ArrayList<>();
        getUpperPartOThePolygon(result, minX, maxX);
        getLowerPartOfThePolygon(result, minX, maxX);
        return result.stream().distinct().collect(Collectors.toList());
    }

    private Antenna getCircleWithSmallestX() {
        Optional<Antenna> circleWithSmallestX = antennas.stream().min(Comparator.comparingDouble(Antenna::getSmallestX));
        return circleWithSmallestX.orElse(null);
    }

    private Antenna getCircleWithBiggestX() {
        Optional<Antenna> circleWithBiggestX = antennas.stream().max(Comparator.comparingDouble(Antenna::getBiggestX));
        return circleWithBiggestX.orElse(null);
    }

    private void getLowerPartOfThePolygon(List<Point> result, double minX, double maxX) {
        for (double nextX = maxX; nextX >= minX; nextX-=0.001) {
            double minY = Integer.MAX_VALUE;
            for (Antenna antenna : antennas) {
                minY = getMinY(nextX, minY, antenna);
                if (minY == Integer.MAX_VALUE) {
                    minY = lastMinY;
                }
            }
            Point point = new Point(nextX, minY);
            lastMinY = minY;
            result.add(point);
        }
    }

    private double getMinY(double nextX, double minY, Antenna antenna) {
        try {
            double smallerY = getSmallerYForXAndCircle(nextX, antenna);
            minY = (smallerY <= minY ? smallerY: minY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minY;
    }

    private void getUpperPartOThePolygon(List<Point> result, double minX, double maxX) {
        for (double nextX = minX; nextX <= maxX; nextX+=0.001) {
            double maxY = Integer.MIN_VALUE;
            for (Antenna antenna : antennas) {
                maxY = getMaxY(nextX, maxY, antenna);
                if (maxY == Integer.MIN_VALUE) {
                    maxY = lastMaxY;
                }
            }
            Point point = new Point(nextX, maxY);
            lastMaxY = maxY;
            result.add(point);
        }
    }

    private double getMaxY(double nextX, double maxY, Antenna antenna) {
        try {
            double biggerY = getBiggerYForXAndCircle(nextX, antenna);
            maxY = (biggerY >= maxY ? biggerY : maxY);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        return maxY;
    }

    private double getBiggerYForXAndCircle(double x, Antenna antenna) throws NegativeValueException {
        double underSqrt = Math.sqrt(antenna.getR() * antenna.getR() - (x - antenna.getX()) * (x - antenna.getX()));
        if (underSqrt < 0) {
            throw new NegativeValueException("Negative value");
        }
        return antenna.getY() + underSqrt;
    }

    private double getSmallerYForXAndCircle(double x, Antenna antenna) throws NegativeValueException {
        double underSqrt = Math.sqrt(antenna.getR() * antenna.getR() - (x - antenna.getX()) * (x - antenna.getX()));
        if (underSqrt < 0) {
            throw new NegativeValueException("Negative value");
        }
        return antenna.getY() - underSqrt;
    }
}
