package se.z_app.stb.api;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;

/**
 * Class that handles communication with an STB and its EPG
 * @author Markus Widegren, Rasmus Holm
 */
public class EPGData implements Observer{
	private STB stb;
	private BiDirectionalCmdInterface com;
	
	/**
	 * Creates and holds the singleton instance of the EPG data class
	 * @author Markus Widegren, Rasmus Holm
	 */
	private static class SingletonHolder { 
        public static final EPGData INSTANCE = new EPGData();
	}
	
	/**
	 * Get the instance of this class
	 * @return the instance
	 */
	public static EPGData instance() {
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Private constructor, since it's a singleton
	 */
	private EPGData() {
		STBContainer.instance().addObserver(this);
	}
	
	/**
	 * Update the EPGData with a (possibly) new STB and command interface
	 */
	@Override
	public void update(Observable observable, Object data) {
		stb = STBContainer.instance().getActiveSTB();
		com = AbstractAPIFactory.getFactory(stb).getBiDirectional();
	}
	
	/**
	 * Get the current EPG
	 * @return An EPG object 
	 */
	public EPG getEPG() {
		if(com == null) {
			return null;
		}
		EPG epg = com.getEPG();
		if(epg != null) {
			epg.setStb(stb);
		}
		return epg; 
	}
	
	/**
	 * Get the current channel
	 * @return the current channel
	 */
	public Channel getCurrentChannel() {
		if(com == null) {
			return null;
		}
		return com.getCurrentChannel();
	}
	
	/**
	 * Get the icon for a channel
	 * @param channel
	 * @return the icon as a bitmap
	 */
	public Bitmap getChannelIcon(Channel channel) {
		if(com == null || channel == null) {
			return null;
		}
		Bitmap icon = null;
		for(int i = 0; i<6 && icon == null; i++) {
			icon = com.getChannelIcon(channel);
		}
		return icon;
	}
	
	/**
	 * Get the icon for a channel, and set that channels icon to it
	 * @param channel
	 */
	public void populateWithChannelIcon(Channel channel) {
		if(com == null || channel == null) {
			return;
		}
		channel.setIcon(getChannelIcon(channel));
	}
	
	/**
	 * Populate all channels in an EPG with icons
	 * @param epg
	 */
	public void populateWithChannelIcon(EPG epg) {
		if(com == null || epg == null) {
			return;
		}
		Iterator<Channel> channels = epg.iterator();
		while(channels.hasNext()) {
			populateWithChannelIcon(channels.next());
		}
	}
	
	/**
	 * Populate channels without icons with the appropriate icons
	 * @param epg
	 */
	public void populateAbsentChannelIcon(EPG epg){
		if(com == null || epg == null) {
			return;
		}
		for (Channel channel : epg) {
			if(channel.getIcon() == null) {
				populateWithChannelIcon(channel);
			}
		}
	}
	
	/**
	 * Returns the current volume setting
	 * @return An integer with the current volume
	 */
	public int getVolume() {
		if(com != null) {
			return com.getVolume();
		}
		return 0;
	}
	
	/**
	 * Return whether it's currently muted or not.
	 * @return boolean
	 */
	public boolean isMute() {
		return com.isMute();
	}
}
