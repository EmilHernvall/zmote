package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

public class RemoteControl implements Observer {
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
	
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
