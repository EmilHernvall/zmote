package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

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
		STB newSTB = new STB();
		newSTB.setBoxName("Kitchen");
		STB newSTB2 = new STB();
		newSTB2.setBoxName("Living room");
		STB newSTB3 = new STB();
		newSTB3.setBoxName("Living room");
		STB newSTB4 = new STB();
		newSTB4.setBoxName("TV Room");
		STB newSTB5 = new STB();
		newSTB5.setBoxName("Bomb shelter");
		theList.add(newSTB);
		theList.add(newSTB2);
		theList.add(newSTB3);
		theList.add(newSTB4);
		theList.add(newSTB5);
		theAdapter = new STBAdapter(theActivity, theList);
		this.getLayoutParams().height = Math.max(theList.size(),3) * 75;
		this.setAdapter(theAdapter); 
	}

}
