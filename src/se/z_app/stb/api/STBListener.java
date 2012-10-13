package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

public class STBListener extends Observable implements Observer, Runnable{

	//Singleton and adding itself as an observer
	private static STBListener instance; 
	private STBListener(){
		STBContainer.instance().addObserver(this);
	}
	public static STBListener instance(){
		if(instance == null)
			instance = new STBListener();
		return instance;
	}
	

	
	
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
	

}
