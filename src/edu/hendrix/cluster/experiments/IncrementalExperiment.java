package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.hendrix.cluster.Clusterable;

public class IncrementalExperiment<C extends Clusterable<C>> {
	public static void processAllPoints(ImageFunc proc, File fin) throws IOException {
		if (fin.isDirectory()) {
			for (File file: fin.listFiles()) {
				processAllPoints(proc, file);
			}
		} else {
			for (String suffix: ImageIO.getWriterFileSuffixes()) {
				if (fin.getName().endsWith(suffix)) {
					processImage(proc, fin);
				}
			}
		}
	}
	
	private static void processImage(ImageFunc proc, File imgFile) throws IOException {
		BufferedImage img = ImageIO.read(imgFile);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				proc.procPixel(img, x, y);
			}
		}
	}
}
