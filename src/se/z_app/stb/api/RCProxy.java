package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.STBEvent;

/**
 * 
 * @author Leonard Jansson, Viktor von Zeipel, refractored by Rasmus Holm, Linus Back
 * 
 * TODO: Implementation.
 */
public class RCProxy implements Observer{


	private RCProxyState state;
	
	//Singleton
	private static class SingletonHolder { 
        public static final RCProxy INSTANCE = new RCProxy();
	}
	public static RCProxy instance(){
		return SingletonHolder.INSTANCE;
	}
	
	private RCProxy() {
		state = new DefaultState();
		STBListener.instance().addObserver(this);
	}
	
	/**
	 * Sends the correct button enum for different states when up is pressed
	 */
	public void up() {
		state.up();
	}

	/**
	 * Sends the correct button enum for different states when down is pressed
	 */	
	public void down() {
		state.down();
	}

	/**
	 * Sends the correct button enum for different states when right is pressed
	 */
	public void right() {
		state.right();
	}	

	/**
	 * Sends the correct button enum for different states when left is pressed
	 */
	public void left() {
		state.left();
	}

	/**
	 * Sends the correct button enum for different states when ok is pressed
	 */
	public void ok() {
		state.ok();
	}
	
	/**
	 * Sends the correct button enum for different states when back is pressed
	 */
	public void back() {
		state.back();
	}
	
	/**
	 * Sends the correct button enum for different states when mute is pressed
	 */
	public void mute() {
		state.mute();
	}

	/**
	 * Sends the correct button enum for different states when info is pressed
	 */
	public void info() {
		state.info();
	}
	
	/**
	 * Sends the correct button enum for different states when menu is pressed
	 */
	public void menu() {
		state.menu();
	}
	
	public void exit() {
		state.exit();
	}

	/**
	 * Get the current state 
	 */
	public RCProxyState getState() {
		return state;
	}
	
	/**
	 * Sets the state 
	 */
	public void setState(RCProxyState state) {
		this.state = state;
	}

	@Override
	public void update(Observable observable, Object data) {
		/*
		 * Code for setting different states depending on events sent from the box
		 *  should go here.
		 */
		if(data instanceof STBEvent){
			
		}
		
	}
}
