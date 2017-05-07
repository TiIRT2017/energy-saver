package pwr.tiirt.energy.saver.model;

import lombok.*;

/**
 * Created by kasia on 04.05.2017.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AntennaWithRadius {

    private double x;
    private double y;
    private double r;
    private boolean isActive;

    public double getSmallestX() {
        return getX() - getR();
    }

    public double getBiggestX() {
        return getX() + getR();
    }
}
