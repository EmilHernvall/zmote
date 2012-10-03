package se.z_app.stb.api;


import android.graphics.Bitmap;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;

public interface BiDirectionalCmdInterface {
	public EPG getEPG();
	public Channel getChannel();
	public Bitmap getChannelIcon(Channel channel);
	
	public WebTVService[] getWebTVServices();
	public Bitmap getWebTVServiceIcon(WebTVService serivce);
	public Bitmap getWebTVItemIcon(WebTVItem item);
	public WebTVItem[] searchWebTVService(String query, WebTVService service);
	
	
}
