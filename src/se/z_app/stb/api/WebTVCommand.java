package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.STB;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.zenterio.RCCommand;

public class WebTVCommand implements Observer{

	private static WebTVCommand instance; 
	private WebTVCommand(){
		STBContainer.instance().addObserver(this);
	}
	public static WebTVCommand instance(){
		if(instance == null)
			instance = new WebTVCommand();
		return instance;
	}
	
	private MonoDirectionalCmdInterface remoteImpl;
	@Override
	
	
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getSTB();
		switch(stb.getType()){
		case DEFAULT:
			remoteImpl = null;
			break;
		case ZENTERIO:
			remoteImpl = new RCCommand(stb.getIP());
			break;
		default:
			break;
		}
	}
	
	public WebTVService getSevice(){
		return null;
	}
	
	public WebTVItem search(){
		return null;
	}
	
	public void play(){
		
	}
	
	public void queue(){
		
	}
	
	public Bitmap getIcon(WebTVItem wti){
		return null;
		
	}
	
	public Bitmap getIcon(WebTVService wts){
		return null;
	}
}
