package se.z_app.zmote.webtv;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.WebTVCommand;


/**
 * 
 * @author Sebastian Rauhala
 * 
 *
 */

public class WebTVQuery extends Observable implements Observer, Runnable {
	private String defaultDir;
	private WebTVdbHandler db;
	private static Context theContext;
	private long updateIntervalMillis = 3600 * 1000;
	private WebTVService[] WebTvServices;	
	private WebTVService webtvservice;
	private boolean isRunning;
	private Thread thread, thread2;
	
	private static class SingletonHolder { 
        public static final WebTVQuery INSTANCE = new WebTVQuery();
        
	}	
	
	public static WebTVQuery instance(){
		Log.i("WebTVQuery test", "WebTVQuary test WebTVQuery Instance()");
		return SingletonHolder.INSTANCE;
		
	}

	private WebTVQuery(){
		defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zmote";
		new File(defaultDir).mkdirs();
		Log.i("WebTVQuery test", "WebTVQuary test WebTVQuery got initialized");
		
		thread = new Thread(this);
//		thread2 = new Thread(this);
		isRunning = true;
		webtvservice = new WebTVService();
//		db.updateServices(STBContainer.instance().getActiveSTB(), WebTvServices);
		STBContainer.instance().addObserver(this);
//		STBListener.instance().addObserver(this);
		thread.start();
//		thread2.start();
	}
	
	public void buildWebTVdb(){
		if (theContext != null){
			db = new WebTVdbHandler(theContext);
			WebTVService servicedatecrated = db.selectTimeCreated(STBContainer.instance().getActiveSTB());
			WebTVService[] services = db.selectServices(STBContainer.instance().getActiveSTB());
			if (services == null || servicedatecrated.getDateOfCreation() < System.currentTimeMillis() + updateIntervalMillis){
				Log.i("WebTVQuery test", "WebTVQuary test WebTV service == Null");
				WebTvServices = WebTVCommand.instance().getService();
				db.updateServices(STBContainer.instance().getActiveSTB(), WebTvServices);
				db.updateDateOfCreation(STBContainer.instance().getActiveSTB());
			}
			else{
				Log.i("WebTVQuery test", "WebTVQuary test WebTV service != Null ");
				WebTvServices = services;
			}	
		}
		else {
			WebTvServices = db.selectServices(STBContainer.instance().getActiveSTB());
		}
	}
	
	
	 
	public static void setContext(Context theContextIn) {
		theContext = theContextIn;
		Log.i("WebTVQuery test", "WebTVQuary test WebTV Context is set: " + theContext);
	}
	
	
	public WebTVService[] getService(){
		synchronized (this) {
			if(WebTvServices == null){
//				Log.i("WebTVQuery test", "WebTVQuary test WebTV WebTVServices == null");
				buildWebTVdb();
			}
		}
//		Log.i("WebTVQuery test", "WebTVQuary test WebTV WebTVServices != null " + WebTvServices);
		return WebTvServices;
	}
	
	
	public Bitmap populateWithIcon(WebTVService service){
		populateWithIconFromCache(service);
		
		if(service.getIcon() != null)
			return service.getIcon();
		
		Bitmap b = null;
		for(int i = 0; i < 5; i++){
			b = WebTVCommand.instance().getIcon(service);
			if(b != null) break;
		}
		service.setIcon(b);
		
		saveIconsToCache(service);	
		
		return b;
	}
	
	
	
	public void populateWithIcon(WebTVService services[]){
		populateWithIconFromCache(services);
		
		for(WebTVService serv : services){
			if(serv.getIcon() == null)
				for(int i = 0; i<10; i++){
					serv.setIcon(WebTVCommand.instance().getIcon(serv));
					if(serv.getIcon() != null) break;
				}
		}
		
		saveIconsToCache(services);
	}

	public Bitmap populateWithIcon(WebTVItem item){
		Bitmap b = null;
		if(item.getIconURL().startsWith("http://")){
			b = getImage(item.getIconURL());
		}else{
			b = WebTVCommand.instance().getIcon(item);
		}
		item.setIcon(b);
		return b;
	}
	
	public void populateWithIcon(WebTVItem item[]){
		for(WebTVItem ite : item){
			populateWithIcon(ite);
		}
	}
	
	public WebTVItem[] search(String q, WebTVService s){
		return WebTVCommand.instance().search(q, s);
	}

	public void play(WebTVItem item){
		WebTVCommand.instance().play(item);
	}
	
	public void queue(WebTVItem item){
		WebTVCommand.instance().queue(item);
	}
	
	
	
	
	//Cacheing stuff
	
	private void populateWithIconFromCache(WebTVService services){
		
		//System.out.println("Populating here 1");
		String iconPath = defaultDir+"/"+services.getID()+".png";
		File iconFile = new File(iconPath);
		if(iconFile.exists()){
			services.setIcon(BitmapFactory.decodeFile(iconPath));
		}
	
	}
	
	private void populateWithIconFromCache(WebTVService services[]){
		//System.out.println("Populating here 2");
		
		for (WebTVService webTVService : services) {
			String iconPath = defaultDir+"/"+webTVService.getID()+".png";
			File iconFile = new File(iconPath);
			if(iconFile.exists()){
				webTVService.setIcon(BitmapFactory.decodeFile(iconPath));
			}
		}
	}
	
	
	private void saveIconsToCache(WebTVService services){
		//System.out.println("Saving here 1");
		if(services.getIcon() != null){
			String iconPath = defaultDir+"/"+services.getID()+".png";	
			FileOutputStream out;
			try {
				out = new FileOutputStream(iconPath);
				services.getIcon().compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void saveIconsToCache(WebTVService services[]){
		//System.out.println("Saving here 2");
		for (WebTVService webTVService : services) {
			if(webTVService.getIcon() != null){
				String iconPath = defaultDir+"/"+webTVService.getID()+".png";
				
				FileOutputStream out;
				try {
					out = new FileOutputStream(iconPath);
					webTVService.getIcon().compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	public Bitmap getImage(String urlStr){
		URL url;
		Bitmap theImage = null;
		try {
			//TODO: Problem with loading images comes from creating rapid connections, keepAlive should be disable
			url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
			InputStream in = connection.getInputStream();
			theImage = BitmapFactory.decodeStream(in);
			in.close();
			//connection.getOutputStream().close();
			connection.disconnect();
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return theImage;
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		if(observable instanceof STBContainer){
			synchronized (thread) {
				thread.notifyAll();
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			if(STBContainer.instance().getActiveSTB() != null){
				
				synchronized(this){
					buildWebTVdb();
				}
					Log.i("WebTVQuery test", "WebTVQuary test Run()");
			}
			try {
				synchronized (thread) {
					thread.wait();
				}
			} catch (InterruptedException e) {
				
			}
		}
		
		
	}
	
}
