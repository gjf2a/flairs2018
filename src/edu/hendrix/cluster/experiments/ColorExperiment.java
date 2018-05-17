package edu.hendrix.cluster.experiments;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import edu.hendrix.cluster.BSOCSimpleEdge;
import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.RandomIncrementalClusterer;
import edu.hendrix.cluster.kmeans.KMeans;
import edu.hendrix.cluster.kmeans.PlusPlusSeed;

public class ColorExperiment extends Experiment<ColorCluster> {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Usage: ColorExperiment k (imageFile|imageDirectory) [outfile]");
			System.exit(1);
		}
		
		ColorExperiment expr = new ColorExperiment(i -> new PlusPlusSeed<>(i, ColorCluster::distance, x -> x),
				i -> new KMeans<>(i, ColorCluster::distance, x -> x),
				i -> new RandomIncrementalClusterer<>(i, ColorCluster::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x),
				i -> new BSOCSimpleEdge<>(i, ColorCluster::distance, x -> x));
		expr.addAllImagesFrom(new File(args[1]));
		
		expr.runAndSave(Integer.parseInt(args[0]), args, 2);
	}
	
	@SafeVarargs
	public ColorExperiment(Function<Integer,Clusterer<ColorCluster,ColorCluster>>... makers) {
		super(makers);
	}
	
	public void addAllImagesFrom(File fin) throws IOException {
		System.out.println("Adding all images from " + fin);
		PointProducer.makeStreamFrom((img, x, y) -> new ColorCluster(img.getRGB(x, y)), fin).forEach(c -> addExample(c));
	}
}
