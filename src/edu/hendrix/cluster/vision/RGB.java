package edu.hendrix.cluster.vision;

import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

public class RGB {
	private double[] rgb = new double[3];
	
	public RGB(int bufferedImageValue) {
		for (int i = 0; i < rgb.length; i++) {
			rgb[i] = (0xff & (bufferedImageValue >> (8 * (rgb.length - i - 1)))) / (double)0xff;
		}
	}
	
	public double getRed()   {return rgb[0];}
	public double getGreen() {return rgb[1];}
	public double getBlue()  {return rgb[2];}
	
	public DoubleStream allChannels() {
		return Arrays.stream(rgb);
	}
	
	public static DoubleStream zip(RGB one, RGB two, DoubleBinaryOperator op) {
		double[] result = new double[Math.min(one.rgb.length, two.rgb.length)];
		for (int i = 0; i < result.length; i++) {
			result[i] = op.applyAsDouble(one.rgb[i], two.rgb[i]);
		}
		return Arrays.stream(result);
	}
}
