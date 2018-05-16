package edu.hendrix.cluster.deep;

public interface Convolvable<C extends Enum<C>> {
	public double get(C channel, int x, int y);
	public void set(C channel, int x, int y, double value);
	public int getWidth();
	public int getHeight();
}
