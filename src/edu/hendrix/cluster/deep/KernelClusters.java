package edu.hendrix.cluster.deep;

import edu.hendrix.util.*;
import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.vision.PixelDistanceFunc;
import edu.hendrix.cluster.vision.distances.DimInvSimMeasurePixel;

public class KernelClusters {
	private BoundedSelfOrgCluster<Kernel,Kernel> kernels;
	private int kernelSize;
	
	private static final PixelDistanceFunc PIX_DIST = new DimInvSimMeasurePixel();
	
	public KernelClusters(int numClusters, int kernelSize) {
		kernels = new BoundedSelfOrgCluster<>(numClusters, (k1, k2) -> {
			double total = 0.0;
			Util.assertArgument(k1.size() == k2.size(), "Mismatched kernel sizes");
			for (int i = 0; i < k1.size(); i++) {
				for (int j = 0; j < k1.size(); j++) {
					total += PIX_DIST.pixelDistance(k1.get(i, j), k2.get(i, j));
				}
			}
			return total / Math.pow(k1.size(), 2);
		}, i -> i);
		this.kernelSize = kernelSize;
	}
	
	public <C extends Enum<C>> void trainFrom(Convolvable<C> img, C channel) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				kernels.train(new Kernel(kernelSize, img, channel, i, j));
			}
		}
	}
	
	public <C extends Enum<C>> DoubleImage<One> applyTo(Convolvable<C> img, C channel) {
		DoubleImage<One> result = new DoubleImage<>(One.class, img.getWidth(), img.getHeight());
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Kernel segment = new Kernel(kernelSize, img, channel, i, j);
				result.set(One.ONLY, i, j, kernels.getClosestNodeDistanceFor(segment).getSecond());
			}
		}
		return result;
	}
}
