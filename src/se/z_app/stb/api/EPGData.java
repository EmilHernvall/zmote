package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;
import android.util.Log;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;
import se.z_app.stb.STB.STBEnum;
import se.z_app.stb.api.zenterio.StandardCommand;

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
		switch(stb.getType()){
			case ZENTERIO:
				com = new StandardCommand(stb.getIP());
				break;
			case DEFAULT:
				com = new StandardCommand(stb.getIP());
				break;
			default:
		}
	}
	
	public EPG getEPG(){
		EPG epg = com.getEPG();
		epg.setStb(stb);
		
		return epg; 
	}
	
	public Channel getCurrentChannel(){
		Log.i("EPGData", com.getClass().toString());
		return com.getCurrentChannel();
	}
	public Bitmap getChannelIcon(Channel channel){
		return com.getChannelIcon(channel);
	}
	
	
	
}
