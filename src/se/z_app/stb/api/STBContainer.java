package se.z_app.stb.api;
import java.util.Observable;
import se.z_app.stb.STB;




public class STBContainer extends Observable {
	private STB stb;
	
	
	
	private static STBContainer instance; 
	private STBContainer(){}
	public static STBContainer instance(){
		if(instance == null)
			instance = new STBContainer();
		return instance;
		
	}
	
	public STB getSTB(){
		return stb;
	}
	
	public void setSTB(STB stb){
		if(this.stb.equals(stb)) return;
		
		this.stb = stb;
		super.setChanged();
		super.notifyObservers();
	}
	
}
