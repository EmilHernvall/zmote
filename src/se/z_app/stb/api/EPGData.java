package se.z_app.stb.api;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;


public class EPGData implements Observer{
	private STB stb;
	private BiDirectionalCmdInterface com;
	
	//Singleton and adding itself as an observer
	private static class SingletonHolder { 
        public static final EPGData INSTANCE = new EPGData();
	}
		
	public static EPGData instance(){
		return SingletonHolder.INSTANCE;
	}
	
	private EPGData(){
		STBContainer.instance().addObserver(this);
	}
	
	
	public void update(Observable observable, Object data) {
		stb = STBContainer.instance().getSTB();
		com = AbstractAPIFactory.getFactory(stb).getBiDirectional();
	}
	
	public EPG getEPG(){
		EPG epg = com.getEPG();
		epg.setStb(stb);
		
		return epg; 
	}
	
	public Channel getCurrentChannel(){
		return com.getCurrentChannel();
	}
	public Bitmap getChannelIcon(Channel channel){
		return com.getChannelIcon(channel);
	}
	
	public void populateWithChannelIcon(Channel channel){
		channel.setIcon(getChannelIcon(channel));
	}
	
	public void populateWithChannelIcon(EPG epg){
		Iterator<Channel> channels = epg.iterator();
		while(channels.hasNext()){
			populateWithChannelIcon(channels.next());
		}
	}
	
	
}
