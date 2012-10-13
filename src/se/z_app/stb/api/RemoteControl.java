package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

public class RemoteControl implements Observer {
	public enum Button{
		P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, BACK, BLUE, BROWSERHOME, CHANNELMINUS, CHANNELPLUS, DOWN, EXIT, GREEN, GUIDE, INFO, LEFT, MENU, OK, OPT, RED, RIGHT, UP, VOLMINUS, VOLPLUS, YELLOW, MUTE;
	}
	
	
	//Singleton and adding itself as an observer
	private static RemoteControl instance; 
	private RemoteControl(){
		STBContainer.instance().addObserver(this);
	}
	public static RemoteControl instance(){
		if(instance == null)
			instance = new RemoteControl();
		return instance;
	}
	
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
