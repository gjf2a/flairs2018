package edu.hendrix.cluster.experiments;

import java.io.FileNotFoundException;
import java.util.function.Function;

import edu.hendrix.cluster.BSOCSimpleEdge;
import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.kmeans.KMeans;
import edu.hendrix.cluster.kmeans.PlusPlusSeed;

public class OneDExperiment extends Experiment<SimpleValue> {
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 2) {
			System.out.println("Usage: OneDExperiment numClusters numValues [outfile.csv]");
			System.exit(1);
		}
		int numValues = Integer.parseInt(args[1]);
		
		OneDExperiment expr = new OneDExperiment(numValues, 
				i -> new PlusPlusSeed<>(i, SimpleValue::distance, x -> x),
				i -> new KMeans<>(i, SimpleValue::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, SimpleValue::distance, x -> x),
				i -> new BSOCSimpleEdge<>(i, SimpleValue::distance, x -> x));
		
		expr.runAndSave(Integer.parseInt(args[0]), args, 2);
	}
	
	@SafeVarargs
	public OneDExperiment(int n, Function<Integer,Clusterer<SimpleValue,SimpleValue>>... functions) {
		super(functions);
		for (int i = 0; i < n; i++) {
			addExample(new SimpleValue(i));
		}
	}
	
	@SafeVarargs
	public OneDExperiment(int numValues, int repetitionsPerValue, Function<Integer,Clusterer<SimpleValue,SimpleValue>>... functions) {
		super(functions);
		for (int i = 0; i < numValues; i++) {
			for (int j = 0; j < repetitionsPerValue; j++) {
				addExample(new SimpleValue(i));
			}
		}
	}
}
