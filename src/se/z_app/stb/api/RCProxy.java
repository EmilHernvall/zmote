package se.z_app.stb.api;

/**
 * 
 * @author Leonard Jansson, Viktor von Zeipel
 * 
 *
 */
public class RCProxy {

	private STBState.State state; 

	//Singleton
	private static RCProxy instance; 
	public static RCProxy instance(){
		if(instance == null)
			instance = new RCProxy();
		return instance;

	}

	private RCProxy() {
		
	}
	
	/*
	 * Sends the correct button enum for different states when up is pressed
	 */
	public void up() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLPLUS);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLPLUS);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.UP);	
		}
	}

	/*
	 * Sends the correct button enum for different states when down is pressed
	 */	
	public void down() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLMINUS);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.VOLMINUS);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.DOWN);	
		}
	}

	/*
	 * Sends the correct button enum for different states when right is pressed
	 */
	public void right() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.RIGHT);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.UP);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.RIGHT);	
		}
	}	

	/*
	 * Sends the correct button enum for different states when left is pressed
	 */
	public void left() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.LEFT);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.DOWN);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.LEFT);	
		}
	}

	/*
	 * Sends the correct button enum for different states when ok is pressed
	 */
	public void ok() {		
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.OK);	
		}		
	}
	
	/*
	 * Sends the correct button enum for different states when back is pressed
	 */
	public void back() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.BACK);	
		}	
	}
	
	/*
	 * Sends the correct button enum for different states when mute is pressed
	 */
	public void mute() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);	
		}	
	}

	/*
	 * Sends the correct button enum for different states when info is pressed
	 */
	public void info() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.INFO);	
		}	
	}
	
	/*
	 * Sends the correct button enum for different states when menu is pressed
	 */
	public void menu() {
		if (state == STBState.State.MENU) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.instance().sendButton(RemoteControl.Button.MENU);	
		}	
	}

	/*
	 * Get the current state 
	 */
	public STBState.State getState() {
		return state;
	}
	
	/*
	 * Sets the state 
	 */
	public void setState(STBState.State state) {
		this.state = state;
	}
}
