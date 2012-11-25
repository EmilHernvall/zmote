package se.z_app.stb;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents a program
 * 
 * @author Rasmus Holm
 */
public class Program implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2985783355705993721L;
	private String name;
	private int eventID = -1;
	private Date start;
	private int duration = -1;
	private String shortText;
	private String longText;
	private Channel channel;
	
	/**
	 * Creates a program belonging to the specified channel
	 * @param parentChannel - The channel which the program belongs to
	 */
	public Program(Channel parentChannel){
		 /* @Leonard: Changed the function this.channel = channel; it didn't do anything */
		this.channel = parentChannel;
	}
	
	/**
	 * Getter for parent channel
	 * @return The channel which the program belongs to
	 */
	public Channel getParentChannel(){
		return channel;
	}
	
	/**
	 * Getter for program name
	 * @return The name of the program
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for program name
	 * @param name - The name of the program
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for program event ID
	 * @return The event ID of the program
	 */
	public int getEventID() {
		return eventID;
	}
	
	/**
	 * Setter for program event ID
	 * @param eventID - The eventID of the program
	 */
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	/**
	 * Getter for program start
	 * @return When the program starts
	 */
	public Date getStart() {
		return start;
	}
	
	/**
	 * Setter for program start
	 * @param start - The program start date
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	
	/**
	 * Getter for program duration
	 * @return Duration of the program in seconds
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Setter for program duration
	 * @param duration - Duration of the program in seconds
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Getter for the short program description
	 * @return A short description of the program
	 */
	public String getShortText() {
		return shortText;
	}
	
	/**
	 * Setter for short program description
	 * @param shortText - A short description of the program
	 */
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}
	
	/**
	 * Getter for the long program description
	 * @return A long description of the program
	 */
	public String getLongText() {
		return longText;
	}
	
	/**
	 * Setter for the long program description
	 * @param longText - A long description of the program
	 */
	public void setLongText(String longText) {
		this.longText = longText;
	}
	
	/**
	 * Getter for the channel which this program belongs to
	 * @return The channel
	 */
	public Channel getChannel() {
		return channel;
	}
	
	/**
	 * Setter for the channel which this program belongs to
	 * @param channel - The channel
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	
}
