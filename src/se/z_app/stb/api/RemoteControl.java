package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.Channel;
import se.z_app.stb.MediaItem;
import se.z_app.stb.STB;

/**
 * Remote control needs a STB in the STBContainer to be able to send 
 * commands.
 * @author Linus Back
 */
public class RemoteControl implements Observer {
	
	/**
	 * Defines the available buttons on the virtual remote
	 * @author Linus Back
	 */
	public enum Button {
		P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, BACK, BLUE, BROWSERHOME, CHANNELMINUS, CHANNELPLUS, DOWN, 
		EXIT, GREEN, GUIDE, INFO, LEFT, MENU, OK, OPT, RED, RIGHT, UP, VOLMINUS, VOLPLUS, YELLOW, MUTE, 
		TOGGLEPAUSEPLAY;
	}
	private MonoDirectionalCmdInterface remoteImpl;
	
	/**
	 * Creates and holds the singleton instance of the remote control
	 * @author Linus Back
	 */
	private static class SingletonHolder { 
        public static final RemoteControl INSTANCE = new RemoteControl();
	}
	
	/**
	 * Request the singleton instance of the remote control
	 * @return The instance of remote control
	 */
	public static RemoteControl instance(){
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Creates remote control and adds it as an observer of the STB
	 */
	private RemoteControl(){
		STBContainer.instance().addObserver(this);
	}
	
	/**
	 * Updates the remote control, with correspondence to the STB
	 */
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getActiveSTB();
		remoteImpl = AbstractAPIFactory.getFactory(stb).getMonoDirectional();
	}
	
	/**
	 * Sends a text string to the STB
	 * @param chars - The string to send
	 */
	public void sendText(String chars){
		if(remoteImpl != null)
			remoteImpl.sendText(chars);
	}
	
	/**
	 * Sends a button press to the STB 
	 * @param button - The button that has been pressed
	 */
	public void sendButton(Button button){
		if(remoteImpl != null)
			remoteImpl.sendButton(button);
	}
	
	/**
	 * Sends a URL to be launched by the STB
	 * @param url - The URL to be sent
	 */
	public void launch(String url){
		if(remoteImpl != null)
			remoteImpl.launch(url);
	}
	
	/**
	 * Sends a media item to be launched by the STB
	 * @param item - The media item to be sent
	 */
	public void launch(MediaItem item){
		if(remoteImpl != null)
			remoteImpl.launch(item);
	}
	
	/**
	 * Sends a channel to be played by the STB
	 * @param channel - The channel to be sent by the STB
	 */
	public void launch(Channel channel){
		launch(channel.getUrl()+"&clid=0");	
	}
}
