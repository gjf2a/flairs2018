package edu.hendrix.cluster.experiments;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColorClusterTest {

	@Test
	public void test() {
		ColorCluster c1 = new ColorCluster(100, 50, 20);
		ColorCluster c2 = new ColorCluster(200, 100, 40);
		assertEquals(new ColorCluster(150, 75, 30), c1.weightedCentroidWith(c2, 1, 1));
	}

}
