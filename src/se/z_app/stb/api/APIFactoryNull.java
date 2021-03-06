package se.z_app.stb.api;

/**
 * Creates an API factory where all interfaces are null. It's the default
 * API Factory used when no else is found.
 * @author Rasmus Holm
 *
 */
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
