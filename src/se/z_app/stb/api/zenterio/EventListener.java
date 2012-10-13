package se.z_app.stb.api.zenterio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import se.z_app.stb.STB;
import se.z_app.stb.api.EventListnerInterface;

public class EventListener implements EventListnerInterface {

	private String iPaddress;
	private String currentEvent;
	private Socket socket;
	private BufferedReader in;
	
	/**
	 * Initializes the event listener.
	 */
	public void init(STB stb) {
		iPaddress = stb.getIP();
		try {
			socket = new Socket(iPaddress, 9999);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Gets the current event.
	 */
	public String getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * Gets the next event. If not initialized returns current event.
	 */
	public String getNextEvent() {
		if(socket.isConnected()){
			try {
				currentEvent = in.readLine();
			} catch (IOException e) {
				return "EOF";
			}
		}
		return currentEvent;
	}
	
	/**
	 * Stops the eventListener, needs to be initialized 
	 * before getNextEvent() can be called again.
	 */
	public void stop(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
