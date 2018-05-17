package edu.hendrix.cluster.experiments;

import java.io.File;
import java.io.IOException;

import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.RandomIncrementalClusterer;
import edu.hendrix.util.Stopwatch;

public class ColorIncrementalExperiment {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Usage: ColorIncrementalExperiment k (imageFile|imageDirectory) [outfile]");
			System.exit(1);
		}
		
		System.out.println("Inputs: k=" + args[0] + ", file=" + args[1]);
		
		System.out.println("Starting to train...");
		Stopwatch timer = new Stopwatch();
		timer.start();
		IncrementalExperiment<ColorCluster> expr = new IncrementalExperiment<ColorCluster>((img, x, y) -> new ColorCluster(img.getRGB(x, y)), 
				Integer.parseInt(args[0]), 
				new File(args[1]),
				i -> new RandomIncrementalClusterer<>(i, ColorCluster::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x));
		timer.stop();
		System.out.println("Total training time: " + timer.getDurationSeconds());
		
		System.out.println("Starting analysis...");
		timer.start();
		expr.outputMeanDistances(System.out);
		System.out.println("Total analysis time: " + timer.getDurationSeconds());
	}
}
