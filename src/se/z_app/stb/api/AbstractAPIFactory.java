package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.APIFactoryZenterio;

/**
 * Creates a factory that produces an API set for a specific STB brand.
 * Allows us to more easily produce the application for boxes other than Zenterio's.
 * @author Rasmus Holm
 */
public abstract class AbstractAPIFactory {
	
	/**
	 * This method returns a APIFactory that is suited for the STB that is passed as an argument
	 * @param stb the STB that an APIFactory is needed for
	 * @return a APIFactory for the specified STB, with which APIs for the STB can be created.
	 */
	public static synchronized AbstractAPIFactory getFactory(STB stb){
		if(stb == null){
			return new APIFactoryNull();
		}
		switch (stb.getType()) {
		case ZENTERIO:
			return new APIFactoryZenterio(stb);
		default:
			return new APIFactoryNull();
		}
	}
	
	/**
	 * 
	 * @return a realization of the DiscoveryInterface for a specific type of STB
	 */
	public abstract DiscoveryInterface getDiscovery();
	/**
	 * 
	 * @return a realization of the BiDirectionalCmdInterface for a specific type of STB
	 */
	public abstract BiDirectionalCmdInterface getBiDirectional();
	/**
	 * 
	 * @return a realization of the MonoDirectionalCmdInterface for a specific type of STB
	 */
	public abstract MonoDirectionalCmdInterface getMonoDirectional();
	/**
	 * 
	 * @return a realization of the EventListnerInterface for a specific type of STB
	 */
	public abstract EventListnerInterface getEventListner();
}
