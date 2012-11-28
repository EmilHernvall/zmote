package se.z_app.zmote.gui;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import se.z_app.stb.api.EPGData;
import se.z_app.stb.api.RCProxy;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.WebTVCommand;
import se.z_app.zmote.epg.EPGContentHandler;
import se.z_app.zmote.webtv.MediaStreamer;

/**
 * Instantiates necessary components and saves data like the phone ip address
 * 
 * @author Rasmus Holm
 */
public class Bootstrap {
	private static String ip;
	
	/**
	 * Getter for the smartphone device's local ip address
	 * @return the ip address
	 */
	public static String getLocalIP(){
		return ip;
	}
	
	/**
	 * Constructor for Bootstrap
	 * @param context the application context
	 * @param wifiManager a WifiManager for the application
	 */
	public Bootstrap(Context context, WifiManager wifiManager){
		
		EPGContentHandler.setContext(context);
        EPGContentHandler.instance();
        RemoteControl.instance();
        EPGData.instance();
        STBContainer.instance();
        RCProxy.instance();
        WebTVCommand.instance();
        
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	int ipAddress = wifiInfo.getIpAddress();    	
    	@SuppressWarnings("deprecation")
		String ip = android.text.format.Formatter.formatIpAddress(ipAddress);
        
    	MediaStreamer.instance().setLocalIP(ip);
    	Bootstrap.ip = ip;
	}
}
