package edu.hendrix.cluster.vision;

import java.awt.image.BufferedImage;

import edu.hendrix.cluster.DistanceFunc;

public class ImageDistanceFunc implements DistanceFunc<BufferedImage> {
	private PixelDistanceFunc pixDist;
	
	public ImageDistanceFunc(PixelDistanceFunc pixDist) {
		this.pixDist = pixDist;
	}
	
	@Override
	public double distance(BufferedImage img1, BufferedImage img2) {
		double total = 0.0;
		for (int x = 0; x < img1.getWidth(); ++x) {
			for (int y = 0; y < img1.getHeight(); ++y) {
				total += RGB.zip(new RGB(img1.getRGB(x, y)), new RGB(img2.getRGB(x, y)), 
						(p1, p2) -> pixDist.pixelDistance(p1, p2)).sum();
			}
		}
		return total;
	}
}
