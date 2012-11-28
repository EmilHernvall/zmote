package se.z_app.stb.api.zenterio;

import se.z_app.stb.STB;
import se.z_app.stb.api.AbstractAPIFactory;
import se.z_app.stb.api.BiDirectionalCmdInterface;
import se.z_app.stb.api.DiscoveryInterface;
import se.z_app.stb.api.EventListnerInterface;
import se.z_app.stb.api.MonoDirectionalCmdInterface;

/**
 * TODO: class description
 * @author Rasmus Holm 
 */
public class APIFactoryZenterio extends AbstractAPIFactory {

	STB stb;

	/**
	 * Constructor
	 * @param stb
	 */
	public APIFactoryZenterio(STB stb){
		this.stb = stb;
	}
	
	/**
	 * Discovers the interface of the STB
	 */
	@Override
	public DiscoveryInterface getDiscovery() {
		return new Discovery(stb.getIP());
	}

	/**
	 * Gets BiDirectional commands interface of the STB
	 */
	@Override
	public BiDirectionalCmdInterface getBiDirectional() {
		return new StandardCommand(stb.getIP());
	}

	/**
	 * Gets MonoDirectional commands interface of the STB
	 */
	@Override
	public MonoDirectionalCmdInterface getMonoDirectional() {
		return new RCCommand(stb.getIP());
	}

	/**
	 * Event Listener interface of the STB
	 */
	@Override
	public EventListnerInterface getEventListner() {	
		return new EventListener();
	}

}
