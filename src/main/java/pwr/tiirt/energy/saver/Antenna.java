package pwr.tiirt.energy.saver;

import java.util.Collections;
import java.util.List;

public class Antenna {

	public final int x;
	public final int y;
	public final List<Antenna> neighbours;
	public final boolean active;

	public Antenna(final int x, final int y, final List<Antenna> neighbours, final boolean active) {
		this.x = x;
		this.y = y;
		this.neighbours = Collections.unmodifiableList(neighbours);
		this.active = active;
	}

}
