package se.z_app.stb;

/**
 * Class that describes an Event from the STB.
 * Depending on the type of event the STB state 
 * Contains different data.
 * NodeLaunch has a label and an url.
 * Volum has a value ranging from 0 to 100 inclusive.
 * and mute have a boolean, true if muted false if not.
 * A better idea might be to have an abstract class and then 
 * depending on the type of event have different classes. 
 * @author Linus Back
 *
 */
public class STBEvent {
	private String type;
	private String label;
	private String url;
	private int value;
	private boolean state;
	
	/**
	 * Getter for the type
	 * only exist in an event of type nodelaunch
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 * Setter for type
	 * only exist in an event of type nodelaunch
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Getter for label
	 * only exist in an event of type nodelaunch
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * Setter for label
	 * only exist in an event of type nodelaunch
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * getter for URL
	 * only exist in an event of type nodelaunch
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Setter for URL
	 * only exist in an event of type nodelaunch
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Getter for value
	 * only exist in an event of type volum
	 * @return
	 */
	public int getValue() {
		return value;
	}
	/**
	 * Setter for value
	 * only exist in an event of type volume
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}
	/**
	 * Getter for state
	 * only exist in an event of type mute
	 * @return
	 */
	public boolean getState() {
		return state;
	}
	/**
	 * Setter for state
	 * only exist in an event of type mute
	 * @param state
	 */
	public void setState(boolean state) {
		this.state = state;
	}
	
	
}
