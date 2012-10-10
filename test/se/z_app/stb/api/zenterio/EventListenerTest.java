package se.z_app.stb.api.zenterio;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventListenerTest {

	
	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCurrentEvent() {
		EventListener e = new EventListener();
		assertTrue(e.getCurrentEvent().getClass() == String.class);
		assertTrue(e.getCurrentEvent() != null);
	}

	@Test
	public void testGetNextEvent() {
		fail("Not yet implemented");
	}

}
