package pwr.tiirt.energy.saver.model;

import com.google.common.collect.Lists;
import org.junit.Test;
import pwr.tiirt.energy.saver.Antenna;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sandra on 21.05.2017.
 */
public class AntennaWithRadiusTest {
    private static final AntennaWithRadius a1 = new AntennaWithRadius(1, 10, 10, 10, true, Lists.newArrayList());
    private static final AntennaWithRadius a2 = new AntennaWithRadius(2, 5, 20, 0, true, Lists.newArrayList(3));
    private static final AntennaWithRadius a3 = new AntennaWithRadius(3, 5, 20, 0, true, Lists.newArrayList(2));
    private static final AntennaWithRadius a4 = new AntennaWithRadius(4, 5, 20, 0, true, Lists.newArrayList(4));
    private static final AntennaWithRadius a5 = new AntennaWithRadius(5, 5, 20, 0, true, Lists.newArrayList(6));
    private static final List<Antenna> EMPTY = Lists.newArrayList();

    @Test
    public void shouldCreateValidAntennaWithoutAdjFromAntennaWithRadius() {
        List<Antenna> antennas = AntennaWithRadius.antennaWithRadiusToAntenna(Lists.newArrayList(a1));
        assertEquals("Lists should be of the same size", 1, antennas.size());
        Antenna ante1 = antennas.get(0);
        assertEquals(ante1.getId(), a1.getId());
        assertEquals(ante1.getX(), a1.getX());
        assertEquals(ante1.getY(), a1.getY());
        assertEquals(ante1.isActive(), a1.isActive());
        assertEquals("Neighbor list should be EMPTY", EMPTY, ante1.getNeighbours());
    }

    @Test
    public void shouldCreateAntennasWithValidAdjFromAntennasWithRadius() {
        List<Antenna> antennas = AntennaWithRadius.antennaWithRadiusToAntenna(Lists.newArrayList(a2, a3));
        assertEquals("Lists should be of the same size", 2, antennas.size());
        Antenna ante2 = antennas.get(0);
        Antenna ante3 = antennas.get(1);
        assertEquals("Antenna and the same antenna in another antenna adjacency should be the same", ante2, ante3.getNeighbours().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowsExceptionWhenCircularRefDetected() {
        List<Antenna> antennas = AntennaWithRadius.antennaWithRadiusToAntenna(Lists.newArrayList(a4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNeighborObjDoesNotExists() {
        List<Antenna> antennas = AntennaWithRadius.antennaWithRadiusToAntenna(Lists.newArrayList(a5));
    }

}