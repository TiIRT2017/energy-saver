package pwr.tiirt.energy.saver;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Genotype {

	public final double[] ranges;
	public double score = 0.0;
	public double coverage = 0.0;

	public Genotype(final int noOfAntennas) {
		ranges = new double[noOfAntennas];
	}

	public Genotype(final int noOfAntennas, final double maxRange) {
		ranges = new double[noOfAntennas];
		initialize(maxRange);
	}

	public Genotype(final Genotype other) {
		this.ranges = new double[other.ranges.length];
		for (int i = 0; i < this.ranges.length; i++) {
			this.ranges[i] = other.ranges[i];
		}
		this.score = other.score;
		this.coverage = other.coverage;
	}

	private void initialize(final double maxRange) {
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = ThreadLocalRandom.current().nextDouble(maxRange);
		}
	}

	public void initializeMask() {
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = -1.0;
		}
	}

	public void initializeMask(final Genotype mask, final List<Antenna> antennas) {
		final List<Integer> indices = antennas.stream()
		                                      .filter(a -> !a.active)
		                                      .flatMap(a -> a.neighbours.stream())
		                                      .mapToInt(a -> antennas.indexOf(a)).boxed().collect(Collectors.toList());
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = indices.contains(i) ? -1.0 : mask.ranges[i];
		}
	}

}
