package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The STB list generate
 *
 */
public class STBListView extends ListView{
	Vector<STB> theList;
	ArrayAdapter<STB> theAdapter;
	
	public STBListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public STBListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public STBListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setList(Activity theActivity, STB[] listIn)
	{
		theList = new Vector<STB>();
		for(int i = 0; i < listIn.length; i ++)
		{
			theList.add(listIn[i]);
		}
		//ArrayAdapter<STB> adapter = new ArrayAdapter<STB>(theActivity,
		//		R.layout.list_row, theList);
		STBAdapter adapter = new STBAdapter(theActivity, theList);
		this.setAdapter(adapter); 
	}

}
