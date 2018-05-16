package edu.hendrix.util;

import java.util.ArrayList;
import java.util.Collections;

public class Stats {
	private ArrayList<Double> values = new ArrayList<>();
	
	public Stats(double[] values) {
		for (double v: values) {this.values.add(v);}
	}
	
	public Stats(Iterable<Double> values) {
		for (double v: values) {this.values.add(v);}
	}
	
	public double size() {
		return values.size();
	}
	
	public double min() {
		return values.stream().reduce(Double.POSITIVE_INFINITY, (r, e) -> Math.min(r, e));
	}
	
	public double max() {
		return values.stream().reduce(Double.NEGATIVE_INFINITY, (r, e) -> Math.max(r, e));
	}
	
	public double mean() {
		Util.assertState(values.size() >= 1, "No values");
		return values.stream().reduce(0.0, (x, y) -> x + y) / values.size();
	}
	
	public double variance() {
		double mean = mean();
		return values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).sum() / (values.size() - 1);
	}
	
	public double stdev() {
		return Math.sqrt(variance());
	}
	
	public String normalSummary() {
		return "[" + min() + ", " + mean() + " (+/- " + stdev() + "), " + max() + "]";
	}
	
	public double median() {
		Util.assertState(values.size() >= 1, "No values");
		ArrayList<Double> sorted = new ArrayList<>(values);
		Collections.sort(sorted);
		int middle = sorted.size() / 2;
		if (sorted.size() % 2 == 1) {
			return sorted.get(middle);
		} else {
			return (sorted.get(middle - 1) + sorted.get(middle)) / 2.0;
		}
	}
}
