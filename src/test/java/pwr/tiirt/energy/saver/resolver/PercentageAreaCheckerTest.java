package pwr.tiirt.energy.saver.resolver;

import org.assertj.core.data.Percentage;
import org.assertj.core.util.Lists;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.Antenna;
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
        Antenna antenna = new Antenna(4, 5, 2, true);
        ArrayList<Antenna> antennas = Lists.newArrayList(antenna);
        ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
        ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.15, Percentage.withPercentage(10));
    }

    @Test
    public void shouldCalculateCoverageOverlappingAntennas() {
        // given
        Antenna antenna1 = new Antenna(4, 5, 2, true);
        Antenna antenna2 = new Antenna(6, 5, 2, true);
        ArrayList<Antenna> antennas = Lists.newArrayList(antenna1, antenna2);
        ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
        ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        System.out.println(area);
        // then
        assertThat(area).isCloseTo(0.2356, Percentage.withPercentage(10));
    }

    @Test
    public void shouldCalculateCoverageBigCircle() {
        // given
        Antenna antenna = new Antenna(4, 4, 3, true);
        ArrayList<Antenna> antennas = Lists.newArrayList(antenna);
        ArrayList<Double> x = Lists.newArrayList(3.0, 5.0, 5.0, 3.0);
        ArrayList<Double> y = Lists.newArrayList(5.0, 5.0, 2.0, 2.0);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isEqualTo(1);
    }

    @Test
    public void shouldCalculateCoverageHalfCirclesTouching() {
        // given
        Antenna antenna1 = new Antenna(102, 109, 7, true);
        Antenna antenna2 = new Antenna(116, 109, 7, true);
        ArrayList<Antenna> antennas = Lists.newArrayList(antenna1, antenna2);
        ArrayList<Double> x = Lists.newArrayList(102.0, 116.0, 116.0, 102.0);
        ArrayList<Double> y = Lists.newArrayList(116.0, 116.0, 102.0, 102.0);
        PercentageAreaChecker percentageAreaChecker = new PercentageAreaChecker(new Rectangle(x, y), antennas);
        // when
        double area = percentageAreaChecker.calculateCoverage();
        // then
        assertThat(area).isCloseTo(0.785, Percentage.withPercentage(5));
    }

}