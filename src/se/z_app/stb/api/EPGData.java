package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

public class EPGData implements Observer{
	//Singleton and adding itself as an observer
	private static EPGData instance; 
	private EPGData(){
		STBContainer.instance().addObserver(this);
	}
	public static EPGData instance(){
		if(instance == null)
			instance = new EPGData();
		return instance;
	}
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
