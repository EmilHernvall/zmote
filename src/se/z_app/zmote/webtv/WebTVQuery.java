package se.z_app.zmote.webtv;


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
		return WebTVCommand.instance().getSevice();
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
