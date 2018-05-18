package edu.hendrix.cluster.experiments.poster;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.RandomIncrementalClusterer;
import edu.hendrix.cluster.experiments.ColorCluster;
import edu.hendrix.cluster.experiments.Experiment;
import edu.hendrix.cluster.experiments.PointProducer;
import edu.hendrix.cluster.kmeans.KMeans;
import edu.hendrix.cluster.kmeans.PlusPlusSeed;

public class PosterExperiments {
	
	public static void main(String[] args) throws IOException {
		procFiles("single_image_expr.csv", ALL_FILES, FOUR_MAKERS);
		procFiles("short_movie_expr.csv", SHORT_MOVIES, FOUR_MAKERS);
		procFiles("long_movie_expr.csv", LONG_MOVIES, INCR_MAKERS);
	}
	
	public final static ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> FOUR_MAKERS = new ArrayList<>();
	static {
		FOUR_MAKERS.add(i -> new PlusPlusSeed<>(i, ColorCluster::distance, x -> x));
		FOUR_MAKERS.add(i -> new KMeans<>(i, ColorCluster::distance, x -> x));
		FOUR_MAKERS.add(i -> new RandomIncrementalClusterer<>(i, ColorCluster::distance, x -> x));
		FOUR_MAKERS.add(i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x));
	}
	
	public final static ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> INCR_MAKERS = new ArrayList<>();
	static {
		INCR_MAKERS.add(i -> new RandomIncrementalClusterer<>(i, ColorCluster::distance, x -> x));
		INCR_MAKERS.add(i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x));
	}
	
	public final static int[] KS = new int[] {2, 4, 8};
	
	public final static ArrayList<File> ALL_FILES = new ArrayList<>();
	static {
		for (File f: new File("inside_short").listFiles()) {
			ALL_FILES.add(f);
		}
		for (File f: new File("outside_short").listFiles()) {
			ALL_FILES.add(f);
		}
	}
	
	public final static ArrayList<File> SHORT_MOVIES = new ArrayList<>();
	static {
		SHORT_MOVIES.add(new File("inside_short"));
		SHORT_MOVIES.add(new File("outside_short"));
	}
	
	public final static ArrayList<File> LONG_MOVIES = new ArrayList<>();
	static {
		LONG_MOVIES.add(new File("inside"));
		LONG_MOVIES.add(new File("outside"));
	}
	
	public static Stream<ColorCluster> colorsFrom(File fin) throws IOException {
		return PointProducer.makeStreamFrom((img, x, y) -> new ColorCluster(img.getRGB(x, y)), fin);
	}
	
	public static double meanDist(Clusterer<ColorCluster,ColorCluster> clusterer, File fin) throws IOException {
		return colorsFrom(fin).mapToDouble(c -> clusterer.getClosestNodeDistanceFor(c).getSecond()).average().getAsDouble();
	}
	
	public static void procFiles(String outputFileName, ArrayList<File> files, ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> makers) throws IOException {
		if (!outputFileName.endsWith(".csv")) {
			outputFileName += ".csv";
		}
		File output = new File(outputFileName);
		PrintStream fout = new PrintStream(output);
		fout.println("k,algorithm,image,mean");
		for (File imgFile: files) {
			procImgFile(imgFile, fout, makers);
		}
		fout.close();
	}
	
	public static void procImgFile(File imgFile, PrintStream fout, ArrayList<Function<Integer,Clusterer<ColorCluster,ColorCluster>>> makers) throws IOException {
		System.out.println("file: " + imgFile.getName());
		for (int k: KS) {
			System.out.println("k: " + k);
			for (Function<Integer,Clusterer<ColorCluster,ColorCluster>> maker: makers) {
				Clusterer<ColorCluster,ColorCluster> clusterer = maker.apply(k);
				String name = Experiment.getSimpleClassNameOf(clusterer);
				System.out.println("algorithm: " + name);
				colorsFrom(imgFile).forEach(c -> clusterer.train(c));
				fout.println(k + "," + name + "," + imgFile.getName() + "," + meanDist(clusterer, imgFile));
			}
		}		
	}
}
