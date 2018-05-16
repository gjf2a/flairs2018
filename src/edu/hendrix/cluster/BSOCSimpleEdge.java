package edu.hendrix.cluster;

import java.util.function.Function;

import edu.hendrix.util.DeepCopyable;

public class BSOCSimpleEdge<C extends Clusterable<C> & DeepCopyable<C>, I> extends BoundedSelfOrgCluster<C,I> {

	public BSOCSimpleEdge(int maxNumNodes, DistanceFunc<C> dist, Function<I, C> transformer) {
		super(maxNumNodes, dist, transformer);
	}

	@Override
	double distance(Node<C> n1, Node<C> n2) {
		return distance(n1.getCluster(), n2.getCluster());
	}
}
