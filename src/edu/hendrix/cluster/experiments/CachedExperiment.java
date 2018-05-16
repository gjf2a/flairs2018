package edu.hendrix.cluster.experiments;

import java.util.HashMap;
import java.util.function.Function;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.Clusterer;

public class CachedExperiment<C extends Clusterable<C>> extends Experiment<C> {
	private HashMap<C,C> cache = new HashMap<>();

	@SafeVarargs
	public CachedExperiment(Function<Integer,Clusterer<C,C>>... makers) {
		super(makers);
	}
	
	@Override
	public void addExample(C example) {
		if (!cache.containsKey(example)) {
			cache.put(example, example);
		}
		super.addExample(cache.get(example));
	}
}
