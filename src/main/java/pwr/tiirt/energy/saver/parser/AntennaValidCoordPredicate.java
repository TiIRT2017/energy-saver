package pwr.tiirt.energy.saver.parser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pwr.tiirt.energy.saver.Antenna;

import java.util.function.Predicate;

@AllArgsConstructor
@NoArgsConstructor
public class AntennaValidCoordPredicate implements Predicate<Antenna> {
    private int xAxisRange;
    private int yAxisRange;

    @Override
    public boolean test(Antenna antennaWithRadius) {
        return antennaWithRadius.getX() >= 0 && antennaWithRadius.getX() <= xAxisRange && antennaWithRadius.getY() >= 0 && antennaWithRadius.getY() <= yAxisRange;
    }
}
