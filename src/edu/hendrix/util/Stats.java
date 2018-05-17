package edu.hendrix.util;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

public class Stats {
	private Supplier<DoubleStream> valueSupplier;
	
	public Stats(Supplier<DoubleStream> valueSupplier) {
		this.valueSupplier = valueSupplier;
	}
	
	public Stats(double[] values) {
		this(() -> Arrays.stream(values));
	}
	
	public Stats(Iterable<Double> values) {
		this(() -> StreamSupport.stream(values.spliterator(), true).mapToDouble(d -> d));
	}
	
	public double size() {
		return valueSupplier.get().count();
	}
	
	public double min() {
		return valueSupplier.get().reduce(Double.POSITIVE_INFINITY, (r, e) -> Math.min(r, e));
	}
	
	public double max() {
		return valueSupplier.get().reduce(Double.NEGATIVE_INFINITY, (r, e) -> Math.max(r, e));
	}
	
	public double mean() {
		Util.assertState(size() >= 1, "No values");
		return valueSupplier.get().reduce(0.0, (x, y) -> x + y) / size();
	}
	
	public double variance() {
		double mean = mean();
		return valueSupplier.get().map(v -> Math.pow(v - mean, 2)).sum() / (size() - 1);
	}
	
	public double stdev() {
		return Math.sqrt(variance());
	}
	
	public String normalSummary() {
		return "[" + min() + ", " + mean() + " (+/- " + stdev() + "), " + max() + "]";
	}
	
	public double median() {
		Util.assertState(size() >= 1, "No values");
		double[] sorted = valueSupplier.get().sorted().toArray();
		int middle = sorted.length / 2;
		if (sorted.length % 2 == 1) {
			return sorted[middle];
		} else {
			return (sorted[middle - 1] + sorted[middle]) / 2.0;
		}
	}
}
