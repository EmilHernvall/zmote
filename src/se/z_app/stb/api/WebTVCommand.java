package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.STB;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;


/**
 * 
 * @author Sebastian
 * 
 *
 */

public class WebTVCommand implements Observer{

	
	private static class SingletonHolder { 
        public static final WebTVCommand INSTANCE = new WebTVCommand();
}
	public static WebTVCommand instance(){
		return SingletonHolder.INSTANCE;
	}
	
	private WebTVCommand(){
		STBContainer.instance().addObserver(this);
	}
	
	private MonoDirectionalCmdInterface MonoCmd;
	private BiDirectionalCmdInterface BiCmd;
	
	@Override
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getActiveSTB();
		AbstractAPIFactory factory = AbstractAPIFactory.getFactory(stb);
		MonoCmd = factory.getMonoDirectional();
		BiCmd = factory.getBiDirectional();
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
