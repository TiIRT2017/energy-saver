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

    private int x;
    private int y;
    private int r;
    private boolean isActive;

    public int getSmallestX() {
        return getX() - getR();
    }

    public int getBiggestX() {
        return getX() + getR();
    }
}
