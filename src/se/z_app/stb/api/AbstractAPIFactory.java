package se.z_app.stb.api;

import se.z_app.stb.STB;

public abstract class AbstractAPIFactory {
	public static synchronized AbstractAPIFactory getFactory(STB stb){
		switch (stb.getType()) {
		case ZENTERIO:
			return new APIFactoryZenterio(stb);

		default:
			return null;
		}
		
		
	}
	
	public abstract DiscoveryInterface getDiscovery();
	public abstract BiDirectionalCmdInterface getBiDirectional();
	public abstract MonoDirectionalCmdInterface getMonoDirectional();
	public abstract EventListnerInterface getEventListner();
}
