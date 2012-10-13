package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.EventListener;

public class STBListener extends Observable implements Observer, Runnable{

	//Singleton and adding itself as an observer
	private EventListnerInterface eventListener;
	private static STBListener instance; 
	private Thread myThread;
	
	private STBListener(){
		STBContainer.instance().addObserver(this);
	}
	
	
	public static STBListener instance(){
		if(instance == null)
			instance = new STBListener();
		return instance;
	}
	

	
	
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getSTB();
		switch(stb.getType()){
		case DEFAULT:
			break;
		case ZENTERIO:
			if(eventListener != null)
				eventListener.stop();
			eventListener = new EventListener();
			eventListener.init(stb);
			myThread = new Thread(this);
			myThread.start();

			break;
		default:
			break;
		
		}
		
	}

	public void run() {
		String event;
		while((event = eventListener.getNextEvent())!="EOF"){
			notifyObservers(event);
		}
		
	}
	

}
