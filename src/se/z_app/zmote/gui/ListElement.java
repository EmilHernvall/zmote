package se.z_app.zmote.gui;



import se.z_app.stb.STB;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

public class ListElement extends ImageView {
	private STB theSTB;
	
	/* Default constructors */
	public ListElement(Context context) { super(context);}	
	public ListElement(Context context, AttributeSet attr) { super(context, attr);}
	public ListElement(Context context, AttributeSet attr, int n) { super(context, attr, n);}
	
	
	public void setSTB(STB theSTBIn) {
		theSTB = theSTBIn;
	}
	
	public STB getSTB() {
		return theSTB;
	}

}
