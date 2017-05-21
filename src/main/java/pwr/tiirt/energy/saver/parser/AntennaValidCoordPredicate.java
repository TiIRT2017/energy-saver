package pwr.tiirt.energy.saver.parser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pwr.tiirt.energy.saver.Antenna;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;

import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class AntennaValidCoordPredicate implements Predicate<AntennaWithRadius> {
    private int xAxisRange;
    private int yAxisRange;
    private int maxRange;

    @Override
    public boolean test(final AntennaWithRadius antennaWithRadius) {
        return antennaWithRadius.getX() >= maxRange && antennaWithRadius.getX() <= (maxRange + xAxisRange)
               && antennaWithRadius.getY() >= maxRange && antennaWithRadius.getY() <= (maxRange + yAxisRange);
    }
}
