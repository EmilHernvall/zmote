package se.z_app.stb.api.zenterio;

import se.z_app.stb.STB;
import se.z_app.stb.api.AbstractAPIFactory;
import se.z_app.stb.api.BiDirectionalCmdInterface;
import se.z_app.stb.api.DiscoveryInterface;
import se.z_app.stb.api.EventListnerInterface;
import se.z_app.stb.api.MonoDirectionalCmdInterface;

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
