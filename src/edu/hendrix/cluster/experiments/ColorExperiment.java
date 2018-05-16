package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import edu.hendrix.cluster.BSOCSimpleEdge;
import edu.hendrix.cluster.BoundedSelfOrgCluster;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.kmeans.KMeans;
import edu.hendrix.cluster.kmeans.PlusPlusSeed;

public class ColorExperiment extends /*Cached*/Experiment<ColorCluster> {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Usage: ColorExperiment k (imageFile|imageDirectory) [outfile]");
			System.exit(1);
		}
		
		ColorExperiment expr = new ColorExperiment(i -> new PlusPlusSeed<>(i, ColorCluster::distance, x -> x),
				i -> new KMeans<>(i, ColorCluster::distance, x -> x),
				i -> new BoundedSelfOrgCluster<>(i, ColorCluster::distance, x -> x),
				i -> new BSOCSimpleEdge<>(i, ColorCluster::distance, x -> x));
		expr.addAllImagesFrom(new File(args[1]));
		
		expr.runAndSave(Integer.parseInt(args[0]), args, 2);
	}
	
	@SafeVarargs
	public ColorExperiment(Function<Integer,Clusterer<ColorCluster,ColorCluster>>... makers) {
		super(makers);
	}
	/*
	public void addPointsFrom(File imgFile) throws IOException {
		System.out.println("Loading " + imgFile);
		addPointsFrom(ImageIO.read(imgFile));
		System.out.println("loaded");
	}
	
	public void addPointsFrom(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				ColorCluster c = new ColorCluster(img.getRGB(x, y));
				addExample(c);
			}
		}
		System.out.println("Examples: " + this.numExamples());
	}
	*/
	
	public void addAllImagesFrom(File fin) throws IOException {
		System.out.println("Adding all images from " + fin);
		IncrementalExperiment.processAllPoints((img, x, y) -> addExample(new ColorCluster(img.getRGB(x, y))), fin);
		
		/*
		 
		// It should have worked...
		if (fin.getName().endsWith(".mp4")) {
			Java2DFrameConverter converter = new Java2DFrameConverter();
			FFmpegFrameGrabber g = new FFmpegFrameGrabber(fin);
			g.start();
			while (true) {
				Frame f = g.grab();
				if (f == null) {
					break;
				} else {
					addPointsFrom(converter.getBufferedImage(f));
				}
			}
			g.stop();
			g.close();
		} */
	}
}
