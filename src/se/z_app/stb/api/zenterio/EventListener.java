package se.z_app.stb.api.zenterio;

import java.io.IOException;
import java.io.InputStream;

import java.net.InetSocketAddress;
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

	private String iPaddress;
	private STBEvent currentEvent;
	private Socket socket;
	private InputStream in;
	private byte[] buffer;
	private boolean connectionClosed;
	private Object synclock = new Object();
	private final int timeBetweenReconnectTries = 1000;
	private final int portToConnect = 9999;

	/**
	 * Initializes the event listener.
	 * @param stb
	 */
	public void init(STB stb) {
		iPaddress = stb.getIP();
		
		try {
			socket = new Socket(iPaddress, portToConnect);
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
				System.out.println("Trying to read event from server");
				int len = in.read(buffer);
				currentEvent = stringToSTBEvent(new String(buffer, 0, len));
				System.out.println(currentEvent.getType());
			} catch (IOException e) {
				/*
				 * Will first check if the connection was closed by the 
				 * STBListener.
				 */
				if(isConnectionClosed()){
					System.out.println("Closed success!!!");
					return null;
				}
				/*
				 * If not then will try to close the connection and reconnect 
				 * with a new  socket.
				 */
				while(true){
					try {
						Thread.sleep(timeBetweenReconnectTries);
						if (isConnectionClosed()) {
							return null;
						}
						System.out.println("Trying to reconnect");
						in.close();
						socket.close();
						socket = new Socket();
						socket.connect(new InetSocketAddress(iPaddress, portToConnect), 0);
						System.out.println("Reconnecting done");
						in = socket.getInputStream();
						int len = in.read(buffer);
						currentEvent = stringToSTBEvent(new String(buffer, 0, len));
						break;
						
					} catch (IOException e1) {
						if (isConnectionClosed()) {
							return null;
						}
						System.out.println("Throwing another exception");
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						if (isConnectionClosed()) {
							return null;
						}
						System.out.println("Problem with the sleep function");
						e1.printStackTrace();
					}
				}


				
				
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
				setConnectionClosed(true);
				System.out.println("Forcing close!!!");
				in.close();
				socket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch( RuntimeException e){

			}
		}
	}

	/**
	 * Synchronized getter for boolean connectionClosed.
	 * @return the connectionClosed
	 */
	public boolean isConnectionClosed() {
		synchronized (synclock) {
			return connectionClosed;			
		}
	}

	/**
	 * Synchronized setter for boolean connectionClosed.
	 * @param connectionClosed the connectionClosed to set
	 */
	public void setConnectionClosed(boolean connectionClosed) {
		synchronized (synclock) {
			this.connectionClosed = connectionClosed;			
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
