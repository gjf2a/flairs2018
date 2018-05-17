package edu.hendrix.cluster;

import java.util.ArrayList;
import java.util.function.Function;

import edu.hendrix.util.DeepCopyable;
import edu.hendrix.util.FixedSizeArray;

public class RandomIncrementalClusterer<C extends Clusterable<C> & DeepCopyable<C>, I> implements Clusterer<C,I>, DeepCopyable<RandomIncrementalClusterer<C,I>> {
	private FixedSizeArray<C> nodes;
	private int opening = 0, attempts = 0;
	private DistanceFunc<C> dist;
	private Function<I,C> transformer;
	
	public RandomIncrementalClusterer(int maxNumNodes, DistanceFunc<C> dist, Function<I,C> transformer) {
		nodes = FixedSizeArray.make(maxNumNodes);
		this.dist = dist;
		this.transformer = transformer;
	}
	
	@Override
	public RandomIncrementalClusterer<C, I> deepCopy() {
		RandomIncrementalClusterer<C,I> ric = new RandomIncrementalClusterer<>(nodes.capacity(), dist, transformer);
		ric.opening = this.opening;
		ric.attempts = this.attempts;
		for (int i = 0; i < opening; i++) {
			ric.nodes.put(i, this.nodes.get(i).deepCopy());
		}
		return ric;
	}

	@Override
	public int train(I example) {
		attempts += 1;
		C added = transform(example);
		if (opening < nodes.capacity()) {
			nodes.put(opening++, added);
		} else {
			int where = (int)(Math.random() * attempts);
			if (where < nodes.capacity()) {
				nodes.put(where, added);
			}
		}
		return getClosestMatchFor(example);
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
		return nodes.get(node);
	}

	@Override
	public int size() {
		return opening;
	}

	@Override
	public ArrayList<Integer> getClusterIds() {
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < opening; i++) {
			result.add(i);
		}
		return result;
	}
}
