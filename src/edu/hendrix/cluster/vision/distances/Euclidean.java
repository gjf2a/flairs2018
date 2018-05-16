package edu.hendrix.cluster.vision.distances;

import edu.hendrix.cluster.vision.ImageDistanceFunc;

public class Euclidean extends ImageDistanceFunc {
	public Euclidean() {
		super(new EuclideanPixel());
	}
}
