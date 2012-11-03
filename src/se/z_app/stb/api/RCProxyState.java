package se.z_app.stb.api;

/**
 * An abstract state class specifying any functions needed in a state, default state is now
 * found under default state class. If other states are needed extends this class.
 * All commands, such as up() corresponds RemoteControl.Button.UP
 * @author Rasmus Holm, refractord Linus Back
 *
 */
public abstract class RCProxyState {
	
	
	public abstract void up();

	public abstract void down();
	
	public abstract void right();

	public abstract void left() ;

	public abstract void ok();	
	
	public abstract void back();	
	
	public abstract void mute();
	
	public abstract void info();
	
	public abstract void menu();
	
	public abstract void exit();
	
	
}
