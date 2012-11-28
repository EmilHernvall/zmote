package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.STB;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;

/**
 * Class that handles commands for the WebTV features
 * 
 * @author Sebastian Rauhala
 */
public class WebTVCommand implements Observer{
	private MonoDirectionalCmdInterface MonoCmd;
	private BiDirectionalCmdInterface BiCmd;
	/**
	 * Creates and holds the singleton instance of the WebTV command
	 * @author Sebastian Rauhala
	 */
	private static class SingletonHolder { 
        public static final WebTVCommand INSTANCE = new WebTVCommand();
	}
	
	/**
	 * Request the singleton instance of the WebTV command
	 * @return The instance of WebTV command
	 */
	public static WebTVCommand instance() {
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Private constructor for WebTV command that starts to listen to the STB container
	 */
	private WebTVCommand() {
		STBContainer.instance().addObserver(this);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getActiveSTB();
		AbstractAPIFactory factory = AbstractAPIFactory.getFactory(stb);
		MonoCmd = factory.getMonoDirectional();
		BiCmd = factory.getBiDirectional();
	}
	
	/**
	 * Fetches all WebTV services from the currently connected STB
	 * @return An array with all WebTV services, or null if no BiDirectionalCommandInterface is found.
	 */
	public WebTVService[] getService(){
		if(BiCmd != null) {
			return BiCmd.getWebTVServices();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Forwards a search for WebTV items to the STB and returns the answer
	 * @param query - The search string
	 * @param service - The service to be searched
	 * @return An array with the WebTV items that matched the search string
	 */
	public WebTVItem[] search(String query, WebTVService service) {
		if(BiCmd != null) {
			return BiCmd.searchWebTVService(query, service);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Forwards a WebTV item to the STB to be played
	 * @param item - The item to be played
	 */
	public void play(WebTVItem item) {
		if (MonoCmd != null) {
			MonoCmd.playWebTV(item);
		} 	
	}
	
	/**
	 * Forwards a WebTV item to the STB to be put in the play queue
	 * @param item The item to be queued
	 */
	public void queue(WebTVItem item) {
		if (MonoCmd != null) {
			MonoCmd.queueWebTV(item);
		} 	
	}
	
	/**
	 * Forwards a request for a WebTV item icon
	 * @param item - The item to fetch the icon for
	 * @return The icon for the specified item
	 */
	public Bitmap getIcon(WebTVItem item){
		if (BiCmd != null) {
			return BiCmd.getWebTVItemIcon(item);
		} 
		else {
			return null;
		}
	}
	
	/**
	 * Forwards a request for a WebTV service icon
	 * @param item - The service to fetch the icon for
	 * @return The icon for the specified service
	 */
	public Bitmap getIcon(WebTVService item){
		if (BiCmd != null) {
			return BiCmd.getWebTVServiceIcon(item);
		} 
		else {
			return null;
		}
	}
}
