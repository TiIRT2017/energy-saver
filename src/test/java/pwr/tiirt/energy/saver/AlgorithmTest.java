package pwr.tiirt.energy.saver;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AlgorithmTest {

	private Rectangle rectangle;

	@Before
	public void setUp() throws Exception {
		final ArrayList<Double> x = Lists.newArrayList(1.0, 14.0, 14.0, 1.0);
		final ArrayList<Double> y = Lists.newArrayList(7.0, 7.0, 1.0, 1.0);
		rectangle = new Rectangle(x, y);
	}

	@Test
	public void solve() throws Exception {
		final Antenna antenna1 = new Antenna(4, 5, Lists.emptyList(), true);
		final Antenna antenna2 = new Antenna(6, 5, Lists.emptyList(), true);
		final ArrayList<Antenna> antennas = Lists.newArrayList(antenna1, antenna2);
		final Algorithm a = new Algorithm(antennas, rectangle, 1000, 100, 0.1, 0.2, 10);
		a.solve();
		a.toString();
	}
}