package se.z_app.stb.api;

public class RCProxy {
	
	//Singleton
	private static RCProxy instance; 
	public static RCProxy instance(){
		if(instance == null)
			instance = new RCProxy();
		return instance;
		
	}
}
