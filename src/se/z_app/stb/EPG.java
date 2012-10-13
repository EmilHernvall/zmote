package se.z_app.stb;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

public class EPG implements Iterable<Channel> {
	private long dateOfCreation;
	private STB stb;
	private ConcurrentSkipListSet<Channel> channels;
	@Override
	public Iterator<Channel> iterator() {
		return channels.iterator();
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
		channels.add(channel);
		
	}
	


}
