package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * Enum for different states.
 *
 */
public class STBState implements Observer{
	public enum State{
		MENU, CHANNELVIEW, SETTINGS;
		
	}
	
	
	//Singleton and adding itself as an observer
	private static STBState instance; 
	private STBState(){
		STBContainer.instance().addObserver(this);
		STBListener.instance().addObserver(this);
	}
	public static STBState instance(){
		if(instance == null)
			instance = new STBState();
		return instance;
	}
	
	public void update(Observable observable, Object data) {
		
		if(observable.getClass() == STBContainer.class){
			
		}else if(observable.getClass() == STBListener.class){
			if(data.getClass()==String.class){
				
			}
		}
	}
	
	public State getState() {
		return  null;
		
	}

}
