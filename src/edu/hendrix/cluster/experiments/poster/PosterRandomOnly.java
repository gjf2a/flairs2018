package edu.hendrix.cluster.experiments.poster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.RandomIncrementalClusterer;
import edu.hendrix.cluster.experiments.ColorCluster;

public class PosterRandomOnly {
	
	public static ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> makeRandoms(int n) {
		ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> result = new ArrayList<>();
		for (int k = 0; k < n; k++) {
			result.add(i -> new RandomIncrementalClusterer<>(i, ColorCluster::distance, x -> x));
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		int numClusterers = args.length > 0 ? Integer.parseInt(args[0]) : 10;
		PosterExperiments.procFiles("short_movie_random", PosterExperiments.SHORT_MOVIES, makeRandoms(numClusterers));
		PosterExperiments.procFiles("long_movie_random", PosterExperiments.LONG_MOVIES, makeRandoms(numClusterers));
	}
}
