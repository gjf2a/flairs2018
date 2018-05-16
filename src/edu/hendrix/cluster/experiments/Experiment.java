package edu.hendrix.cluster.experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.util.Stats;

public class Experiment<C extends Clusterable<C>> {
	private ArrayList<C> examples = new ArrayList<>();
	private ArrayList<Function<Integer,Clusterer<C,C>>> makers = new ArrayList<>();
	private TreeMap<Integer,ArrayList<Clusterer<C,C>>> kToClusterers = new TreeMap<>();
	
	@SafeVarargs
	public Experiment(Function<Integer,Clusterer<C,C>>... makers) {
		for (Function<Integer,Clusterer<C,C>> f: makers) {
			this.makers.add(f);
		}
	}
	
	public int numExamples() {return examples.size();}
	
	public void runAndSave(int numClusters, String[] args, int outputIndex) throws FileNotFoundException {
		for (int k = 2; k <= numClusters; k++) {
			runExpr(k);
		}
		
		if (outputIndex < args.length) {
			PrintStream out = new PrintStream(new File(args[outputIndex]));
			outputMeanDistances(out);
			out.close();
		} else {
			outputMeanDistances(System.out);
		}
	}
	
	public void addExample(C example) {
		examples.add(example);
	}
	
	public void runExpr(int k) {
		ArrayList<Clusterer<C,C>> clusterers = new ArrayList<>();
		kToClusterers.put(k, clusterers);
		for (Function<Integer,Clusterer<C,C>> f: makers) {
			long start = System.currentTimeMillis();
			Clusterer<C,C> c = f.apply(k);
			clusterers.add(c);
			for (C example: examples) {
				c.train(example);
			}
			int size = c.getClusterIds().size(); // trigger training
			System.out.println(getSimpleClassNameOf(c) + ": " + size + " clusters trained");
			long duration = System.currentTimeMillis() - start;
			System.out.println(duration / 1000.0 + " s");
		}
	}
	
	public static String getSimpleClassNameOf(Object obj) {
		String fullName = obj.getClass().getName();
		return fullName.substring(fullName.lastIndexOf('.') + 1);
	}
	
	public void outputMeanDistances(PrintStream out) {
		out.println("k,algorithm,min,max,mean,stdev");
		for (Entry<Integer, ArrayList<Clusterer<C, C>>> k: kToClusterers.entrySet()) {
			for (Clusterer<C, C> clusterer: k.getValue()) {
				ArrayList<Double> diffs = new ArrayList<>();
				for (C example: examples) {
					diffs.add(clusterer.getClosestNodeDistanceFor(example).getSecond());
				}
				Stats s = new Stats(diffs);				
				out.println(k.getKey() + "," + getSimpleClassNameOf(clusterer) + "," + s.min() + "," + s.max() + "," + s.mean() + "," + s.stdev());
			}
		}
	}
}
