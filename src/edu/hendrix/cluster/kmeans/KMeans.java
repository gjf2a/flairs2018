package edu.hendrix.cluster.kmeans;

import java.util.ArrayList;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.DistanceFunc;
import edu.hendrix.util.DeepCopyable;
import edu.hendrix.util.Util;

public class KMeans<C extends Clusterable<C> & DeepCopyable<C>, I> extends PlusPlusSeed<C, I> {

	public KMeans(int k, DistanceFunc<C> dist, Function<I,C> transformer) {
		super(k, dist, transformer);
	}
	
	@Override
	public void performTraining() { 
		super.performTraining();
		kMeans();
	}
	
	private void kMeans() {
		ArrayList<ArrayList<C>> matches = makeEmptyMatches();
		for (C example: examples) {
			matches.get(findClosest(example)).add(example);
		}
		boolean moved = true;
		while (moved) {
			rebuildClusters(matches);
			ArrayList<ArrayList<C>> updated = makeEmptyMatches();
			moved = false;
			for (int i = 0; i < matches.size(); i++) {
				for (int j = 0; j < matches.get(i).size(); j++) {
					C example = matches.get(i).get(j);
					int best = findClosest(example);
					if (best != i) {
						moved = true;
					}
					updated.get(best).add(example);
				}
			}
			matches = updated;
		}
	}

	private int findClosest(C candidate) {
		int best = 0;
		double bestDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < clusters.size(); i++) {
			double distance = dist.distance(clusters.get(i), candidate);
			if (distance < bestDist) {
				bestDist = distance;
				best = i;
			}
		}
		return best;
	}

	private ArrayList<ArrayList<C>> makeEmptyMatches() {
		ArrayList<ArrayList<C>> matches = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			matches.add(new ArrayList<>());
		}
		return matches;
	}

	private void rebuildClusters(ArrayList<ArrayList<C>> matches) {
		Util.assertArgument(matches.size() == clusters.size(), "Mismatched sizes of clusters and match lists");
		for (int i = 0; i < clusters.size(); i++) {
			clusters.set(i, matches.get(i).size() > 0 
					? Clusterable.clusterOf(matches.get(i)) 
					: examples.get((int)(Math.random() * examples.size())));
		}
	}
}
