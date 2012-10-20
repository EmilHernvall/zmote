package se.z_app.stb.api;

import java.util.concurrent.locks.ReentrantLock;



/**
 * 
 * @author Leonard Jansson, Viktor von Zeipel
 * 
 *
 */
public class RCProxy {

	private STBState.State state; 
	private ReentrantLock reentrantlock;

	//Singleton
	private static RCProxy instance; 
	public static RCProxy instance(){
		if(instance == null)
			instance = new RCProxy();
		return instance;

	}

	private RCProxy() {
		reentrantlock = new ReentrantLock(true);
		setState(STBState.State.CHANNELVIEW);
	}
	
	/**
	 * Sends the correct button enum for different states when up is pressed
	 */
	public void up() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLPLUS);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLPLUS);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.UP);	
		}
	}

	/**
	 * Sends the correct button enum for different states when down is pressed
	 */	
	public void down() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLMINUS);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLMINUS);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.DOWN);	
		}
	}

	/**
	 * Sends the correct button enum for different states when right is pressed
	 */
	public void right() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.RIGHT);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.UP);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.RIGHT);	
		}
	}	

	/**
	 * Sends the correct button enum for different states when left is pressed
	 */
	public void left() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.LEFT);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.DOWN);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.LEFT);	
		}
	}

	/**
	 * Sends the correct button enum for different states when ok is pressed
	 */
	public void ok() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);	
		}		
	}
	
	/**
	 * Sends the correct button enum for different states when back is pressed
	 */
	public void back() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);	
		}	
	}
	
	/**
	 * Sends the correct button enum for different states when mute is pressed
	 */
	public void mute() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);	
		}	
	}

	/**
	 * Sends the correct button enum for different states when info is pressed
	 */
	public void info() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);	
		}	
	}
	
	/**
	 * Sends the correct button enum for different states when menu is pressed
	 */
	public void menu() {
		if (getState() == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);
		}
		else if (getState() == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);		
		}
		else if (getState() == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);	
		}
	
	}

	/**
	 * Get the current state 
	 */
	public STBState.State getState() {
		reentrantlock.lock();
		STBState.State tempState = state;
		reentrantlock.unlock();
		return tempState;
		
	}
	
	/**
	 * Sets the state 
	 */
	public void setState(STBState.State state) {
		reentrantlock.lock();
		this.state = state;
		reentrantlock.unlock();
	}
}
