package se.z_app.stb;

import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

/**
 * Class that describes an EPG.
 * 
 * @author Rasmus Holm
 */
public class EPG implements Iterable<Channel> {
	private long dateOfCreation = System.currentTimeMillis();
	private STB stb;

	private ConcurrentSkipListMap channelsNr = new ConcurrentSkipListMap();
	private ConcurrentSkipListMap channelsName = new ConcurrentSkipListMap();
	
	
	/**
	 * Fetches an iterator with all channels in the EPG.
	 * @return An iterator with all channels in the EPG.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Channel> iterator() {
		return (Iterator<Channel>)channelsNr.values().iterator();
		//return channels.iterator();
	}
	
	/**
	 * Fetches an iterator with all channels in the EPG, sorted by channel name.
	 * @return An iterator with all channels in the EPG.
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Channel> iteratorByName() {
		return (Iterator<Channel>)channelsName.values().iterator();
	}
	
	/**
	 * Fetches an iterator with all channels in the EPG, sorted by channel number.
	 * @return An iterator with all channels in the EPG.
	 */
	public Iterator<Channel> iteratorByNr() {
		return (Iterator<Channel>)iterator();
	}
	
	/**
	 * Getter for date of creation
	 * @return The date on which the EPG was created
	 */
	public long getDateOfCreation() {
		return dateOfCreation;
	}
	
	/**
	 * Setter for date of creation
	 * @param dateOfCreation - The date on which the EPG was created
	 */
	public void setDateOfCreation(long dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	
	/**
	 * Getter for the STB to which this EPG is connected
	 * @return The STB for this EPG
	 */
	public STB getStb() {
		return stb;
	}
	
	/**
	 * Setter for the STB to which this EPG is connected
	 * @param stb - The STB for this EPG
	 */
	public void setStb(STB stb) {
		this.stb = stb;
	}
	
	/**
	 * Adds a channel to this EPG
	 * @param channel - The channel to add
	 */
	public void addChannel(Channel channel){
		channelsNr.put(channel.getNr(), channel);
		channelsName.put(channel.getName(), channel);
	}
	
	/**
	 * Get the channel with the specified channel number
	 * @param nr - The number of the desired channel
	 * @return The channel with the specified number
	 */
	public Channel getChannel(int nr){
		return (Channel)channelsNr.get(nr);
	}
	
	/**
	 * Get the channel with the specified channel name
	 * @param name - The name of the desired channel
	 * @return The channel with the specified name
	 */
	public Channel getChannel(String name){
		return (Channel)channelsName.get(name);
	}
	


}

