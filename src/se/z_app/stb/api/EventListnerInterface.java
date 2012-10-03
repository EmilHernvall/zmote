package se.z_app.stb.api;

import se.z_app.stb.STB;

public interface EventListnerInterface {
	public void init(STB stb);
	public String getCurrentEvent();
	public String getNextEvent();
	
}
