package se.z_app.stb;

/**
 * Class that describes an Event from the STB.
 * Depending on the type of event the STB state 
 * containts different data.
 * NodeLaunch has a label and an url.
 * Volum has a value ranging from 0 to 100 inclusive.
 * and mute have a boolean, true if muted false if not.
 * A better idea might be to have an abstract class and then 
 * depending on the type of event have different classes. 
 * @author Linus
 *
 */
public class STBEvent {
	private String type;
	private String label;
	private String url;
	private int value;
	private boolean state;
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	
}
