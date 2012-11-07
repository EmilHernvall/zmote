package se.z_app.zmote.gui;


import java.text.SimpleDateFormat;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Thed&Ralf
 *
 */
public class EPGFragment extends Fragment{
	private String temp;
	private EPG epg;
	private View v;
	private MainTabActivity main;
	private LinearLayout i_layout;
	private LinearLayout p_layout;
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

    /**
     * Fetch the channels
     */
	void mainEPG(){
		
		for (Channel channel : epg) {

			addIconToLayout(channel);
			p_layout = new LinearLayout(v.getContext());
			p_layout.setOrientation(LinearLayout.HORIZONTAL);
			for (Program program : channel) {
				addProgramToLayout(program);	
			}
			
			vt_scroll.addView(p_layout);
	     }	
	}

	/**
	 * 
	 * @param ch
	 */
	void addIconToLayout(Channel ch){
		
		ImageButton new_btn = new ImageButton(v.getContext());
		new_btn.setPadding(0, 0, 0, 0);
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
	
	
	
	//TODO: Make sure it's no space between buttons and they are aligned properly
	/**
	 * Adding programs to the layout
	 * @param pg
	 */
	void addProgramToLayout(Program pg){
		
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
		textParams.setMargins(2,1,2,1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pg.getDuration()/10, 80);
		params.setMargins(0, 0, 0, 0);

		LinearLayout container = new LinearLayout(v.getContext());
		container.setBackgroundColor(0xFF777777);
		
		TextView text = new TextView(v.getContext());
		text.setText(new SimpleDateFormat("HH:mm").format(pg.getStart())+" "+pg.getName());
		text.setLines(2);
		text.setPadding(2, 1, 2, 1);
		text.setClickable(true);
		text.setTextColor(0xFFFFFFFF);
		text.setBackgroundColor(0xFF222222);
		

		text.setOnClickListener(new View.OnClickListener() {
		
			/*
			 * TODO: Send parameters to load the specific clicked channel/program
			 * 
			 * When a program is clicked, the channel information view is loaded*/
			@Override
			public void onClick(View v) {
			
				Fragment fragment = new ChannelInformationFragment();
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.container, fragment);
				fragmentTransaction.commit();
			}
			
		});
		
		container.addView(text, textParams);
		p_layout.addView(container, params);
		
	}
	
	/**
	 * Changes the icons size
	 * @param bm
	 * @param newHeight
	 * @param newWidth
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		
		// Create a matrix for the manipulation
		Matrix matrix = new Matrix();
		
		// Resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		
		// Recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		
		return resizedBitmap;
	
	}


	/**
	 * Loads the information asynchronously
	 * @author 
	 *
	 */
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
