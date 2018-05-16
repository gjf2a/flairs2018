package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;

public interface ImageFunc {
	public void procPixel(BufferedImage img, int x, int y);
}
