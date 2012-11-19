package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import se.z_app.stb.MediaItem;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.api.MonoDirectionalCmdInterface;
import se.z_app.stb.api.RemoteControl.Button;


public class RCCommand implements MonoDirectionalCmdInterface {
	
	
	private String iPAdress;

	private enum Method{
		SENDTEXT, SENDBUTTON, LAUNCH, PLAYWEBTV, QUEUEWEBTV, FACEBOOKAUTH, RAWPOST, RAWGET, CREATENODE;
	}
	/**
	 * Constructor that takes the IP address of the STB as in argument.
	 * @param iP
	 */
	public RCCommand(String iP){
		iPAdress = iP.trim();
	}
	
	/**
	 * Sends a text string to the STB. 
	 */
	public void sendText(String text) {
		new Thread(new RCCommandRunnable(Method.SENDTEXT, iPAdress, text)).start();
	}

	/**
	 * Sends a button command to the STB.
	 */
	public void sendButton(Button button) {
	
		new Thread(new RCCommandRunnable(Method.SENDBUTTON, iPAdress, buttonToString(button))).start();
		
	}

	/**
	 * Launches the note corresponding to the url. Whatever that is.
	 */
	public void launch(String url) {
		new Thread(new RCCommandRunnable(Method.LAUNCH, iPAdress, url)).start();
		
	}
	
	@Override
	public void launch(MediaItem item) {
		new Thread(new RCCommandRunnable(Method.CREATENODE, iPAdress, item.getName(), item.getUrl())).start();
		
	}	

	/**
	 * Plays the webtvitem.
	 */
	public void playWebTV(WebTVItem item) {
		
		new Thread(new RCCommandRunnable(Method.PLAYWEBTV, iPAdress, item.getId())).start();
		
	}

	/**
	 * Queues the webtv item.
	 */
	public void queueWebTV(WebTVItem item) {
		
		new Thread(new RCCommandRunnable(Method.QUEUEWEBTV, iPAdress, item.getId())).start();
		
	}
	/**
	 * Sends the facebook authorisation.
	 */
	public void facebookAuth(String accesstoken, String expires, String uid) {
		new Thread(new RCCommandRunnable(Method.FACEBOOKAUTH, iPAdress, accesstoken, expires, uid)).start();
		
	}

	/**
	 * Sends a rawpost.
	 */
	public void rawPost(String rawPostData, String uri) {
		new Thread(new RCCommandRunnable(Method.RAWPOST, iPAdress, rawPostData, uri)).start();
		
	}

	/**
	 * Sends a raw get.
	 */
	public void rawGet(String uri) {
		new Thread(new RCCommandRunnable(Method.RAWGET, iPAdress, uri)).start();
		
	}
	
	
	
	
	
	/**
	 * Private function translating 
	 * @param button
	 * @return
	 */
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
		private String address = null;
		private Method method;
		private HttpClient httpclient;
	    private HttpPost httppost;
	    private HttpGet httpGet;

		/**
		 * Constructor with 1 argument.
		 * @param method
		 * @param address
		 * @param arg1
		 */
		@SuppressWarnings("deprecation")
		public RCCommandRunnable(Method method, String address, String arg1){
			this.method = method;
			this.address = address;
			this.arg1 = URLEncoder.encode(arg1);
		}
		/**
		 * Constructor with 2 arguments.
		 * @param method
		 * @param address
		 * @param arg1
		 * @param arg2
		 */
		@SuppressWarnings("deprecation")
		public RCCommandRunnable(Method method, String address, String arg1, String arg2){
			this.method = method;
			this.address = address;
			this.arg1 = URLEncoder.encode(arg1);
			this.arg2 = URLEncoder.encode(arg2);
			
		}
		/**
		 * Constructor with 3 arguments.
		 * @param method
		 * @param address
		 * @param arg1
		 * @param arg2
		 * @param arg3
		 */
		@SuppressWarnings("deprecation")
		public RCCommandRunnable(Method method, String address, String arg1, String arg2, String arg3){
			this.method = method;
			this.address = address;
			this.arg1 = URLEncoder.encode(arg1);
			this.arg2 = URLEncoder.encode(arg2);
			this.arg3 = URLEncoder.encode(arg3);
			
		}



		@Override
		/**
		 * Sends the command to the box
		 */
		public void run() {
			switch(method){
			case FACEBOOKAUTH:
				httpclient = new DefaultHttpClient();
				httpGet = new HttpGet("http://" + address +"/mdio/facebook?accesstoken="+arg1+"&expires="+arg2+"&uid="+arg3);
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case LAUNCH:
				httpclient = new DefaultHttpClient();
				httpGet = new HttpGet("http://" + address +"/mdio/launchurl?url="+arg1);
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case CREATENODE:
				httpclient = new DefaultHttpClient();
				String url = "http://" + address +"/mdio/createnode?name="+arg1 + "&url=" + arg2;
				//System.out.println("URL Sent: " + url);
				httpGet = new HttpGet(url);
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case PLAYWEBTV:
				httpclient = new DefaultHttpClient();
				httpGet = new HttpGet("http://" + address +"/mdio/webtv/play?url=webtv:"+arg1);
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case QUEUEWEBTV:
				httpclient = new DefaultHttpClient();
				httpGet = new HttpGet("http://" + address +"/mdio/webtv/play?url=webtv:"+arg1+"&queue=1");
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case RAWGET:
				httpclient = new DefaultHttpClient();
				httpGet = new HttpGet("http://" + address + arg1);
			    
			    try {
			        httpclient.execute(httpGet);	
	        
			        
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
			case RAWPOST:
			    httpclient = new DefaultHttpClient();
			    httppost = new HttpPost("http://" + address + arg2);
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
				
			case SENDBUTTON:
			    httpclient = new DefaultHttpClient();
			    httppost = new HttpPost("http://" + address + "/cgi-bin/writepipe_key");
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
			    httpclient = new DefaultHttpClient();
			    httppost = new HttpPost("http://" + address + "/cgi-bin/writepipe_text");
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
			default:
				break;
			}		
			
		}
		
	}




}
