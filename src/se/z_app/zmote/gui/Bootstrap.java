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

public class Bootstrap {
	private static String ip;
	
	public static String getLocalIP(){
		return ip;
	}
	
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
