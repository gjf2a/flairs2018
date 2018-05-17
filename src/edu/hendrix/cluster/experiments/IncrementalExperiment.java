package edu.hendrix.cluster.experiments;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.util.Stats;

public class IncrementalExperiment<C extends Clusterable<C>> {
	private TreeMap<Integer,ArrayList<Clusterer<C,C>>> kToClusterers = new TreeMap<>();
	private File fin;
	private PixelFunc<C> proc;
	
	@SafeVarargs
	public IncrementalExperiment(PixelFunc<C> proc, int numClusters, File fin, Function<Integer,Clusterer<C,C>>... makers) throws IOException {
		this.fin = fin;
		this.proc = proc;
		
		for (int k = 2; k <= numClusters; k++) {
			ArrayList<Clusterer<C,C>> clusterers = new ArrayList<>();
			kToClusterers.put(k, clusterers);
			for (Function<Integer,Clusterer<C,C>> f: makers) {
				clusterers.add(f.apply(k));
			}
		}

		PointProducer.makeStreamFrom(proc, fin).forEach(p -> {
			for (int k = 2; k <= numClusters; k++) {
				for (Clusterer<C,C> c: kToClusterers.get(k)) {
					c.train(p);
				}
			}
		});
	}
	
	public void outputMeanDistances(PrintStream out) {
		out.println("k,algorithm,min,max,mean,stdev");
		for (Entry<Integer, ArrayList<Clusterer<C, C>>> k: kToClusterers.entrySet()) {
			for (Clusterer<C, C> clusterer: k.getValue()) {
				Stats s = new Stats(() -> PointProducer.makeSafeStreamFrom(proc, fin).mapToDouble(example -> clusterer.getClosestNodeDistanceFor(example).getSecond()));
				out.println(k.getKey() + "," + Experiment.getSimpleClassNameOf(clusterer) + "," + s.min() + "," + s.max() + "," + s.mean() + "," + s.stdev());				
			}
		}
	}
}
