package edu.hendrix.cluster.kmeans;

import java.util.ArrayList;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.DistanceFunc;
import edu.hendrix.util.DeepCopyable;
import edu.hendrix.util.Distribution;
import edu.hendrix.util.Util;

public class PlusPlusSeed<C extends Clusterable<C> & DeepCopyable<C>, I> implements Clusterer<C,I>, DeepCopyable<PlusPlusSeed<C,I>> {

	protected ArrayList<C> examples;
	protected ArrayList<C> clusters;
	private int k;
	protected DistanceFunc<C> dist;
	private Function<I,C> transformer;

	public PlusPlusSeed(int k, DistanceFunc<C> dist, Function<I,C> transformer) {
		this.dist = dist;
		this.transformer = transformer;
		this.k = k;
		examples = new ArrayList<>();
		clusters = new ArrayList<>();
	}

	public void performTraining() {
		Util.assertState(!trained(), "Already trained");
		Util.assertState(k <= examples.size(), "Not enough examples for " + k + " clusters.");
		
		initKPlusPlusClusters();
	}

	private void initKPlusPlusClusters() {
		ArrayList<C> candidates = new ArrayList<>(examples);
		clusters.add(candidates.remove((int)(Math.random() * candidates.size())));
		while (clusters.size() < k) {
			Distribution d = new Distribution(makeDistroFrom(clusters, candidates));
			clusters.add(candidates.remove(d.pick()));		
		}
	}

	public double[] makeDistroFrom(ArrayList<C> clusters, ArrayList<C> candidates) {
		double[] result = new double[candidates.size()];
		for (int i = 0; i < candidates.size(); i++) {
			C candidate = candidates.get(i);
			result[i] = 0;
			for (C cluster: clusters) {
				double distance = dist.distance(candidate, cluster);
				if (distance > result[i]) {
					result[i] = distance;
				}
			}
			result[i] = Math.pow(result[i], 2);
		}
		return result;
	}
	
	@Override
	public PlusPlusSeed<C, I> deepCopy() {
		PlusPlusSeed<C,I> copy = new PlusPlusSeed<>(k, dist, transformer);
		for (C example: examples) {
			copy.examples.add(example.deepCopy());
		}
		for (C cluster: clusters) {
			copy.clusters.add(cluster.deepCopy());
		}
		return copy;
	}

	@Override
	public int train(I example) {
		Util.assertState(!trained(), "Already trained");
		examples.add(transform(example));
		return -1;
	}

	@Override
	public double distance(C one, C two) {
		return dist.distance(one, two);
	}

	@Override
	public C transform(I input) {
		return transformer.apply(input);
	}

	@Override
	public C getIdealInputFor(int node) {
		if (!trained()) {
			performTraining();
		}
		return clusters.get(node);
	}

	public boolean trained() {
		return size() > 0;
	}

	@Override
	public int size() {
		return clusters.size();
	}

	@Override
	public ArrayList<Integer> getClusterIds() {
		if (!trained()) {
			performTraining();
		}
		ArrayList<Integer> ids = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			ids.add(i);
		}
		return ids;
	}

}