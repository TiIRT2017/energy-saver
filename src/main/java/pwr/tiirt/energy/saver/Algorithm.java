package pwr.tiirt.energy.saver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Algorithm {

	private final List<Antenna> antennas;
	private final List<Genotype> population;
	private final int maxIterations;
	private final int populationSize;
	private final double mutationProbability;
	private final double crossoverProbability;
	private final double maxRange;
	private final Genotype mask;

	private final List<Double> minimums = new ArrayList<>();
	private final List<Double> maximums = new ArrayList<>();
	private final List<Double> averages = new ArrayList<>();
	private final List<Genotype> bestGenotypes = new ArrayList<>();

	public Algorithm(final List<Antenna> antennas, final int populationSize, final int maxIterations, final double mutationProbability,
			final double crossoverProbability, final double maxRange) {
		this.antennas = Collections.unmodifiableList(antennas);
		this.populationSize = populationSize;
		this.population = new ArrayList<>(populationSize);
		this.maxIterations = maxIterations;
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		this.mask = new Genotype(antennas.size());
		this.maxRange = maxRange;
		initialize();
	}

	public Algorithm(final List<Antenna> antennas, final Genotype mask, final int populationSize, final int maxIterations, final double mutationProbability,
	                 final double crossoverProbability, final double maxRange) {
		this.antennas = Collections.unmodifiableList(antennas);
		this.populationSize = populationSize;
		this.population = new ArrayList<>(populationSize);
		this.maxIterations = maxIterations;
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		this.mask = new Genotype(antennas.size());
		this.maxRange = maxRange;
		initialize(mask);
	}

	private void initialize() {
		mask.initializeMask();
		for (int i = 0; i < populationSize; i++) {
			population.add(new Genotype(antennas.size(), maxRange));
		}
		evaluate();
	}

	private void initialize(final Genotype mask) {
		mask.initializeMask(mask, antennas);
		for (int i = 0; i < populationSize; i++) {
			population.add(new Genotype(mask));
		}
		evaluate();
	}

	private void evaluate() {
		//TODO: population.parallelStream().forEach(g -> calculateFunction());
		population.sort(scoreComparator());
		saveResults();
	}

	private Comparator<Genotype> scoreComparator() {return (g1, g2) -> (int) Math.signum(g1.score - g2.score);}

	private void saveResults() {
		minimums.add(population.get(0).score);
		maximums.add(population.get(population.size() - 1).score);
		averages.add(population.stream().mapToDouble(g -> g.score).sum() / population.size());
		bestGenotypes.add(population.get(0));
	}

	public void solve() {
		int iterations = 0;
		while (notFinished(iterations++)) {
			mutate();
			cross();
			evaluate();
			select();
		}
	}

	private boolean notFinished(final int iterations) {
		final double currentMin = minimums.get(minimums.size() - 1);
		return currentMin > 0.0 && iterations < maxIterations && !population.isEmpty();
	}

	private void mutate() {
		population.parallelStream().forEach(g -> mutate(g));
	}

	private void mutate(final Genotype g) {
		for (int i = 0; i < g.ranges.length; i++)
			if (mask.ranges[i] < 0.0 && ThreadLocalRandom.current().nextDouble() < mutationProbability)
				g.ranges[i] = ThreadLocalRandom.current().nextDouble(maxRange);
	}


	private void cross() {
		final List<Genotype> newPopulation = Collections.synchronizedList(new ArrayList<>((int) (populationSize * (1 + 2 * crossoverProbability))));
		population.parallelStream().forEach(g -> cross(g, newPopulation));
		population.addAll(newPopulation);
	}

	private void cross(final Genotype g, final List<Genotype> newPopulation) {
		if (ThreadLocalRandom.current().nextDouble() < calculateCrossingProbability(g.score)) {
			final Genotype other = selectDifferent(g);
			final int noOfAntennas = antennas.size();
			createOffspring(g, newPopulation, other, noOfAntennas);
		}
	}

	private double calculateCrossingProbability(final double score) {
		final double currentMin = minimums.get(minimums.size() - 1);
		final double currentMax = maximums.get(maximums.size() - 1);
		final double factor = 1.0 - (currentMax - score) /  (currentMax - currentMin);
		return Math.min(crossoverProbability * (0.5 + factor), 1.0);
	}

	private Genotype selectDifferent(final Genotype... genotypes) {
		Genotype g;
		do {
			g = population.get(ThreadLocalRandom.current().nextInt(0, population.size()));
		} while (Arrays.stream(genotypes).noneMatch(other -> g == other));
		return g;
	}

	private void createOffspring(final Genotype g, final List<Genotype> newPopulation, final Genotype other, final int noOfAntennas) {
		final Genotype newGenotype1 = new Genotype(noOfAntennas);
		final Genotype newGenotype2 = new Genotype(noOfAntennas);
		final int i = ThreadLocalRandom.current().nextInt(noOfAntennas);
		for (int j = 0; j < noOfAntennas; j++) {
			if (mask.ranges[j] < 0.0) {
				if (j < i) {
					newGenotype1.ranges[j] = g.ranges[j];
					newGenotype2.ranges[j] = other.ranges[j];
				}
				else {
					newGenotype1.ranges[j] = other.ranges[j];
					newGenotype2.ranges[j] = g.ranges[j];
				}
			} else {
				newGenotype1.ranges[j] = mask.ranges[j];
				newGenotype2.ranges[j] = mask.ranges[j];
			}
		}
		newPopulation.add(newGenotype1);
		newPopulation.add(newGenotype2);
	}

	private void select() {
		final List<Genotype> newPopulation = Collections.synchronizedList(new ArrayList<>(populationSize));
		IntStream.range(0, populationSize).parallel().forEach(i -> bestOfThree(newPopulation));
		population.clear();
		population.addAll(newPopulation);
	}

	private void bestOfThree(final List<Genotype> newPopulation) {
		final Genotype g1 = selectDifferent();
		final Genotype g2 = selectDifferent(g1);
		final Genotype g3 = selectDifferent(g1, g2);
		chooseWinner(newPopulation, g1, g2, g3);
	}

	private void chooseWinner(final List<Genotype> newPopulation, final Genotype g1, final Genotype g2, final Genotype g3) {
		final Genotype winner = Arrays.asList(g1, g2, g3).stream().sorted(scoreComparator()).findFirst().get();
		newPopulation.add(new Genotype(winner));
	}

	public List<Double> getMinimums() {
		return minimums;
	}

	public List<Double> getMaximums() {
		return maximums;
	}

	public List<Double> getAverages() {
		return averages;
	}

	public List<Genotype> getBestGenotypes() {
		return bestGenotypes;
	}

}