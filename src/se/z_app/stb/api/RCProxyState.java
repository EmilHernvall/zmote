package se.z_app.stb.api;

/**
 * An abstract state class specifying any functions needed in a state, default state is now
 * found under default state class. If other states are needed extends this class.
 * All commands, such as up() corresponds RemoteControl.Button.UP
 * @author Rasmus Holm, refractored Linus Back
 *
 */
public abstract class RCProxyState {
	
	/**
	 * As a standard corresponds to RemoteControl.Button.UP
	 */
	public abstract void up();

	/**
	 *  As a standard corresponds to RemoteControl.Button.DOWN
	 */
	public abstract void down();
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.RIGHT
	 */
	public abstract void right();
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.LEFT
	 */
	public abstract void left() ;

	/**
	 *  As a standard corresponds to RemoteControl.Button.OK
	 */
	public abstract void ok();	
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.BACK
	 */
	public abstract void back();	
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.MUTE
	 */
	public abstract void mute();
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.INFO
	 */
	public abstract void info();
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.MENU
	 */
	public abstract void menu();
	
	/**
	 *  As a standard corresponds to RemoteControl.Button.EXIT
	 */
	public abstract void exit();
	
}
