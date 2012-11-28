package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;


/**
 * Interface for the functions that handles the events sent from the box.
 * @author Rasmus Holm, Linus Back
 *
 */
public interface EventListnerInterface {
	
	/**
	 * Initializes the event listener.
	 * @param stb
	 */
	public void init(STB stb);
	
	/**
	 * Gets the current event.
	 */
	public STBEvent getCurrentEvent();
	
	/**
	 * Gets the next event. If not initialized returns current event.
	 */
	public STBEvent getNextEvent();
	
	/**
	 * Stops the eventListener, needs to be initialized 
	 * before getNextEvent() can be called again.
	 */
	public void stop();
}
