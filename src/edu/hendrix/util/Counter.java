package edu.hendrix.util;

public class Counter {
	private long count = 0;
	
	public void bump() {count += 1;}
	
	public long get() {return count;}
}
