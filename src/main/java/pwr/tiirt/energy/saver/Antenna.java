package pwr.tiirt.energy.saver;

import java.util.Collections;
import java.util.List;

public class Antenna {

	public final double x;
	public final double y;
	public final List<Antenna> neighbours;
	public final boolean active;

	public Antenna(final double x, final double y, final List<Antenna> neighbours, final boolean active) {
		this.x = x;
		this.y = y;
		this.neighbours = Collections.unmodifiableList(neighbours);
		this.active = active;
	}

}