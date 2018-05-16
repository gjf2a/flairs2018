package edu.hendrix.cluster.deep;

import static org.junit.Assert.*;

import org.junit.Test;

public class KernelTest {

	@Test
	public void test() {
		Kernel k = new Kernel(3, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		String s = "{1.0,4.0,7.0}{2.0,5.0,8.0}{3.0,6.0,9.0}";
		assertEquals(s, k.toString());
		assertEquals(k, Kernel.fromString(s));
	}

}
