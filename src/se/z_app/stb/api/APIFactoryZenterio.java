package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;
import se.z_app.stb.api.zenterio.EventListener;
import se.z_app.stb.api.zenterio.RCCommand;
import se.z_app.stb.api.zenterio.StandardCommand;

public class APIFactoryZenterio extends AbstractAPIFactory {

	STB stb;
	public APIFactoryZenterio(STB stb){
		this.stb = stb;
	}
	
	@Override
	public DiscoveryInterface getDiscovery() {
		return new Discovery(stb.getIP());
	}

	@Override
	public BiDirectionalCmdInterface getBiDirectional() {
		return new StandardCommand(stb.getIP());
	}

	@Override
	public MonoDirectionalCmdInterface getMonoDirectional() {
		return new RCCommand(stb.getIP());
	}

	@Override
	public EventListnerInterface getEventListner() {
		
		//TODO Implement this when EventLisner is Implemented
		return new EventListener();
	}

}
