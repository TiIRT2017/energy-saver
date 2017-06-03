package pwr.tiirt.energy.saver.resolver;

import org.assertj.core.data.Percentage;
import org.assertj.core.util.Lists;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

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
        final ArrayList<Integer> x = Lists.newArrayList(1, 14, 14, 1);
        final ArrayList<Integer> y = Lists.newArrayList(7, 7, 1, 1);
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
        final ArrayList<Integer> x = Lists.newArrayList(1, 14, 14, 1);
        final ArrayList<Integer> y = Lists.newArrayList(7, 7, 1, 1);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        System.out.println(area);
        // then
        assertThat(area).isCloseTo(0.24, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageBigCircle() {
        // given
        final AntennaWithRadius antenna = new AntennaWithRadius(4, 4, 3, true);
        final ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna);
        final ArrayList<Integer> x = Lists.newArrayList(3, 5, 5, 3);
        final ArrayList<Integer> y = Lists.newArrayList(5, 5, 2, 2);
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
        final ArrayList<Integer> x = Lists.newArrayList(102, 116, 116, 102);
        final ArrayList<Integer> y = Lists.newArrayList(116, 116, 102, 102);
        final PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        final double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.79, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageTwoCirclesNotOverlapping() {
        // given
        AntennaWithRadius antenna1 = new AntennaWithRadius(3, 3, 1, true);
        AntennaWithRadius antenna2 = new AntennaWithRadius(15, 3, 1, true);
        ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna1, antenna2);
        ArrayList<Integer> x = Lists.newArrayList(1, 21, 21, 1);
        ArrayList<Integer> y = Lists.newArrayList(5, 5, 1, 1);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.05, Percentage.withPercentage(5));
    }

    @Test
    public void shouldCalculateCoverageWithSwitchedOffAntennas() {
        // given
        AntennaWithRadius antenna1 = new AntennaWithRadius(450, 750, 5, true);
        AntennaWithRadius antenna2 = new AntennaWithRadius(600, 600, 271, false);
        AntennaWithRadius antenna3 = new AntennaWithRadius(750, 450, 1, true);
        ArrayList<AntennaWithRadius> antennas = Lists.newArrayList(antenna1, antenna2, antenna3);
        int maxRange = 400;
        int height = 400;
        int width = 400;
        final List<Integer> x = com.google.common.collect.Lists.newArrayList(maxRange, width + maxRange, maxRange + width, maxRange);
        final List<Integer> y = com.google.common.collect.Lists.newArrayList(height + maxRange, height + maxRange, maxRange, maxRange);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        System.out.println(area);
        // then
        assertThat(area).isCloseTo(0.05, Percentage.withPercentage(5));
    }

}