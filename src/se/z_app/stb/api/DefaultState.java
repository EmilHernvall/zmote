package se.z_app.stb.api;

/**
 * A default state for the RCProxy. It does'nt deviate from the standard 
 * implementation. For more information see the RC proxy state class.
 * @see RCProxyState
 * @author Linus Back
 *
 */
class DefaultState extends RCProxyState {

	@Override
	public void up() {
		RemoteControl.instance().sendButton(RemoteControl.Button.UP);
	
	}

	@Override
	public void down() {
		RemoteControl.instance().sendButton(RemoteControl.Button.DOWN);
	
	}

	@Override
	public void right() {
		RemoteControl.instance().sendButton(RemoteControl.Button.RIGHT);
		
	}	

	@Override
	public void left() {
		RemoteControl.instance().sendButton(RemoteControl.Button.LEFT);
		
	}

	@Override
	public void ok() {
		RemoteControl.instance().sendButton(RemoteControl.Button.OK);
		
	}
	
	@Override
	public void back() {
		RemoteControl.instance().sendButton(RemoteControl.Button.BACK);
		
	}
	
	@Override
	public void mute() {
		RemoteControl.instance().sendButton(RemoteControl.Button.MUTE);
			
	}

	@Override
	public void info() {
		RemoteControl.instance().sendButton(RemoteControl.Button.INFO);
		
	}
	
	@Override
	public void menu() {
		RemoteControl.instance().sendButton(RemoteControl.Button.MENU);
	}
	@Override
	public void exit() {
		RemoteControl.instance().sendButton(RemoteControl.Button.EXIT);
	}
}
