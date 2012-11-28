package se.z_app.zmote.webtv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.WebTVCommand;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Class that handles requests to perform tasks with WebTV related content
 * 
 * @author Sebastian Rauhala
 */
public class WebTVQuery {
	private String defaultDir;
	
	/**
	 * Creates a new WebTVQuery with an associated folder in the media storage on the phone 
	 */
	public WebTVQuery() {
		defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zmote";
		new File(defaultDir).mkdirs();
	}
	
	/**
	 * Fetches all WebTV services available
	 * @return An array with all available WebTV services
	 */
	public WebTVService[] getService() {
		return WebTVCommand.instance().getService();
	}
	
	/**
	 * Sets the correct icon for the WebTV service
	 * @param service - The service for which to set the icon
	 * @return The image set as icon for the service
	 */
	public Bitmap populateWithIcon(WebTVService service) {
		populateWithIconFromCache(service);
		
		if(service.getIcon() != null) {
			return service.getIcon();
		}
		
		Bitmap b = null;
		for(int i = 0; i < 5; i++) {
			b = WebTVCommand.instance().getIcon(service);
			if(b != null) break;
		}
		service.setIcon(b);
		saveIconsToCache(service);	
		return b;
	}
	
	/**
	 * Sets the correct icons for the WebTV services
	 * @param services[] - The services for which to set the icons
	 */
	public void populateWithIcon(WebTVService services[]){
		if(services == null) {
			return;
		}
		populateWithIconFromCache(services);
		for(WebTVService serv : services) {
			if (serv.getIcon() == null)
				for(int i = 0; i < 10; i++) {
					serv.setIcon(WebTVCommand.instance().getIcon(serv));
					if (serv.getIcon() != null) {
						break;
					}
				}
		}
		saveIconsToCache(services);
	}

	/**
	 * Sets the correct icon for the WebTV item
	 * @param item - The item for which to set the icon
	 * @return The image set as icon for the item
	 */
	public Bitmap populateWithIcon(WebTVItem item){
		Bitmap b = null;
		if(item.getIconURL().startsWith("http://")) {
			b = getImage(item.getIconURL());
		} else {
			b = WebTVCommand.instance().getIcon(item);
		}
		item.setIcon(b);
		return b;
	}
	
	/**
	 * Sets the correct icons for the WebTV items
	 * @param item[] - The items for which to set the icons
	 */
	public void populateWithIcon(WebTVItem item[]) {
		for(WebTVItem ite : item) {
			populateWithIcon(ite);
		}
	}
	
	/**
	 * Searches for WebTV content in a specified WebTV service
	 * @param q - A string with the search term
	 * @param s - The WebTV service to search in
	 * @return An array with all items matchiong the search string
	 */
	public WebTVItem[] search(String q, WebTVService s) {
		return WebTVCommand.instance().search(q, s);
	}

	/**
	 * Plays a specified WebTV item
	 * @param item - The item to be played
	 */
	public void play(WebTVItem item) {
		WebTVCommand.instance().play(item);
	}
	
	/**
	 * Puts a WebTV item into the play queue
	 * @param item - The item to be placed in the queue
	 */
	public void queue(WebTVItem item) {
		WebTVCommand.instance().queue(item);
	}
	
	/**
	 * Sets the cached icon for the specified service.
	 * @param services - The service to set the icon for
	 */
	private void populateWithIconFromCache(WebTVService services) {
		String iconPath = defaultDir+"/"+services.getID()+".png";
		File iconFile = new File(iconPath);
		if (iconFile.exists()) {
			services.setIcon(BitmapFactory.decodeFile(iconPath));
		}
	}
	
	/**
	 * Sets the cached icon for the specified services.
	 * @param services[] - An array with all services to set icons for.
	 */
	private void populateWithIconFromCache(WebTVService services[]) {
		if (services == null) {
			return;
		}
		for (WebTVService webTVService : services) {
			String iconPath = defaultDir+"/"+webTVService.getID()+".png";
			File iconFile = new File(iconPath);
			if (iconFile.exists()) {
				webTVService.setIcon(BitmapFactory.decodeFile(iconPath));
			}
		}
	}
	
	/**
	 * Saves the icon for the specified service to the cache
	 * @param services - The service to save the icon for
	 */
	private void saveIconsToCache(WebTVService services) {
		if (services.getIcon() != null) {
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
	
	/**
	 * Saves the icons for the specified services to the cache
	 * @param services[] - The services to save the icons for
	 */
	private void saveIconsToCache(WebTVService services[]) {
		for (WebTVService webTVService : services) {
			if(webTVService.getIcon() != null) {
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
	
	/**
	 * Fetches the image at the specified URL
	 * @param urlStr - The URL to the desired image
	 * @return The image as a bitmap
	 */
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return theImage;
	}
	
}
