package pwr.tiirt.energy.saver.resolver;

import org.assertj.core.data.Percentage;
import org.assertj.core.util.Lists;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by kasia on 05.05.2017.
 */
public class PercentageAreaCheckerTest {

    @Test
    public void shouldCalculateCoverageSingleAntennaInside() {
        // given
        final AntennaWithRadius antenna = new AntennaWithRadius(4, 5, 2, true);
        final ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna);
        final ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
        final ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.15, Percentage.withPercentage(10));
    }

    @Test
    public void shouldCalculateCoverageOverlappingAntennas() {
        // given
        final AntennaWithRadius antenna1 = new AntennaWithRadius(4, 5, 2, true);
        final AntennaWithRadius antenna2 = new AntennaWithRadius(6, 5, 2, true);
        final ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna1, antenna2);
        final ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
        final ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        System.out.println(area);
        // then
        assertThat(area).isCloseTo(0.27, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageBigCircle() {
        // given
        final AntennaWithRadius antenna = new AntennaWithRadius(4, 4, 3, true);
        final ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna);
        final ArrayList<Double> x = Lists.newArrayList(3.0, 5.0, 5.0, 3.0);
        final ArrayList<Double> y = Lists.newArrayList(5.0, 5.0, 2.0, 2.0);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(1, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageHalfCirclesTouching() {
        // given
        final AntennaWithRadius antenna1 = new AntennaWithRadius(102, 109, 7, true);
        final AntennaWithRadius antenna2 = new AntennaWithRadius(116, 109, 7, true);
        final ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna1, antenna2);
        final ArrayList<Double> x = Lists.newArrayList(102.0, 116.0, 116.0, 102.0);
        final ArrayList<Double> y = Lists.newArrayList(116.0, 116.0, 102.0, 102.0);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.839, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageTwoCirclesNotOverlapping() {
        // given
        AntennaWithRadius antenna1 = new AntennaWithRadius(4, 5, 0.005, true);
        AntennaWithRadius antenna2 = new AntennaWithRadius(6, 5, 0.001, true);
        ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna1, antenna2);
        ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
        ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.000154, Percentage.withPercentage(5));
    }

}