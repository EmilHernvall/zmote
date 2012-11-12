package se.z_app.stb.api;

import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.Channel;
import se.z_app.stb.STB;

/**
 * Remote control needs a STB in the STBContainer to be able to send 
 * commands.
 * @author Linus Back
 * TODO: Add Javadoc comments for functions
 *
 */
public class RemoteControl implements Observer {
	public enum Button{
		P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, BACK, BLUE, BROWSERHOME, CHANNELMINUS, CHANNELPLUS, DOWN, EXIT, GREEN, GUIDE, INFO, LEFT, MENU, OK, OPT, RED, RIGHT, UP, VOLMINUS, VOLPLUS, YELLOW, MUTE;
	}
	private MonoDirectionalCmdInterface remoteImpl;
	
	
	//Singleton and adding itself as an observer
	private static class SingletonHolder { 
        public static final RemoteControl INSTANCE = new RemoteControl();
	}
	
	public static RemoteControl instance(){
		return SingletonHolder.INSTANCE;
	}
	private RemoteControl(){
		STBContainer.instance().addObserver(this);
	}
	
	public void update(Observable observable, Object data) {
		STB stb = STBContainer.instance().getActiveSTB();
		remoteImpl = AbstractAPIFactory.getFactory(stb).getMonoDirectional();
		
	}
	
	public void sendText(String chars){
		if(remoteImpl != null)
			remoteImpl.sendText(chars);
	
	}
	public void sendButton(Button button){
		if(remoteImpl != null)
			remoteImpl.sendButton(button);
	}
	
	public void launch(String url){
		if(remoteImpl != null)
			remoteImpl.launch(url);
	
	}
	
	public void launch(Channel channel){
		launch(channel.getUrl()+"&clid=0");	
	}
}
