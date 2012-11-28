package se.z_app.stb.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


import se.z_app.stb.STB;

/**
 * Class that holds the STB for other classes to access.
 * 
 * @author Rasmus Holm, Linus Back
 */
public class STBContainer extends Observable implements Iterable<STB>{
	private STB stb;
	private LinkedList<STB> stbs = new LinkedList<STB>();
	
	/**
	 * Creates and holds the singleton instance of the STB container
	 * @author Rasmus Holm & Linus Back
	 */
	private static class SingletonHolder { 
        public static final STBContainer INSTANCE = new STBContainer();
	}
	
	/**
	 * Request the singleton instance of the STB container
	 * @return The instance of the STB container
	 */
	public static STBContainer instance() {
		return SingletonHolder.INSTANCE;	
	}
	
	/**
	 * Private constructor that creates the single instance of STB container
	 */
	private STBContainer(){
	}
	
	//This is overridden due to the problem with instances initiation at different times
	@Override
	public void addObserver(Observer observer) {
		super.addObserver(observer);
		if(getActiveSTB() != null) {
			observer.update(this, null);
		}
	}
	
	/**
	 * Getter for the currently active STB
	 * @return The active STB 
	 */
	public STB getActiveSTB() {
		return stb;
	}
	
	/**
	 * Setter for the currently active STB
	 * @param stb - The STB to be set as currently active
	 */
	public void setActiveSTB(STB stb) {
		if(this.stb != null && this.stb.equals(stb)) {
			return;
		}		
		addSTB(stb);
		this.stb = stb;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Checks whether the specified STB is the currently active one or not
	 * @param stb - The STB to be checked
	 * @return True if active, otherwise false
	 */
	public boolean isActiveSTB(STB stb) {
		return this.stb.equals(stb);
	}
	
	/**
	 * Adds a STB to the container's list of STBs
	 * @param stb - The STB to be added 
	 * @return True if the STB was added, otherwise false
	 */
	public boolean addSTB(STB stb) {
		for(STB tmpSTB : stbs)
			if(stb.getMAC()!=null && 
			(tmpSTB.getMAC().equals(stb.getMAC())) || tmpSTB.getIP().equals(stb.getIP())) {
				return false;
			}
		stbs.add(stb);
		return true;
	}
	
	/**
	 * Remove the specified STB from the container's list of STBs
	 * @param stb - The STB to be removed
	 * @return True if the STB was removed, otherwise false
	 */
	public boolean removeSTB(STB stb) {
		return stbs.remove(stb);
	}
	
	/**
	 * Checks whether the specified STB is in the container's list of STBs
	 * @param stb - The STB to be checked
	 * @return True if the STB is in the list, otherwise false
	 */
	public boolean containsSTB(STB stb) {
		return stbs.contains(stb);
	}
	
	/**
	 * Returns all STBs in the container's list
	 * @return An iterator containing all STBs
	 */
	@Override
	public Iterator<STB> iterator() {
		return stbs.iterator();
	}
	
	/**
	 * Returns all STBs in the container's list
	 * @return An array containing all STBs
	 */
	public STB[] getSTBs() {
		STB stbToBeReturned[] = new STB[stbs.size()];
		stbs.toArray(stbToBeReturned);
		return stbToBeReturned;
	}
	
	/**
	 * Resets the container, removes the currently active STB and empties the list of STBs
	 */
	public void reset() {
		stbs = new LinkedList<STB>();
		stb = null;
		hasChanged();
		notifyObservers();
	}
	
	
	
}
