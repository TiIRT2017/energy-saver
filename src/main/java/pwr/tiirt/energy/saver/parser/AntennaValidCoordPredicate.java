package pwr.tiirt.energy.saver.parser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pwr.tiirt.energy.saver.Antenna;

import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class AntennaValidCoordPredicate implements Predicate<Antenna> {
    private int x_axis_range;
    private int y_axis_range;

    @Override
    public boolean test(Antenna antennaWithRadius) {
        return antennaWithRadius.getX() >= 0 && antennaWithRadius.getX() <= x_axis_range && antennaWithRadius.getY() >= 0 && antennaWithRadius.getY() <= y_axis_range;
    }
}
