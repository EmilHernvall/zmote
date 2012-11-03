package se.z_app.zmote.gui;
import se.z_app.stb.STB;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An ImageElement for the STB list, contains
 * references to an STB and a TextView
 * 
 * @author Marcus Widegren, Christian Westman
 *
 */
public class SelectSTBImageElement extends ImageView {
	private STB theSTB;
	private TextView theTextView;
	private int theIndex;
	
	/* Default constructors */
	public SelectSTBImageElement(Context context) {
		super(context);
	}
	
	public SelectSTBImageElement(Context context, AttributeSet attr) { 
		super(context, attr);
	}
	
	public SelectSTBImageElement(Context context, AttributeSet attr, int n) { 
		super(context, attr, n);
	}
	
	/**
	 * Set which TextView should be associated with this
	 * 	ImageElement
	 * @param textViewIn
	 */
	public void setTextView(TextView textViewIn) {
		theTextView = textViewIn;
	}
	
	/**
	 * Get the TextView associated with this ImageElement
	 * @return the associated TextView
	 */
	public TextView getTextView() {
		return theTextView;
	}
	
	/**
	 * Set which STB should be associated with this
	 *  ImageElement
	 * @param theSTBIn
	 */
	public void setSTB(STB theSTBIn) {
		theSTB = theSTBIn;
	}
	
	/**
	 * Set which index of the list this Element has
	 * @param theIndexIn
	 */
	public void setIndex(int theIndexIn) {
		theIndex = theIndexIn;
	}
	
	/**
	 * Get the associated index
	 * @param theIndexIn
	 */
	public int getIndex() {
		return theIndex;
	}
	
	/**
	 * Get the associated STB
	 * @return the associated STB
	 */
	public STB getSTB() {
		return theSTB;
	}

}
