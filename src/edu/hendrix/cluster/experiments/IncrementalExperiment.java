package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.util.Counter;

public class IncrementalExperiment<C extends Clusterable<C>> {
	private TreeMap<Integer,ArrayList<Clusterer<C,C>>> kToClusterers = new TreeMap<>();
	
	public IncrementalExperiment(PixelFunc<C> proc, int numClusters, File fin, @SuppressWarnings("unchecked") Function<Integer,Clusterer<C,C>>... makers) throws IOException {
		for (int k = 2; k <= numClusters; k++) {
			ArrayList<Clusterer<C,C>> clusterers = new ArrayList<>();
			kToClusterers.put(k, clusterers);
			for (Function<Integer,Clusterer<C,C>> f: makers) {
				clusterers.add(f.apply(k));
			}
		}
		
		processAllPoints((img, x, y) -> {
			for (int k = 2; k <= numClusters; k++) {
				for (Clusterer<C,C> c: kToClusterers.get(k)) {
					c.train(proc.procPixel(img, x, y));
				}
			}
		}, fin);
		
		
	}
	
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
