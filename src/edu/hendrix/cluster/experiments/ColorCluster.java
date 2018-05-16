package edu.hendrix.cluster.experiments;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.util.DeepCopyable;
import edu.hendrix.util.Util;

public class ColorCluster implements Clusterable<ColorCluster>, DeepCopyable<ColorCluster> {

	private double red, green, blue;
	
	public ColorCluster(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public ColorCluster(int rgb) {
		this(Util.getRed(rgb), Util.getGreen(rgb), Util.getBlue(rgb));
	}
	
	@Override
	public String toString() {
		return "ColorCluster(" + red + "," + green + "," + blue + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ColorCluster) {
			ColorCluster that = (ColorCluster)other;
			return this.red == that.red && this.green == that.green && this.blue == that.blue;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)red << 16 + (int)green << 8 + (int)blue;
	}
	
	@Override
	public ColorCluster deepCopy() {
		return new ColorCluster(red, green, blue);
	}

	@Override
	public ColorCluster weightedCentroidWith(ColorCluster other, long thisCount, long otherCount) {
		return new ColorCluster(Clusterable.weightedMean(this.red, other.red, thisCount, otherCount),
				Clusterable.weightedMean(this.green, other.green, thisCount, otherCount),
				Clusterable.weightedMean(this.blue, other.blue, thisCount, otherCount));
	}

	public static double distance(ColorCluster c1, ColorCluster c2) {
		Util.assertArgument(c1 != null, "c1 null");
		Util.assertArgument(c2 != null, "c2 null");
		return Math.pow(c1.red - c2.red, 2) + Math.pow(c1.green - c2.green, 2) + Math.pow(c1.blue - c2.blue, 2);
	}
}
