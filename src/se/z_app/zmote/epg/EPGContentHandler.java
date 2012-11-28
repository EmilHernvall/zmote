package se.z_app.zmote.epg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STBEvent;
import se.z_app.stb.api.EPGData;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.STBListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Class that contains and controls the current EPG and the current channel
 * @author Markus Widegren
 *
 */
public class EPGContentHandler extends Observable implements Runnable, Observer{
	private Thread thread;
	private boolean isRunning;
	private EPG currentEPG;
	private Channel currentChannel;
	private static Context theContext;
	private long updateIntervalMillis = 3600 * 1000;
	private long lastCachingOfIcons = 0;
	private String defaultDir;

	/* Singleton */
	private static class SingletonHolder { 
		public static final EPGContentHandler INSTANCE = new EPGContentHandler();
	}	

	/**
	 * Get the instance of EPGContentHandler
	 * @return the instance
	 */
	public static EPGContentHandler instance(){
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Set the context for the database
	 */
	public static void setContext(Context theContextIn) {
		theContext = theContextIn;
	}

	/**
	 * The private constructor
	 */
	private EPGContentHandler(){	
		thread = new Thread(this);
		isRunning = true;
		defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zmote";
		new File(defaultDir).mkdirs();
		STBContainer.instance().addObserver(this);
		STBListener.instance().addObserver(this);
		currentEPG = new EPG();
		currentChannel = new Channel();
		thread.start();
	}

	/**
	 * Get the current EPG
	 * @return the EPG
	 */
	public EPG getEPG(){	
		synchronized (currentEPG) {
			if(currentEPG == null){
				buildEPG();
			}
			return currentEPG;
		}
	}

	/**
	 * Get the current channel
	 * @return the channel
	 */
	public Channel getCurrentChannel(){	
		synchronized (currentChannel) {
			if(currentChannel == null) {
				findCurrentChannel();
			}
			return currentChannel;
		}
	}

	/**
	 * Finds the current channel and it's url 
	 */
	private void findCurrentChannel(){
		currentChannel = EPGData.instance().getCurrentChannel();
		if(currentChannel.getUrl() != null){
			for (Channel channel : currentEPG) {
				if(channel.getUrl().toLowerCase().contains(currentChannel.getUrl().toLowerCase())){
					currentChannel = channel;
					break;
				}
			}
		}
	}

	/**
	 * Build the EPG by getting the icons from the box
	 */	
	private void buildEPG() {
		if(theContext != null) {
			EPGdbHandler theHandler = new EPGdbHandler(theContext);
			EPG cachedEPG = theHandler.selectEPG(STBContainer.instance().getActiveSTB());
			if(cachedEPG == null || cachedEPG.getDateOfCreation() < System.currentTimeMillis() + updateIntervalMillis) {
				currentEPG = EPGData.instance().getEPG();
				if(currentEPG != null) {
					populateChannelIconFromCache(currentEPG);
					EPGData.instance().populateAbsentChannelIcon(currentEPG);
				}
				else{
					currentEPG = new EPG();
				}
				theHandler.updateEPG(STBContainer.instance().getActiveSTB(), currentEPG);
			}
			else {
				currentEPG = cachedEPG;
			}
		}
		else {
			currentEPG = EPGData.instance().getEPG();
			if(currentEPG != null){
				populateChannelIconFromCache(currentEPG);
				EPGData.instance().populateAbsentChannelIcon(currentEPG);

			}else{
				currentEPG = new EPG();
			}
		}
	}

	/**
	 * Sets the icon path and converts it to a bitmap
	 * @param epg
	 */
	private void populateChannelIconFromCache(EPG epg){

		for (Channel channel : epg) {
			String iconPath = defaultDir+"/"+getChannelHash(channel)+".png";
			File iconFile = new File(iconPath);
			if(iconFile.exists()){
				channel.setIcon(BitmapFactory.decodeFile(iconPath));
			}
		}
	}

	/**
	 * Creates a thread and gets the icons 
	 */
	private void cacheChannelIcons(){
		new Thread(new Runnable() {
			@Override
			public void run() {

				for (Channel channel : currentEPG) {
					Bitmap icon = null;
					while(icon == null) {
						icon = EPGData.instance().getChannelIcon(channel);
					}
					
					channel.setIcon(icon);
					String iconPath = defaultDir+"/"+getChannelHash(channel)+".png";
					try {
						FileOutputStream out = new FileOutputStream(iconPath);
						channel.getIcon().compress(Bitmap.CompressFormat.PNG, 100, out);
						out.flush();
						out.close();
					}
					
					catch (FileNotFoundException e) { } 
					catch (IOException e) {	}
				}				
			}
		}).start();
	}

	/**
	 * Get the channel information and returns a string where the Zenterio
	 * boxes string attributes are added    
	 * @param channel
	 * @return string with channel information  
	 */
	private String getChannelHash(Channel channel){
		return "tsid"+channel.getTsid()+"sid"+channel.getSid()+"onid"+channel.getOnid();
	}

	/**
	 * The main function of the class, a loop that gets
	 *  the current channel and builds the EPG whenever it changes
	 */
	@Override
	public void run() {
		while(isRunning){
			if(STBContainer.instance().getActiveSTB() != null){
				synchronized (currentEPG) {
					synchronized (currentChannel) {
						buildEPG();
						findCurrentChannel();
					}
				}

				if(lastCachingOfIcons + updateIntervalMillis < System.currentTimeMillis()){
					lastCachingOfIcons = System.currentTimeMillis();
					cacheChannelIcons();
				}
			}

			try {
				synchronized (thread) {
					thread.wait();
				}
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * When something changes, update the EPG and current channel if necessary
	 */
	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof STBContainer){
			synchronized (thread) {
				thread.notifyAll();
			}
		}else if(observable instanceof STBListener){
			STBEvent event = STBListener.instance().getCurrentEvent();
			if(event.getUrl() != null){
				for (Channel channel : currentEPG) {
					if(event.getUrl().toLowerCase().contains(channel.getUrl().toLowerCase())){
						System.out.println("Found Channel: " + channel.getName());
						currentChannel = channel;
						super.setChanged();
						super.notifyObservers();
						break;
					}
				}
			}
		}

	}

	/**
	 * Get the current time limit for how long the EPG is valid
	 * @return milliseconds
	 */
	public long getUpdateInterval() {
		return updateIntervalMillis;
	}

	/**
	 * Set the current time limit for how long the EPG is valid
	 * @param interval in milliseconds
	 */
	public void setUpdateInterval(long intervalIn) {
		updateIntervalMillis = intervalIn;
	}
}
