package edu.hendrix.cluster.deep;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DoubleImageTest {

	@Test
	public void setupTest() {
		DoubleImage<Two> simple = new DoubleImage<>(Two.class, 3, 2);
		simple.set(Two.INTENSITY, 0, 0, 0.08);
		simple.set(Two.COLOR, 0, 0, 0.17);
		simple.set(Two.INTENSITY, 0, 1, 0.25);
		simple.set(Two.COLOR, 0, 1, 0.33);
		simple.set(Two.INTENSITY, 1, 0, 0.41);
		simple.set(Two.COLOR, 1, 0, 0.5);
		simple.set(Two.INTENSITY, 1, 1, 0.58);
		simple.set(Two.COLOR, 1, 1, 0.67);
		simple.set(Two.INTENSITY, 2, 0, 0.75);
		simple.set(Two.COLOR, 2, 0, 0.83);
		simple.set(Two.INTENSITY, 2, 1, 0.91);
		simple.set(Two.COLOR, 2, 1, 1.0);
		
		assertEquals("{0.08,0.25}{0.17,0.33}{0.41,0.58}{0.5,0.67}{0.75,0.91}{0.83,1.0}", simple.toString());
	}
	
	DoubleImage<Two> img;
	String s = "{0.08,0.25,0.92,0.75}{0.17,0.33,0.83,0.67}{0.41,0.58,0.59,0.42}{0.5,0.67,0.5,0.33}{0.75,0.91,0.25,0.9}{0.83,1.0,0.17,0.0}{0.41,0.58,0.59,0.42}{0.5,0.67,0.5,0.33}{0.75,0.91,0.25,0.9}{0.83,1.0,0.17,0.0}";
	
	@Before
	public void setup() {		
		img = new DoubleImage<>(Two.class, s);		
	}

	@Test
	public void basicTest() {
		assertEquals(5, img.getWidth());
		assertEquals(4, img.getHeight());
		assertEquals(s, img.toString());
	}
	
	@Test
	public void identityKernel() {
		kernelTest(new Kernel(3, 0, 0, 0, 0, 1, 0, 0, 0, 0), s);
	}
	
	@Test
	public void simpleGradientKernel() {
		kernelTest(new Kernel(3, 0, 0, 0, -1, 0, 1, 0, 0, 0), "{0.41,0.58,0.59,0.42}{0.5,0.67,0.5,0.33}{0.67,0.66,-0.67,0.15000000000000002}{0.6599999999999999,0.6699999999999999,-0.6599999999999999,-0.67}{0.0,0.0,0.0,0.0}{0.0,0.0,0.0,0.0}{0.0,0.0,0.0,0.0}{0.0,0.0,0.0,0.0}{-0.41,-0.58,-0.59,-0.42}{-0.5,-0.67,-0.5,-0.33}");
	}
	
	public void kernelTest(Kernel k, String target) {
		basicTest();
		DoubleImage<Two> post = k.createFrom(Two.class, img, (w,h) -> new DoubleImage<>(Two.class, w, h));
		assertEquals(target, post.toString());
	}
	
	@Test
	public void extractKernel1() {
		Kernel k1 = new Kernel(3, img, Two.INTENSITY, 2, 2);
		assertEquals("{0.58,0.59,0.42}{0.91,0.25,0.9}{0.58,0.59,0.42}", k1.toString());
	}
	
	@Test
	public void extractKernel2() {
		Kernel k1 = new Kernel(3, img, Two.INTENSITY, 0, 0);
		assertEquals("{0.0,0.0,0.0}{0.0,0.08,0.25}{0.0,0.41,0.58}", k1.toString());
	}
	
	@Test
	public void extractKernel3() {
		Kernel k1 = new Kernel(3, img, Two.INTENSITY, 4, 3);
		assertEquals("{0.59,0.42,0.0}{0.25,0.9,0.0}{0.0,0.0,0.0}", k1.toString());
	}
}
