package se.z_app.zmote.gui;

import javax.xml.datatype.Duration;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v4.app.Fragment;

//This should be a fragment not a Activity
public class EPGFragment extends Fragment{
	private String temp;
	private EPG epg;
	private View v;
	private MainTabActivity main;
	private LinearLayout i_layout;

	private LinearLayout p_layout;
	private GridLayout g_layout;

	private int counter=0;
	private HorizontalScrollView hz_scroll;
	private LinearLayout vt_scroll;
	

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
		
		vt_scroll = (LinearLayout)v.findViewById(R.id.channel_programs);
		
		new AsyncDataLoader().execute();

	
		return v;
	}

    
    
    
    


void mainEPG(){
	
	for (Channel channel : epg) {
		//counter+=1;	
		//g_layout.setRowCount(counter);
		addIconToLayout(channel);
		p_layout = new LinearLayout(v.getContext());
		/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
		p_layout.setLayoutParams(params);*/
		p_layout.setOrientation(LinearLayout.HORIZONTAL);
		for (Program program : channel) {
			addProgramsToLayout(program);	
		}
		
		vt_scroll.addView(p_layout);
    	
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

int i =0;

//TODO:
//Make sure it's no space between buttons and they are aligned properly
void addProgramsToLayout(Program pg){
	Button new_btn2 = new Button(v.getContext());
	
	new_btn2.setText(pg.getName()+" "+pg.getStart());
	new_btn2.setClickable(true);
	//new_btn2.setWidth(pg.getDuration()/30);
	//new_btn2.setHeight(80);
	
	
	LinearLayout.LayoutParams rel_button1 = new LinearLayout.LayoutParams(pg.getDuration()/10, 80);
	rel_button1.setMargins(0, 0, 0, 0);
	//rel_button1.height = 80;
	//rel_button1.width = pg.getDuration()/10;
	
	new_btn2.setLayoutParams(rel_button1);
	
	
	new_btn2.setOnClickListener(new View.OnClickListener() {
	
		
	@Override
		public void onClick(View v) {
		
		}
		});

	p_layout.addView(new_btn2, rel_button1);

	
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



private class AsyncDataLoader extends AsyncTask<Integer, Integer, EPG>{

	@Override
	protected EPG doInBackground(Integer... params) {
		EPGQuery query = new EPGQuery();
		return query.getEPG();
	}
	
	@Override
	protected void onPostExecute(EPG epgTemp) {
		epg = epgTemp;
		v.findViewById(R.id.progressEPGView).setVisibility(View.INVISIBLE);
		
		mainEPG();
	}	

}

}
