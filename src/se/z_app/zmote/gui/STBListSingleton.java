package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;

/** Class that holds the list of STBs, will probably not be used
 *   once the databse is implemented.
 */
public class STBListSingleton {
	private Vector<STB> theList = new Vector<STB>();
	private static STBListSingleton theInstance;
	
	private STBListSingleton(){}
	
	public static STBListSingleton instance() {
		if(theInstance == null) 
		{
			theInstance = new STBListSingleton();
		}
		return theInstance;
	}
	
	public Vector<STB>getList() {
		return theList;
	}
}
