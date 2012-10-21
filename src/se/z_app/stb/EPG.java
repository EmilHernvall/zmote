package se.z_app.stb;

import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;


public class EPG implements Iterable<Channel> {
	private long dateOfCreation = System.currentTimeMillis();
	private STB stb;

	private ConcurrentSkipListMap channelsNr = new ConcurrentSkipListMap();
	private ConcurrentSkipListMap channelsName = new ConcurrentSkipListMap();
	
	

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Channel> iterator() {
		return (Iterator<Channel>)channelsNr.values().iterator();
		//return channels.iterator();
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<Channel> iteratorByName() {
		return (Iterator<Channel>)channelsName.values().iterator();
	}
	public Iterator<Channel> iteratorByNr() {
		return (Iterator<Channel>)iterator();
	}
	
	public long getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(long dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public STB getStb() {
		return stb;
	}
	public void setStb(STB stb) {
		this.stb = stb;
	}
	public void addChannel(Channel channel){
		channelsNr.put(channel.getNr(), channel);
		channelsName.put(channel.getName(), channel);
	}
	public Channel getChannel(int nr){
		return (Channel)channelsNr.get(nr);
	}
	public Channel getChannel(String name){
		return (Channel)channelsName.get(name);
	}
	


}

