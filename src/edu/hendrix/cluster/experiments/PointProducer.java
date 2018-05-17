package edu.hendrix.cluster.experiments;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import edu.hendrix.cluster.Clusterable;

public class PointProducer<C extends Clusterable<C>> {
	private File[] files;
	private int f = 0;
	private BufferedImage img;
	private int x = 0, y = 0;
	private PixelFunc<C> func;
	
	public static <C extends Clusterable<C>> Stream<C> makeStreamFrom(PixelFunc<C> func, File fin) throws IOException {
		PointProducer<C> pp = new PointProducer<>(func, fin);
		return Stream.generate(() -> pp.next()).filter(c -> c.isPresent()).map(c -> c.get()).limit(pp.size());
	}
	
	public static <C extends Clusterable<C>> Stream<C> makeSafeStreamFrom(PixelFunc<C> func, File fin) {
		try {
			return makeStreamFrom(func, fin);
		} catch (IOException exc) {
			System.out.println(exc.getMessage());
			return Stream.empty();
		}
	}
	
	public static boolean isImageFile(File f) {
		return Arrays.stream(ImageIO.getWriterFileSuffixes())
				.anyMatch(s -> f.getName().endsWith(s));
	}
	
	public PointProducer(PixelFunc<C> func, File fin) throws IOException {
		this.func = func;
		if (fin.isDirectory()) {
			files = Arrays.stream(fin.listFiles())
					.filter(f -> isImageFile(f))
					.toArray(File[]::new);
		} else if (isImageFile(fin)){
			files = new File[] {fin};
		} else {
			throw new IllegalArgumentException("No image files available");
		}
		updateImage();
	}
	
	public int size() {
		return img.getWidth() * img.getHeight() * files.length;
	}
	
	private void updateImage() throws IOException {
		img = ImageIO.read(files[f++]);
	}
	
	public Optional<C> next() {
		if (y >= img.getHeight()) {
			y = 0;
			x += 1;
		}
		if (x >= img.getWidth()) {
			try {
				if (f >= files.length) {
					return Optional.empty();
				}
				updateImage();
			} catch (IOException exc) {
				System.out.println("exception: " + exc.getMessage());
				return Optional.empty();
			}
			x = y = 0;
		}
		return Optional.of(func.procPixel(img, x, y++));
	}
}
