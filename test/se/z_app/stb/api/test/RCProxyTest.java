package se.z_app.stb.api.test;

import org.junit.Test;

import junit.framework.TestCase;
import se.z_app.stb.api.*;


public class RCProxyTest extends TestCase {
	
	@Test
	public void testUp(){
		RCProxy tester = new RCProxy();
		assertTrue("pup".equals(tester.up()));	
	}
	
	
}
