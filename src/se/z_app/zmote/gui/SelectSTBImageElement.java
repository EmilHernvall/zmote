package se.z_app.zmote.gui;



import se.z_app.stb.STB;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectSTBImageElement extends ImageView {
	private STB theSTB;
	private TextView theTextView;
	private int theIndex;
	
	/* Default constructors */
	public SelectSTBImageElement(Context context) { super(context);}	
	public SelectSTBImageElement(Context context, AttributeSet attr) { super(context, attr);}
	public SelectSTBImageElement(Context context, AttributeSet attr, int n) { super(context, attr, n);}
	
	public void setTextView(TextView textViewIn) {
		theTextView = textViewIn;
	}
	
	public TextView getTextView() {
		return theTextView;
	}
	
	public void setSTB(STB theSTBIn) {
		theSTB = theSTBIn;
	}
	
	public void setIndex(int theIndexIn) {
		theIndex = theIndexIn;
	}
	
	public int getIndex() {
		return theIndex;
	}
	
	public STB getSTB() {
		return theSTB;
	}

}
