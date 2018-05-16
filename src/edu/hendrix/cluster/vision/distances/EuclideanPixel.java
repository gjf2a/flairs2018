package edu.hendrix.cluster.vision.distances;

import edu.hendrix.cluster.vision.PixelDistanceFunc;

public class EuclideanPixel implements PixelDistanceFunc {

	@Override
	public double pixelDistance(double a, double b) {
		return Math.pow(b - a, 2);
	}

}
