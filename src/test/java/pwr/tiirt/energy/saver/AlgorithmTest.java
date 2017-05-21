package pwr.tiirt.energy.saver;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;
import pwr.tiirt.energy.saver.model.Rectangle;

import java.util.ArrayList;


import static org.assertj.core.api.Java6Assertions.assertThat;

public class AlgorithmTest {

	private Rectangle rectangle;

	@Before
	public void setUp() throws Exception {
		final ArrayList<Integer> x = Lists.newArrayList(1, 14, 14, 1);
		final ArrayList<Integer> y = Lists.newArrayList(7, 7, 1, 1);
		rectangle = new Rectangle(x, y);
	}

	@Test
	public void solve() throws Exception {
		final Antenna antenna1 = new Antenna(1,4, 5, Lists.emptyList(), true);
		final Antenna antenna2 = new Antenna(2,6, 5, Lists.emptyList(), true);
		final ArrayList<Antenna> antennas = Lists.newArrayList(antenna1, antenna2);
		final Algorithm a = new Algorithm(antennas, rectangle, 10, 50, 0.1, 0.2, 6);
		a.solve();
		for (int i = 0; i < a.getAverages().size(); i++) {
			assertThat(a.getMaximums().get(i) >= a.getAverages().get(i)
			           && a.getMinimums().get(i) <= a.getAverages().get(i));
		}
	}
}