package se.z_app.stb.api;
import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.STB;




public class STBContainer extends Observable {
	private STB stb;
	
	
	
	private static class SingletonHolder { 
        public static final STBContainer INSTANCE = new STBContainer();
	}
	
	
	public static STBContainer instance(){
		// Bug appears here if one initatate all other singeltons since they as well asks for this instance to 
		// add them self as observers(Loop of doom). 
		//The problem of not having a congruent/synced observers can be fixed by overriding addObserver, as done
		//Raz
		return SingletonHolder.INSTANCE;	
	}
	private STBContainer(){
	}
	
	//This i overridden due to the problem with instances initiation at different times
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		if(getSTB() != null){
			observer.update(this, null);
		}
	}
	
	public STB getSTB(){
		return stb;
	}
	
	public void setSTB(STB stb){
		if(this.stb != null && this.stb.equals(stb)) return;
		
		this.stb = stb;
		setChanged();
		notifyObservers();
	}
	
}
