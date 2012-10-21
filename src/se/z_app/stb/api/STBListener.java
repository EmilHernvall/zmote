package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;


import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;
import se.z_app.stb.api.zenterio.EventListener;

public class STBListener extends Observable implements Observer, Runnable{

	//Singleton and adding itself as an observer
	private EventListnerInterface eventListener;

	private Thread myThread;
	private STB stb;
	
	private static class SingletonHolder { 
        public static final STBListener INSTANCE = new STBListener();
	}
		
	public static STBListener instance(){
		return SingletonHolder.INSTANCE;
	}
	private STBListener(){
		STBContainer.instance().addObserver(this);
	}
	

	
	
	public void update(Observable obsesrvable, Object data) {
		stb = STBContainer.instance().getSTB();
		if(eventListener != null)
			eventListener.stop();
		eventListener = AbstractAPIFactory.getFactory(stb).getEventListner();
		
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
