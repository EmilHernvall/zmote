package se.z_app.zmote.webtv;


import android.graphics.Bitmap;
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
	
	public WebTVService[] getService(){
		return WebTVCommand.instance().getService();
	}
	
	public Bitmap populateWithIcon(WebTVService service){
		Bitmap b = WebTVCommand.instance().getIcon(service);
		service.setIcon(b);
		return b;
	}
	
	public void populateWithIcon(WebTVService services[]){
		for(WebTVService serv : services){
			Bitmap b = WebTVCommand.instance().getIcon(serv);
			serv.setIcon(b);		
		}
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
