package se.z_app.zmote.gui;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v4.app.Fragment;

//This should be a fragment not a Activity
public class EPGFragment extends Fragment{
	private String temp;
	private EPGQuery q = new EPGQuery();
    private EPG epg = q.getEPG();
	private View v;
	private MainTabActivity main;
	private LinearLayout i_layout;
	private int height=80;
	private int width=80;
	
	public EPGFragment(MainTabActivity main){
		this.main = main;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		
		v = inflater.inflate(R.layout.fragment_epg, null);
		
		i_layout = (LinearLayout)v.findViewById(R.id.channel_icons);
		
		
		
		mainEPG();
	    
	        
	 
	
		return v;
	}

    
    
    
    


void mainEPG(){
	
	for (Channel channel : epg) {
    	addIconToLayout(channel);
    	for (Program program : channel) {
			addProgramsToLayout(program);	
			}
	
     }	
}

void addIconToLayout(Channel ch){
	
	ImageButton new_btn = new ImageButton(v.getContext());
	new_btn.setId(ch.getNr()+200);	// ID of the button: ChannelNr+200
	new_btn.setImageBitmap(getResizedBitmap(ch.getIcon(),height,width));
	new_btn.setBackgroundResource(0);	// Set the background transparent
	new_btn.setClickable(true);
	
	temp = ch.getUrl();
	
	
	
	//TODO: 
	//If you click on an icon you are supposed to change channel
	new_btn.setOnClickListener(new View.OnClickListener() {
		String url = temp;
		@Override
		public void onClick(View v) {
			
		RemoteControl.instance().launch(url);
		main.vibrate();
		}
		});
	
	i_layout.addView(new_btn);
}

void addProgramsToLayout(Program pg){
	

	
	

}

//Changing the icons size
public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

int width = bm.getWidth();

int height = bm.getHeight();

float scaleWidth = ((float) newWidth) / width;

float scaleHeight = ((float) newHeight) / height;

// create a matrix for the manipulation

Matrix matrix = new Matrix();

// resize the bit map

matrix.postScale(scaleWidth, scaleHeight);

// recreate the new Bitmap

Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

return resizedBitmap;

}



}