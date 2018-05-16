package edu.hendrix.cluster.deep;

import java.util.ArrayList;
import java.util.function.BiFunction;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.util.DeepCopyable;
import edu.hendrix.util.Util;

public class Kernel implements Clusterable<Kernel>, DeepCopyable<Kernel> {
	private double[][] kernel;
	
	public Kernel(int size, double... values) {
		kernel = new double[size][];
		for (int i = 0; i < size; i++) {
			kernel[i] = new double[size];
		}
		if (values.length > 0) {
			Util.assertArgument(size*size == values.length, "Kernel values don't match size");
			int i = 0, j = 0;
			for (double v: values) {
				kernel[i][j] = v;
				i += 1;
				if (i == size) {
					i = 0;
					j += 1;
				}
			}
		}
	}
	
	public <C extends Enum<C>> Kernel(int size, Convolvable<C> src, C channel, int xCenter, int yCenter) {
		this(size);
		int xStart = xCenter - size/2;
		int yStart = yCenter - size/2;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				kernel[i][j] = getValue(src, channel, xStart + i, yStart + j);
			}
		}
	}
	
	public static Kernel fromString(String src) {
		ArrayList<String> columns = Util.debrace(src);
		Kernel result = new Kernel(columns.size());
		for (int i = 0; i < result.size(); i++) {
			String[] values = columns.get(i).split(",");
			for (int j = 0; j < result.size(); j++) {
				result.kernel[i][j] = Double.parseDouble(values[j]);
			}
		}
		return result;
	}
	
	public int size() {
		return kernel.length;
	}
	
	public double get(int x, int y) {
		return kernel[x][y];
	}
	
	public static <C extends Enum<C>> double getValue(Convolvable<C> image, C channel, int x, int y) {
		if (x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight()) {
			return image.get(channel, x, y);
		} else {
			return 0;
		}
	}
	
	public <C extends Enum<C>> double applyTo(Convolvable<C> image, C channel, int x, int y) { 
		double sum = 0;
		int xStart = x - size()/2;
		int yStart = y - size()/2;
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				sum += kernel[i][j] * getValue(image, channel, xStart+i, yStart+j);
			}
		}
		return sum;
	}
	
	public <C extends Enum<C>, I extends Convolvable<C>> I createFrom(Class<C> channelClass, Convolvable<C> src, BiFunction<Integer,Integer,I> imageMaker) {
		I result = imageMaker.apply(src.getWidth(), src.getHeight());
		for (int i = 0; i < result.getWidth(); i++) {
			for (int j = 0; j < result.getHeight(); j++) {
				for (C c: channelClass.getEnumConstants()) {
					result.set(c, i, j, applyTo(src, c, i, j));
				}
			}
		}
		return result;
	}

	@Override
	public Kernel weightedCentroidWith(Kernel other, long thisCount, long otherCount) {
		Util.assertArgument(other.size() == this.size(), "Kernels are not the same size: this (" + size() + "); other (" + other.size() + ")");
		Kernel result = new Kernel(size());
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				result.kernel[i][j] = (thisCount * this.kernel[i][j] + otherCount * other.kernel[i][j]) / (thisCount + otherCount);
			}
		}
		return result;
	}

	@Override
	public Kernel deepCopy() {
		Kernel result = new Kernel(size());
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < size(); j++) {
				result.kernel[i][j] = this.kernel[i][j];
			}
		}
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Kernel) {
			Kernel that = (Kernel)other;
			if (this.size() == that.size()) {
				for (int i = 0; i < size(); i++) {
					for (int j = 0; j < size(); j++) {
						if (this.kernel[i][j] != that.kernel[i][j]) {
							return false;
						}
					}
				}
				return true;
			}
		} 
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			result.append('{');
			for (int j = 0; j < size(); j++) {
				result.append(kernel[i][j]);
				result.append(',');
			}
			result.replace(result.length() - 1, result.length(), "}");
		}
		return result.toString();
	}
}
