package se.z_app.stb.api;

import se.z_app.stb.STB;
import se.z_app.stb.STBEvent;

public interface EventListnerInterface {
	public void init(STB stb);
	public STBEvent getCurrentEvent();
	public STBEvent getNextEvent();
	public void stop();
}
