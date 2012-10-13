package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.BiDirectionalCmdInterface;

public class StandardCommand implements BiDirectionalCmdInterface{
	private String ip;

	public StandardCommand(String ip){
		this.ip = ip.trim();
	}
	
	public EPG getEPG() {
		
		return null;
	}

	
	public Channel getChannel() {
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpget = new HttpPost("http://" + ip + "/mdio/currentchannel");

	    try {
	        
	      
	       // System.out.println("Sending Post: " + postText);
	        HttpResponse response = httpclient.execute(httpget);
	        InputStream in = response.getEntity().getContent();
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
		
		return null;
	}

	
	public Bitmap getChannelIcon(Channel channel) {
		
		return null;
	}

	
	public WebTVService[] getWebTVServices() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Bitmap getWebTVServiceIcon(WebTVService serivce) {
		// TODO Auto-generated method stub
		return null;
	}


	public Bitmap getWebTVItemIcon(WebTVItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	public WebTVItem[] searchWebTVService(String query, WebTVService service) {
		// TODO Auto-generated method stub
		return null;
	}

}
