package edu.hendrix.cluster.deep;

import java.util.ArrayList;

import edu.hendrix.util.Util;

public class DoubleImage<C extends Enum<C>> implements Convolvable<C> {
	private double[][] img;
	private C[] enums;
	
	private DoubleImage(Class<C> cClass) {
		enums = cClass.getEnumConstants();
	}
	
	public DoubleImage(Class<C> cClass, int w, int h) {
		this(cClass);
		img = new double[w * enums.length][h];
	}
	
	public DoubleImage(Class<C> cClass, String input) {
		this(cClass);
		ArrayList<String> columns = Util.debrace(input);
		img = new double[columns.size()][];
		for (int i = 0; i < columns.size(); i++) {
			String[] nums = columns.get(i).split(",");
			img[i] = new double[nums.length];
			for (int j = 0; j < nums.length; j++) {
				img[i][j] = Double.parseDouble(nums[j]);
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof DoubleImage<?>) {
			DoubleImage<?> that = (DoubleImage<?>)other;
			if (this.enums.length == that.enums.length && 
				this.getWidth() == that.getWidth() &&
				this.getHeight() == that.getHeight()) {
				for (int i = 0; i < getWidth(); i++) {
					for (int j = 0; j < getHeight(); j++) {
						if (this.img[i][j] != that.img[i][j]) {
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
		for (int i = 0; i < img.length; i++) {
			result.append('{');
			for (int j = 0; j < img[i].length; j++) {
				result.append(img[i][j]);
				result.append(',');
			}
			result.deleteCharAt(result.length() - 1);
			result.append('}');
		}
		return result.toString();
	}

	@Override
	public double get(C channel, int x, int y) {
		return img[getX(channel, x)][y];
	}

	@Override
	public void set(C channel, int x, int y, double value) {
		img[getX(channel, x)][y] = value;
	}
	
	int getX(C channel, int x) {
		return x * enums.length + channel.ordinal();
	}

	@Override
	public int getWidth() {
		return img.length / enums.length;
	}

	@Override
	public int getHeight() {
		return img[0].length;
	}
}
