package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;

/**
 * Class that holds the list of STBs, will probably not be used once the databse
 * is implemented.
 * @author Marcus Widegren, Christian Vestman
 */
public class SelectSTBList {
	private Vector<STB> theList = new Vector<STB>();
	private static SelectSTBList theInstance;

	private SelectSTBList() {
	}

	public static SelectSTBList instance() {
		if (theInstance == null) {
			theInstance = new SelectSTBList();
		}
		return theInstance;
	}

	public Vector<STB> getList() {
		return theList;
	}
}
