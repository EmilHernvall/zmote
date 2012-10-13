package se.z_app.zmote.gui;



import se.z_app.stb.STB;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

public class ListImageElement extends ImageView {
	private STB theSTB;
	private TextView theTextView;
	
	/* Default constructors */
	public ListImageElement(Context context) { super(context);}	
	public ListImageElement(Context context, AttributeSet attr) { super(context, attr);}
	public ListImageElement(Context context, AttributeSet attr, int n) { super(context, attr, n);}
	
	public void setTextView(TextView textViewIn) {
		theTextView = textViewIn;
	}
	
	public TextView getTextView() {
		return theTextView;
	}
	
	public void setSTB(STB theSTBIn) {
		theSTB = theSTBIn;
	}
	
	public STB getSTB() {
		return theSTB;
	}

}
