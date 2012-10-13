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
	Vector<STB> theList = STBListSingleton.instance().getList();
	STBAdapter theAdapter;
	
	/* Default constructors */
	public STBListView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle);}
	public STBListView(Context context, AttributeSet attrs) {super(context, attrs);}
	public STBListView(Context context) {super(context);}
	
    public void notifyAdapter() {
    	if(theAdapter != null)
    		theAdapter.notifyDataSetChanged();
    }
	
	public void setList(Activity theActivity, STB[] listIn)
	{
		theList.clear();
		for(int i = 0; i < listIn.length; i ++)
		{
			theList.add(listIn[i]);
		}
		
		theAdapter = new STBAdapter(theActivity, theList);
		this.setAdapter(theAdapter); 
	}

}
