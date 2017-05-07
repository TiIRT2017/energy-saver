package pwr.tiirt.energy.saver.parser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;

import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class AntennaValidCoordPredicate implements Predicate<AntennaWithRadius> {
    private int x_axis_range;
    private int y_axis_range;

    @Override
    public boolean test(AntennaWithRadius antennaWithRadius) {
        return antennaWithRadius.getSmallestX() > 0 && antennaWithRadius.getBiggestX() < x_axis_range && antennaWithRadius.getSmallestY() > 0 && antennaWithRadius.getBiggestY() < y_axis_range;
    }
}
