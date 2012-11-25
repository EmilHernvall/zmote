package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.APIFactoryZenterio;

/**
 * Creates a factory that produces an API set for a specific STB brand.
 * Allows us to more easily produce the application for boxes other than Zenterio's.
 * @author Rasmus Holm
 */
public abstract class AbstractAPIFactory {
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
	
	public abstract DiscoveryInterface getDiscovery();
	public abstract BiDirectionalCmdInterface getBiDirectional();
	public abstract MonoDirectionalCmdInterface getMonoDirectional();
	public abstract EventListnerInterface getEventListner();
}
