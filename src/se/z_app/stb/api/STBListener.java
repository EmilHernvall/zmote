package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;


import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;

/**
 * Class that listens to a event listener.
 * 
 * @author Linus Back
 */
public class STBListener extends Observable implements Observer, Runnable{

	private EventListnerInterface eventListener;

	private Thread myThread;
	private STB stb;
	private STBEvent event;
	
	/**
	 * Creates and holds the singleton instance of the STB listener
	 * @author Linus Back
	 */
	private static class SingletonHolder { 
        public static final STBListener INSTANCE = new STBListener();
	}
		
	/**
	 * Request the singleton instance of the STB listener
	 * @return The instance of STB listener
	 */
	public static STBListener instance(){
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Private constructor for the STB listener
	 */
	private STBListener(){
		STBContainer.instance().addObserver(this);
	}
	
	/**
	 * Adds an observer object to the STB listener instance
	 * @param observer - The observer to be added
	 */
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		if(eventListener !=null && eventListener.getCurrentEvent() != null){
			this.setChanged();
			this.notifyObservers(eventListener.getCurrentEvent());
		}	
	}
	
	/**
	 * Getter for the current STB event
	 * @return The current STB event
	 */
	public STBEvent getCurrentEvent(){
		return event;
	}
	
	/**
	 * Updates the STB listener
	 */
	public void update(Observable obsesrvable, Object data) {
		stb = STBContainer.instance().getActiveSTB();
		if(eventListener != null)
			eventListener.stop();
		eventListener = AbstractAPIFactory.getFactory(stb).getEventListner();
		
		myThread = new Thread(this);
		myThread.start();
		
	}

	/**
	 * Initiates the STB listener
	 */
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
