package pwr.tiirt.energy.saver;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Genotype {

	public final int[] ranges;
	public double score = 0.0;
	public double coverage = 0.0;

	public Genotype(final int noOfAntennas) {
		ranges = new int[noOfAntennas];
	}

	public Genotype(final int noOfAntennas, final int maxRange) {
		ranges = new int[noOfAntennas];
		initialize(maxRange);
	}

	public Genotype(final Genotype other) {
		this.ranges = new int[other.ranges.length];
		for (int i = 0; i < this.ranges.length; i++) {
			this.ranges[i] = other.ranges[i];
		}
		this.score = other.score;
		this.coverage = other.coverage;
	}

	private void initialize(final int maxRange) {
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = ThreadLocalRandom.current().nextInt(maxRange);
		}
	}

	public void initializeMask() {
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = -1;
		}
	}

	public void initializeMask(final Genotype mask, final List<Antenna> antennas) {
		final List<Integer> indices = antennas.stream()
		                                      .filter(a -> !a.active)
		                                      .flatMap(a -> a.neighbours.stream())
		                                      .mapToInt(a -> antennas.indexOf(a)).boxed().collect(Collectors.toList());
		for (int i = 0; i < ranges.length; i++) {
			ranges[i] = indices.contains(i) ? -1 : mask.ranges[i];
		}
	}

}
