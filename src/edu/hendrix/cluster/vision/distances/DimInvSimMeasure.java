package edu.hendrix.cluster.vision.distances;

import edu.hendrix.cluster.vision.ImageDistanceFunc;

public class DimInvSimMeasure extends ImageDistanceFunc {
	public DimInvSimMeasure() {
		super(new DimInvSimMeasurePixel());
	}
}
