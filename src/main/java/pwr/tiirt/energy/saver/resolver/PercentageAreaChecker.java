package pwr.tiirt.energy.saver.resolver;

import lombok.*;
import pwr.tiirt.energy.saver.model.Antenna;
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
    private List<Antenna> allAntennas;

    public double calculateCoverage() {
        List<Antenna> antennas = allAntennas.stream().filter(Antenna::isActive).collect(Collectors.toList());
        PolygonResolver polygonResolver = new PolygonResolver(antennas);
        List<Double> xCoordinates = polygonResolver.getXCoordinates();
        List<Double> yCoordinates = polygonResolver.getYCoordinates();
        double xA = rectangle.getA().getX();
        double yA = rectangle.getA().getY();
        double xB = rectangle.getB().getX();
        double yB = rectangle.getB().getY();
        double xC = rectangle.getC().getX();
        double yC = rectangle.getC().getY();
        double xD = rectangle.getD().getX();
        double yD = rectangle.getD().getY();

        for (int i = 0; i < xCoordinates.size(); i++) {
            Point qPoint = new Point(xCoordinates.get(i), yCoordinates.get(i));
            double pX = qPoint.getX();
            double pY = qPoint.getY();
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

        double rectangleArea = calculateRectangleArea();
        double polygonArea = polygonResolver.calculatePolygonArea(xCoordinates, yCoordinates);
        return polygonArea / rectangleArea;
    }

    private boolean containsPoint(Rectangle rectangle, Point point) {
        return rectangle.getA().getX() <= point.getX() && rectangle.getB().getX() >=point.getX()
                && rectangle.getA().getY() >= point.getY() && rectangle.getC().getY() <= point.getY();
    }

    private boolean isRightUpper(double xB, double yB, double pX, double pY) {
        return pX > xB && pY > yB;
    }

    private boolean isLeftLower(double xA, double yD, double pX, double pY) {
        return pX < xA && pY < yD;
    }

    private boolean isLeftUpper(double xA, double yA, double pX, double pY) {
        return pX < xA && pY > yA;
    }

    private boolean isLower(double xA, double xB, double yD, double pX, double pY) {
        return pX >= xA && pX <= xB && pY < yD;
    }

    private boolean isRight(double yA, double xB, double yD, double pX, double pY) {
        return pX > xB && pY >= yD && pY <= yA;
    }

    private boolean isUpper(double xA, double yA, double xB, double pX, double pY) {
        return pX >= xA && pX <= xB && pY > yA;
    }

    private boolean isLeft(double xA, double yA, double yD, double pX, double pY) {
        return pX < xA && pY >= yD && pY <= yA;
    }

    private double calculateRectangleArea() {
        double xA = rectangle.getA().getX();
        double xB = rectangle.getB().getX();
        double yB = rectangle.getB().getY();
        double yC = rectangle.getC().getY();
        return (xB - xA) * (yB - yC);
    }

}
