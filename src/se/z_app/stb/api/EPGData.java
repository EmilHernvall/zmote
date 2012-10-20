package se.z_app.stb.api;

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
	private static EPGData instance; 
	private EPGData(){
		STBContainer.instance().addObserver(this);
	}
	public static EPGData instance(){
		if(instance == null)
			instance = new EPGData();
		return instance;
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
	
	
	
}
