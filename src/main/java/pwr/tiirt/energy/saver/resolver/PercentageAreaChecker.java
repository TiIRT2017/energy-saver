package pwr.tiirt.energy.saver.resolver;

import lombok.*;
import pwr.tiirt.energy.saver.Antenna;
import pwr.tiirt.energy.saver.Genotype;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Point;
import pwr.tiirt.energy.saver.model.Rectangle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kasia on 04.05.2017.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PercentageAreaChecker {

    private Rectangle rectangle;
    private List<AntennaWithRadius> allAntennas;

    public double calculateCoverage() {
        final List<AntennaWithRadius> antennas = allAntennas.stream().filter(AntennaWithRadius::isActive).collect(Collectors.toList());
        final PolygonResolver polygonResolver = new PolygonResolver(antennas);
        final List<Double> xCoordinates = polygonResolver.getXCoordinates();
        final List<Double> yCoordinates = polygonResolver.getYCoordinates();
        final double xA = rectangle.getA().getX();
        final double yA = rectangle.getA().getY();
        final double xB = rectangle.getB().getX();
        final double yB = rectangle.getB().getY();
        final double xC = rectangle.getC().getX();
        final double yC = rectangle.getC().getY();
        final double xD = rectangle.getD().getX();
        final double yD = rectangle.getD().getY();

        for (int i = 0; i < xCoordinates.size(); i++) {
            final Point qPoint = new Point(xCoordinates.get(i), yCoordinates.get(i));
            final double pX = qPoint.getX();
            final double pY = qPoint.getY();
            if (!containsPoint(rectangle, qPoint)) {
                if (isLeft(xA, yA, yD, pX, pY)) {
                    xCoordinates.set(i, xA);
                } else if (isUpper(xA, yA, xB, pX, pY)) {
                    yCoordinates.set(i, yA);
                } else if (isRight(yA, xB, yD, pX, pY)) {
                    xCoordinates.set(i, xB);
                } else if (isLower(xA, xB, yD, pX, pY)) {
                    yCoordinates.set(i, yD);
                } else if (isLeftUpper(xA, yA, pX, pY)) {
                    xCoordinates.set(i, xA);
                    yCoordinates.set(i, yA);
                } else if (isLeftLower(xA, yD, pX, pY)) {
                    xCoordinates.set(i, xD);
                    yCoordinates.set(i, yD);
                } else if (isRightUpper(xB, yB, pX, pY)) {
                    xCoordinates.set(i, xB);
                    yCoordinates.set(i, yB);
                } else {
                    xCoordinates.set(i, xC);
                    yCoordinates.set(i, yC);
                }
            }
        }

        final double rectangleArea = calculateRectangleArea();
        final double polygonArea = polygonResolver.calculatePolygonArea(xCoordinates, yCoordinates);
        return polygonArea / rectangleArea;
    }

    private boolean containsPoint(final Rectangle rectangle, final Point point) {
        return rectangle.getA().getX() <= point.getX() && rectangle.getB().getX() >=point.getX()
                && rectangle.getA().getY() >= point.getY() && rectangle.getC().getY() <= point.getY();
    }

    private boolean isRightUpper(final double xB, final double yB, final double pX, final double pY) {
        return pX > xB && pY > yB;
    }

    private boolean isLeftLower(final double xA, final double yD, final double pX, final double pY) {
        return pX < xA && pY < yD;
    }

    private boolean isLeftUpper(final double xA, final double yA, final double pX, final double pY) {
        return pX < xA && pY > yA;
    }

    private boolean isLower(final double xA, final double xB, final double yD, final double pX, final double pY) {
        return pX >= xA && pX <= xB && pY < yD;
    }

    private boolean isRight(final double yA, final double xB, final double yD, final double pX, final double pY) {
        return pX > xB && pY >= yD && pY <= yA;
    }

    private boolean isUpper(final double xA, final double yA, final double xB, final double pX, final double pY) {
        return pX >= xA && pX <= xB && pY > yA;
    }

    private boolean isLeft(final double xA, final double yA, final double yD, final double pX, final double pY) {
        return pX < xA && pY >= yD && pY <= yA;
    }

    private double calculateRectangleArea() {
        final double xA = rectangle.getA().getX();
        final double xB = rectangle.getB().getX();
        final double yB = rectangle.getB().getY();
        final double yC = rectangle.getC().getY();
        return (xB - xA) * (yB - yC);
    }

}
