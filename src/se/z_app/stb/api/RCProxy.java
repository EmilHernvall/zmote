package se.z_app.stb.api;





/**
 * 
 * @author Leonard Jansson, Viktor von Zeipel, refractord by Rasmus Holm
 * 
 *
 */
public class RCProxy {


	private RCProxyState state;
	
	//Singleton
	private static RCProxy instance; 
	public static RCProxy instance(){
		if(instance == null)
			instance = new RCProxy();
		return instance;

	}

	private RCProxy() {
		state = new RCProxyState();
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
}
