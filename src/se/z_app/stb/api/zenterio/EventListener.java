package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.InputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;
import se.z_app.stb.api.EventListnerInterface;

/**
 * Class that handles the events sent from the box.
 * refactored to work with the new firmware.
 * @author Linus Back
 *
 */
public class EventListener implements EventListnerInterface {

	private String IPaddress;
	private STBEvent currentEvent;
	private Socket socket;
	private InputStream in;
	private byte[] buffer;

	/**
	 * Initializes the event listener.
	 * @param stb
	 */
	public void init(STB stb) {
		IPaddress = stb.getIP();
		
		try {
			socket = new Socket(IPaddress, 9999);
			in = socket.getInputStream();
			buffer = new byte[512];
		} catch (UnknownHostException e) { 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Listner is initiated");

	}

	/**
	 * Gets the current event.
	 */
	public STBEvent getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * Gets the next event. If not initialized returns current event.
	 */
	public STBEvent getNextEvent() {
		if(socket == null){
			return null;
		}
		
		else if(socket.isConnected()){
			try {
				int len = in.read(buffer);
				currentEvent = stringToSTBEvent(new String(buffer, 0, len));
			} catch (IOException e) {
				return null;
			}
		}
		

		return currentEvent;
	}

	/**
	 * Stops the eventListener, needs to be initialized 
	 * before getNextEvent() can be called again.
	 */
	public void stop(){
		if(socket != null){
			try {
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch( RuntimeException e){

			}
		}
	}

	/**
	 * Converts the string of events into STB events 
	 * @param eventString
	 */
	private STBEvent stringToSTBEvent(String eventString){

		try {
			JSONObject json = new JSONObject(eventString);
			currentEvent = new STBEvent();
			currentEvent.setType(json.getString("type"));
			if(currentEvent.getType().equals("nodeLaunch")){
				currentEvent.setLabel(json.getString("label"));
				currentEvent.setUrl(json.getString("url"));
			}
			else if(currentEvent.getType().equals("volume")){
				currentEvent.setValue(Integer.parseInt(json.getString("value")));
			}
			else if(currentEvent.getType().equals("mute")){
				String value = json.getString("state");
				if(value.equals("1")){
					currentEvent.setState(true);
				}
				else{
					currentEvent.setState(false);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return currentEvent;
	}
}
