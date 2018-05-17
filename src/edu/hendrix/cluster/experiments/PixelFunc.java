package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;

import edu.hendrix.cluster.Clusterable;

public interface PixelFunc<C extends Clusterable<C>> {
	public C procPixel(BufferedImage img, int x, int y);
}
