package se.z_app.zmote.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ListTextViewElement extends TextView {
	private int theIndex;
	
	/* Default constructors */
	public ListTextViewElement(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle);}
	public ListTextViewElement(Context context, AttributeSet attrs) { super(context, attrs);}
	public ListTextViewElement(Context context) { super(context);}
	
	public void setIndex(int indexIn) {
		theIndex = indexIn;
	}
	
	public int getIndex(){
		return theIndex;
	}

	
	
}
