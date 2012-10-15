package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;


import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;
import se.z_app.stb.api.zenterio.EventListener;

public class STBListener extends Observable implements Observer, Runnable{

	//Singleton and adding itself as an observer
	private EventListnerInterface eventListener;
	private static STBListener instance; 
	private Thread myThread;
	private STB stb;
	
	private STBListener(){
		STBContainer.instance().addObserver(this);
	}
	
	
	public static STBListener instance(){
		if(instance == null)
			instance = new STBListener();
		return instance;
	}
	

	
	
	public void update(Observable obsesrvable, Object data) {
		stb = STBContainer.instance().getSTB();		
		switch(stb.getType()){
		case DEFAULT:
			break;
		case ZENTERIO:
			if(eventListener != null)
				eventListener.stop();
			eventListener = new EventListener();
			break;
		default:
			break;
		
		}
		myThread = new Thread(this);
		myThread.start();
		
	}

	public void run() {
		
		STBEvent event;
		eventListener.init(stb);
		
		
		switch(stb.getType()){
		case DEFAULT:
			break;
		case ZENTERIO:
			while((event = eventListener.getNextEvent())!=null){
				this.setChanged();
				this.notifyObservers(event);

			}
			break;
		default:
			break;
		
		}


		
	}
	

}
