package se.z_app.stb;

import java.util.Iterator;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;



import android.graphics.Bitmap;

/**
 * Class that represents a TV channel
 * 
 * @author Rasmus Holm
 */
public class Channel implements Iterable<Program>{
	private String name;
	private Bitmap icon;
	private String iconUrl;
	private String url;
	private int nr = -1; 
	private int onid = -1;
	private int tsid = -1;
	private int sid = -1;
	private ConcurrentSkipListMap programsByDate = new ConcurrentSkipListMap();
	
	/**
	 * Returns the name of the channel
	 * @return The name of the channel
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the channel
	 * @param name - The name to set 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the icon of the channel
	 * @return The icon of the channel
	 */
	public Bitmap getIcon() {
		return icon;
	}
	
	/**
	 * Set the icon of the channel
	 * @param icon - The bitmap image to set as icon
	 */
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	
	/**
	 * Returns the URL of the icon of the channel
	 * @return The URL of the icon of the channel
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	
	/**
	 * Set the URL to the the channel's icon
	 * @param iconUrl - The URL to the icon to be set 
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	/**
	 * Returns the URL of the channel
	 * @return The URL of the channel
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set the URL to the channel
	 * @param url - The URL to be set 
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Returns the number of the channel
	 * @return The number of the channel
	 */
	public int getNr() {
		return nr;
	}
	
	/**
	 * Set the number of the channel
	 * @param nr - The number to set for the channel
	 */
	public void setNr(int nr) {
		this.nr = nr;
	}
	
	/**
	 * Returns the on id of the channel
	 * @return The on id of the channel
	 */
	public int getOnid() {
		return onid;
	}
	
	/**
	 * Set the on id of the channel
	 * @param onid - The on id to set 
	 */
	public void setOnid(int onid) {
		this.onid = onid;
	}
	
	/**
	 * Returns the ts id of the channel
	 * @return The ts id of the channel
	 */
	public int getTsid() {
		return tsid;
	}
	
	/**
	 * Set the ts id of the channel
	 * @param tsid - The ts id to set 
	 */
	public void setTsid(int tsid) {
		this.tsid = tsid;
	}
	
	/**
	 * Returns the s id of the channel
	 * @return The s id of the channel
	 */
	public int getSid() {
		return sid;
	}
	
	/**
	 * Set the s id of the channel
	 * @param sid - The s id to set 
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	/**
	 * Adds a program to the channel
	 * @param program - The program to be added
	 */
	public void addProgram(Program program){
//		programs.add(program);
		programsByDate.put(program.getStart(), program);
	}

	/**
	 * Fetches an iterator with all programs for the channel.
	 * @return An iterator with all programs for the channel.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Program> iterator() {
		return programsByDate.values().iterator();
		//return programs.iterator();
	}

	

	
}

