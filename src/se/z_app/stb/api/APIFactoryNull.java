package se.z_app.stb.api;

public class APIFactoryNull extends AbstractAPIFactory{

	@Override
	public DiscoveryInterface getDiscovery() {
		return null;
	}

	@Override
	public BiDirectionalCmdInterface getBiDirectional() {
		return null;
	}

	@Override
	public MonoDirectionalCmdInterface getMonoDirectional() {
		return null;
	}

	@Override
	public EventListnerInterface getEventListner() {
		return null;
	}

}
