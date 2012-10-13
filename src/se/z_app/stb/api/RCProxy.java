package se.z_app.stb.api;

public class RCProxy {

	private STBState.State state; 

	//Singleton
	private static RCProxy instance; 
	public static RCProxy instance(){
		if(instance == null)
			instance = new RCProxy();
		return instance;

	}

	public void up() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.VOLPLUS);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.VOLPLUS);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.UP);	
		}
	}

	public void down() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.VOLMINUS);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.VOLMINUS);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.DOWN);	
		}
	}

	public void right() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.RIGHT);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.UP);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.RIGHT);	
		}
	}	

	public void left() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.LEFT);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.DOWN);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.LEFT);	
		}
	}

	public void ok() {		
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.OK);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.OK);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.OK);	
		}		
	}
	
	public void back() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.BACK);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.BACK);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.BACK);	
		}	
	}
	
	public void mute() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.MUTE);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.MUTE);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.MUTE);	
		}	
	}

	public void info() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.INFO);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.INFO);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.INFO);	
		}	
	}
	
	public void menu() {
		if (state == STBState.State.MENU) {
			RemoteControl.sendButton(RemoteControl.Button.MENU);
		}
		else if (state == STBState.State.CHANNELVIEW) {
			RemoteControl.sendButton(RemoteControl.Button.MENU);		
		}
		else if (state == STBState.State.SETTINGS) {
			RemoteControl.sendButton(RemoteControl.Button.MENU);	
		}	
	}

	public STBState.State getState() {
		return state;
	}
	public void setState(STBState.State state) {
		this.state = state;
	}
}
