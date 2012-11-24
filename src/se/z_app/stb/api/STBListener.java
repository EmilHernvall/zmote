package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;


import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;

/**
 * Class that listens to a eventlistener.
 * @author Linus Back
 *
 */
public class STBListener extends Observable implements Observer, Runnable{

	//Singleton and adding itself as an observer
	private EventListnerInterface eventListener;

	private Thread myThread;
	private STB stb;
	private STBEvent event;
	
	private static class SingletonHolder { 
        public static final STBListener INSTANCE = new STBListener();
	}
		
	public static STBListener instance(){
		return SingletonHolder.INSTANCE;
	}
	private STBListener(){
		STBContainer.instance().addObserver(this);

	}
	
	/**
	 * Might be unnecessary with this override, needs to look at this more
	 */
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		if(eventListener !=null && eventListener.getCurrentEvent() != null){
			this.setChanged();
			this.notifyObservers(eventListener.getCurrentEvent());
		}
		
	}
	
	

	public STBEvent getCurrentEvent(){
		return event;
	}
	
	
	public void update(Observable obsesrvable, Object data) {
		stb = STBContainer.instance().getActiveSTB();
		if(eventListener != null)
			eventListener.stop();
		eventListener = AbstractAPIFactory.getFactory(stb).getEventListner();
		
		myThread = new Thread(this);
		myThread.start();
		
	}

	public void run() {
		
		System.out.println("Initiating Listner");
		eventListener.init(stb);
		
		//System.out.println("waiting for events");
		while((event = eventListener.getNextEvent())!=null){
			//System.out.println("Recived Event");
			this.setChanged();
			this.notifyObservers(event);

		}		
	}
}
