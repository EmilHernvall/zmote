package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;

public class EPGData implements Observer{
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
		// TODO Auto-generated method stub
		
	}
	
	public EPG getEPG(){
		return null;
	}
	
	public Channel getCurrentChannel(){
		return null;
	}
	public Bitmap getChannelIcon(Channel channel){
		return null;
	}
	
	
	
}
