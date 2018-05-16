package edu.hendrix.cluster.experiments;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.util.DeepCopyable;

public class SimpleValue implements Clusterable<SimpleValue>, DeepCopyable<SimpleValue>{
	private double x;
	
	public SimpleValue(double x) {this.x = x;}
	
	public double get()	{return x;}

	@Override
	public SimpleValue deepCopy() {
		return new SimpleValue(x);
	}

	@Override
	public SimpleValue weightedCentroidWith(SimpleValue other, long thisCount, long otherCount) {
		return new SimpleValue(Clusterable.weightedMean(x, other.x, thisCount, otherCount));
	}
	
	public static double distance(SimpleValue v1, SimpleValue v2) {
		return Math.abs(v1.x - v2.x);
	}
	
	@Override
	public String toString() {
		return "SimpleValue(" + x + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SimpleValue) {
			SimpleValue that = (SimpleValue)other;
			return this.x == that.x;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {return (int)(x * 100);}
}
