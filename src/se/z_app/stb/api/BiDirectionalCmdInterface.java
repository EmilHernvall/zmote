package se.z_app.stb.api;

import android.graphics.Bitmap;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;

/**
 * The interface for Bidirectional commands.
 * @author Rasmus Holm
 *
 */
public interface BiDirectionalCmdInterface {
	
	/**
	 * Returns an EPG Object from the box.
	 * @return EPG
	 */
	public EPG getEPG();
	
	/**
	 * Gets the current channel from the STB.
	 * @return A channel
	 */
	public Channel getCurrentChannel();
	
	/**
	 * Gets the channel icon from the STB.
	 * @param channel
	 * @return The icon of the channel.
	 */
	public Bitmap getChannelIcon(Channel channel);
	
	/**
	 * Returns an array of the web TV services available.
	 */
	public WebTVService[] getWebTVServices();
	
	/**
	 * Returns the icon of a web service (Spotify, youtube f.e.)
	 * @param service
	 * @return Bitmap of a Web TV Service
	 */
	public Bitmap getWebTVServiceIcon(WebTVService service);
	
	/**
	 * Returns an icon from a Web TV item
	 * @param item
	 * @return Bitmap of a Web TV Icon
	 */
	public Bitmap getWebTVItemIcon(WebTVItem item);
	
	/**
	 * Makes a query in a web service.
	 * @param query What to search for.
	 * @param service Which service to use.
	 * @return An array with the search results.
	 */
	public WebTVItem[] searchWebTVService(String query, WebTVService service);
	
	/**
	 * Gets the current volume from the box.
	 * @return Integer with the current value.
	 */
	public int getVolume();
	
	/**
	 * Checks whether the box is currently set to mute or not.
	 * @return True/False
	 */
	public boolean isMute();
	
}
