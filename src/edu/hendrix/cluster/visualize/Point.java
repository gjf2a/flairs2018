package edu.hendrix.cluster.visualize;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.util.DeepCopyable;

public class Point implements Clusterable<Point>, DeepCopyable<Point> {
	
	private double x, y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static double weightedMean(double a, double b, long aCount, long bCount) {
		return (a * aCount + b * bCount) / (aCount + bCount);
	}

	@Override
	public Point weightedCentroidWith(Point other, long thisCount, long otherCount) {
		return new Point(weightedMean(this.x, other.x, thisCount, otherCount),
				weightedMean(this.y, other.y, thisCount, otherCount));
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	@Override
	public int hashCode() {return toString().hashCode();}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Point) {
			Point that = (Point)other;
			return this.x == that.x && this.y == that.y;
		} else {
			return false;
		}
	}

	@Override
	public Point deepCopy() {
		return new Point(this.x, this.y);
	}
	
	public static double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
	}
}
