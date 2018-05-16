package edu.hendrix.cluster.vision;

import static org.junit.Assert.*;

import org.junit.Test;

public class RGBTest {

	@Test
	public void test() {
		assertTrue(new RGB(0xffffffff).allChannels().allMatch(d -> d == 1.0));
		assertTrue(new RGB(0xff000000).allChannels().allMatch(d -> d == 0.0));
		
		RGB rgb = new RGB(0xffa0f020);
		assertEquals((double)0xa0 / 0xff, rgb.getRed(), 0.0001);
		assertEquals((double)0xf0 / 0xff, rgb.getGreen(), 0.0001);
		assertEquals((double)0x20 / 0xff, rgb.getBlue(), 0.0001);
	}

}
