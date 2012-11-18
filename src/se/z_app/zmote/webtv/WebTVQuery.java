package se.z_app.zmote.webtv;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import se.z_app.stb.Channel;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.WebTVCommand;

/**
 * 
 * @author Sebastian
 * 
 *
 */

public class WebTVQuery {
	private String defaultDir;
	
	
	
	public WebTVQuery(){
		defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zmote";
		new File(defaultDir).mkdirs();
	}
	
	public WebTVService[] getService(){
		return WebTVCommand.instance().getService();
	}
	
	public Bitmap populateWithIcon(WebTVService service){
	
		populateWithIconFromCache(service);
		
		if(service.getIcon() != null)
			return service.getIcon();
		
		Bitmap b = null;
		for(int i = 0; i < 10; i++){
			b = WebTVCommand.instance().getIcon(service);
			if(b != null) break;
		}
		service.setIcon(b);
		
		saveIconsToCache(service);	
		
		return b;
	}
	
	
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

	
	
	
	public Bitmap populateWebTVItemsWithIcon(WebTVItem item){
		Bitmap b = WebTVCommand.instance().getIcon(item);
		item.setIcon(b);
		return b;
	}
	
	public void populateWebTVItemsWithIcon(WebTVItem item[]){
		for(WebTVItem ite : item){
			Bitmap b = WebTVCommand.instance().getIcon(ite);
			ite.setIcon(b);		
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
	
}
