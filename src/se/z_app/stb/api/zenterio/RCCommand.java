package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import se.z_app.stb.WebTVItem;
import se.z_app.stb.api.MonoDirectionalCmdInterface;
import se.z_app.stb.api.RemoteControl.Button;

public class RCCommand implements MonoDirectionalCmdInterface {
	
	private String iPAdress;
	
	/**
	 * Constructor that takes the IP adress of the STB as in argument.
	 * @param iP
	 */
	public RCCommand(String iP){
		iPAdress = iP.trim();
	}
	
	/**
	 * Sends a text string to the STB.
	 */
	public void sendText(String text) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://" + iPAdress + "/cgi-bin/writepipe_text");
   
	    try {
			httppost.setEntity(new StringEntity(text));
	        httpclient.execute(httppost);	 
	        
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}

	/**
	 * Sends a button command to the STB.
	 */
	public void sendButton(Button button) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://" + iPAdress + "/cgi-bin/writepipe_key");
	    String text = buttonToString(button);
	
	    try {
			httppost.setEntity(new StringEntity(text));
	        httpclient.execute(httppost);	 
	        
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public void launch(String url) {
		// TODO Auto-generated method stub
		
	}

	
	public void playWebTV(WebTVItem item) {
		// TODO Auto-generated method stub
		
	}


	public void queueWebTV(WebTVItem item) {
		// TODO Auto-generated method stub
		
	}

	public void facebookAuth(String accesstoken, String expires, String uid) {
		// TODO Auto-generated method stub
		
	}

	
	public void rawPost(String rawPostData, String uri) {
		// TODO Auto-generated method stub
		
	}

	
	public void rawGet(String uri) {
		// TODO Auto-generated method stub
		
	}
	
	private String buttonToString(Button button){
		String returnString = button.toString();
		if(returnString.substring(0, 0) != "P")
			returnString = "P"+returnString;
		returnString.toLowerCase();
		return returnString;
		
	}

	
}
