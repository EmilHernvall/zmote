package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import android.widget.ListView;

/**
 * The STB list generator
 * 
 * @author Marcus Widegren, Christian Vestman
 * 
 * 
 */
public class SelectSTBListView extends ListView {
	Vector<STB> theList = SelectSTBList.instance().getList();
	SelectSTBAdapter theAdapter;
	String filePath = null;

	/* Default constructors */
	public SelectSTBListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SelectSTBListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectSTBListView(Context context) {
		super(context);
	}

	/**
	 * notify�s the adapter when the list is changed
	 */
	public void notifyAdapter() {
		if (theAdapter != null)
			theAdapter.notifyDataSetChanged();
	}

	/**
	 * sets the lenght of the STB list
	 * 
	 * @param theActivity
	 * @param listIn
	 */
	public void setList(Activity theActivity, STB[] listIn, String filePath) {
		theList.clear();
		for (int i = 0; i < listIn.length; i++) {
			theList.add(listIn[i]);
		}

		theAdapter = new SelectSTBAdapter(theActivity, theList, filePath);
		this.setAdapter(theAdapter);
	}
	
	

}
