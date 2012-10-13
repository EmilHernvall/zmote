package se.z_app.stb;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

public class EPG implements Iterable<Channel> {
	private long dateOfCreation = System.currentTimeMillis();
	private STB stb;
	private ConcurrentSkipListMap<Integer, Channel> channelsNr = new ConcurrentSkipListMap<Integer, Channel>();
	private ConcurrentSkipListMap<String, Channel> channelsName = new ConcurrentSkipListMap<String, Channel>();
	
	
	@Override
	public Iterator<Channel> iterator() {
		return channelsNr.values().iterator();
	}
	public Iterator<Channel> iteratorByName() {
		return channelsName.values().iterator();
	}
	public Iterator<Channel> iteratorByNr() {
		return iterator();
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
		channelsNr.putIfAbsent(channel.getNr(), channel);
		channelsName.putIfAbsent(channel.getName(), channel);
	}
	public Channel getChannel(int nr){
		return channelsNr.get(nr);
	}
	public Channel getChannel(String name){
		return channelsName.get(name);
	}
	


}

