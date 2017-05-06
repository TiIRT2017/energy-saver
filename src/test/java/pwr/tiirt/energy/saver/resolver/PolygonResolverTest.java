package pwr.tiirt.energy.saver.resolver;


import org.assertj.core.data.Percentage;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.Antenna;

import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by kasia on 04.05.2017.
 */
public class PolygonResolverTest {

    private PolygonResolver polygonResolver;
    private Antenna first;
    private Antenna second;

    @Before
    public void setUp() throws Exception {
        first = new Antenna(3, 3, 2, true);
        second = new Antenna(5, 5, 2, true);
        polygonResolver = new PolygonResolver(Arrays.asList(first, second));
    }

    @Test
    public void shouldCalculatePolygonAreaTwoTouchingCircles() throws Exception {
        // when
        double area = polygonResolver.calculatePolygonArea(Lists.newArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 4.0, 3.01, 2.0, 1.01),
                Lists.newArrayList(1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 1.01, 0.0, 1.01));
        // then
        assertThat(area).isCloseTo(4.0, Percentage.withPercentage(10));
    }

    @Test
    public void shouldCalculatePolygonAreaTwoOverlappingCircles() throws Exception {
        // when
        double area = polygonResolver.calculatePolygonArea(Lists.newArrayList(0.0, 1.0, 2.0, 3.0, 3.01, 2.0, 1.0, 0.01),
                Lists.newArrayList(1.0, 2.0, 2.0, 1.0, 1.01, 0.0, 0.0, 1.01));
        // then
        assertThat(area).isCloseTo(4.0, Percentage.withPercentage(10));
    }


}