package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.STB;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.zenterio.RCCommand;
import se.z_app.stb.api.zenterio.StandardCommand;

/**
 * 
 * @author Sebastian
 * 
 *
 */

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
	
	private MonoDirectionalCmdInterface MonoCmd;
	private BiDirectionalCmdInterface BiCmd;
	@Override
	
	
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getSTB();
		switch(stb.getType()){
		case DEFAULT:
			MonoCmd = null;
			BiCmd = null;
			break;
		case ZENTERIO:
			MonoCmd = new RCCommand(stb.getIP());
			BiCmd = new StandardCommand(stb.getIP());
			break;
		default:
			break;
		}
	}
	
	
	public WebTVService[] getSevice(){
		if(BiCmd != null){
			return BiCmd.getWebTVServices();
		}
		else
			return null;		
	}
	
	public WebTVItem[] search(String query, WebTVService service){
		if(BiCmd != null){
			return BiCmd.searchWebTVService(query, service);
		}
		else
			return null;
	}
	
	public void play(WebTVItem item){
		if (MonoCmd != null){
			MonoCmd.playWebTV(item);
		} 	
	}
	
	public void queue(WebTVItem item){
		if (MonoCmd != null){
			MonoCmd.queueWebTV(item);
		} 	
	}
	
	public Bitmap getIcon(WebTVItem item){
		if (BiCmd != null){
			return BiCmd.getWebTVItemIcon(item);
		} 
		else
			return null;
	}
	
	public Bitmap getIcon(WebTVService item){
		if (BiCmd != null){
			return BiCmd.getWebTVServiceIcon(item);
		} 
		else
			return null;
	}
}
