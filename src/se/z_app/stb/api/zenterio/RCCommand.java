package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.bool;

import se.z_app.stb.WebTVItem;
import se.z_app.stb.api.MonoDirectionalCmdInterface;
import se.z_app.stb.api.RemoteControl.Button;


public class RCCommand implements MonoDirectionalCmdInterface {
	
	
	private String iPAdress;

	private enum Method{
		SENDTEXT, SENDBUTTON, LAUNCH, PLAYWEBTV, QUEUEWEBTV, FACEBOOKAUTH, RAWPOST, RAWGET;
	}
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

	}

	/**
	 * Sends a button command to the STB.
	 */
	public void sendButton(Button button) {
	
		new Thread(new RCCommandRunnable(Method.SENDBUTTON, iPAdress, buttonToString(button))).start();
		
	}

	
	public void launch(String url) {

		
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
		if(!returnString.startsWith("P"))
			returnString = "P"+returnString;
		returnString = returnString.toLowerCase();
		return returnString;
		
	}
	
	/**
	 * Private class that sends the command to the box.
	 * The reason for this class is that it needs to be able to access
	 * variables. And therefore you need a customized constructor.
	 * @author Linus
	 *
	 */
	private class RCCommandRunnable implements Runnable{
		
		
		private String arg1 = null;
		private String arg2 = null;
		private String arg3 = null;
		private WebTVItem webTVItem = null;
		private String address = null;
		private Method method;

		
		public RCCommandRunnable(Method method, String address, String arg1){
			this.method = method;
			this.address = address;
			this.arg1 = arg1;
		}
		public RCCommandRunnable(Method method, String address, String arg1, String arg2){
			this.method = method;
			this.address = address;
			this.arg1 = arg1;
			this.arg2 = arg2;
			
		}
		public RCCommandRunnable(Method method, String address, String arg1, String arg2, String arg3){
			this.method = method;
			this.address = address;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			
		}
		public RCCommandRunnable(Method method, String address, WebTVItem webTVItem){
			this.method = method;
			this.address = address;
			this.webTVItem = webTVItem;
			
		}

		@Override
		public void run() {
			switch(method){
			case FACEBOOKAUTH:
				break;
			case LAUNCH:
				break;
			case PLAYWEBTV:
				break;
			case QUEUEWEBTV:
				break;
			case RAWGET:
				break;
			case RAWPOST:
				break;
				
			case SENDBUTTON:
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://" + address + "/cgi-bin/writepipe_key");
			    try {

					httppost.setEntity(new StringEntity(arg1));
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
				break;
				
			case SENDTEXT:
				break;
			default:
				break;
			}
			
			

			


				
		
			
		}
		
	}	
}
