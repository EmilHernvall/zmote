package se.z_app.stb.api.test;

import junit.framework.TestCase;
import se.z_app.stb.api.*;


public class RCProxyTest extends TestCase {
	
	public void up(){
		RCProxy tester = new RCProxy();
		assertEquals("Result", "pup", tester.up());	
	}
}
