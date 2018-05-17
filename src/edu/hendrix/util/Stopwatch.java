package edu.hendrix.util;

public class Stopwatch {
	private long start = 0, end = 1;
	
	public void start() {stop(); start = System.currentTimeMillis();}
	public void stop() {end = System.currentTimeMillis();}
	public long getDurationMillis() {return end - start;}
	public double getDurationSeconds() {
		return getDurationMillis() / 1000.0;
	}
	public boolean isRunning() {return end <= start;}
}
