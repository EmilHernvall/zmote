package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

public class STBState implements Observer{
	
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
			
		}
	}

}
