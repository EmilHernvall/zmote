package se.z_app.zmote.gui;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.app.FragmentTransaction;
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
	private int height_of_rows = 80;
	private int height=80;
	private int width=80;
	private Program program_temp;
	
	private int screen_width = 0;

	public EPGFragment(){
		
	}
	
	public EPGFragment(MainTabActivity main){
		this.main = main;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_epg, null);
		i_layout = (LinearLayout)v.findViewById(R.id.channel_icons);
		vt_scroll = (LinearLayout)v.findViewById(R.id.channel_programs);
		
		// Get the size of the screen in pixels
		screen_width = getResources().getDisplayMetrics().widthPixels;
		
		new AsyncDataLoader().execute();
		
		return v;
	}

    /**
     * Sets the timeBar in 30min intervals starting from the hour passed by "start"
     * @param start		Starting time for the time bar
     */
    public void setProgramTimeBar(Date start){
    	
    	LinearLayout program_timebar = new LinearLayout(v.getContext());
    	LinearLayout.LayoutParams pt_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,30);
    	program_timebar.setOrientation(0);
    	
		//Date now = new Date(System.currentTimeMillis());
    	
    	for(int i=0; i<48; ++i){
			
			TextView time = new TextView(v.getContext());
			time.setTextColor(0xFFFFFFFF);
			time.setText(new SimpleDateFormat("HH:mm").format(start) );
			time.setWidth(screen_width/2);
			time.setHeight(30);
			program_timebar.addView(time);
			
    		// Adding 1 hour
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(start);
		    calendar.add(Calendar.MINUTE, 30);
		    start = calendar.getTime();
    	}
    	
    	vt_scroll.addView(program_timebar, pt_params);
    	
    }
    
    /**
     * Fetch the channels
     */
	void mainEPG(){
		
		// First of all, we add the time bar
		Date start = new Date(2012,10,10,12,0);
		setProgramTimeBar(start);
		
		// Then, we add the channel information
		for (Channel channel : epg) {


			int programs = 0;
			
			addIconToLayout(channel);
			p_layout = new LinearLayout(v.getContext());
			p_layout.setOrientation(LinearLayout.HORIZONTAL);
			for (Program program : channel) {
				addProgramToLayout(program);
				programs++;
			}
			// Add space separation if there is no programs for this channel
			if(programs == 0){
				addSpaceBetweenChannels();
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
		
		Date start = new Date(2012,10,10,12,0);	// Starting hour (12:00)
		
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
		textParams.setMargins(1,1,1,1);
		
		int hours_of_difference = 0;
		int minutes_of_difference = 0;
		LinearLayout.LayoutParams params = null;
		long end = pg.getStart().getTime() + pg.getDuration()*1000;
		if( pg.getStart().getHours() < start.getHours()){
			
				hours_of_difference = start.getHours() - pg.getStart().getHours() -1;
				minutes_of_difference = 60 - pg.getStart().getMinutes();
				
		}else if( pg.getStart().getHours() == start.getHours()){
			if(pg.getStart().getMinutes() < start.getMinutes()){
				minutes_of_difference = 60 - pg.getStart().getMinutes();
			}
		}
		/*System.out.println(pg.getName()+" empieza:"+pg.getStart().getHours()+
				":"+pg.getStart().getMinutes()+" y dura:"+pg.getDuration()/60+"min");
		System.out.println("Diferencia con hora de comienzo:"+hours_of_difference+":"+minutes_of_difference);
		*/
		float length = pg.getDuration()*screen_width/3600 - hours_of_difference*screen_width - minutes_of_difference*screen_width/60;
		params= new LinearLayout.LayoutParams((int)length, height_of_rows);
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
		
		program_temp = pg;
		text.setOnClickListener(new View.OnClickListener() {
			
			Program p = program_temp;
			/*
			 * TODO: Send parameters to load the specific clicked channel/program
			 * 
			 * When a program is clicked, the channel information view is loaded*/
			@Override
			public void onClick(View v) {
			
				Fragment fragment = new ChannelInformationFragment(p);
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				fragmentTransaction.replace(R.id.container, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			
		});
		
		container.addView(text, textParams);
		p_layout.addView(container, params);
		
	}
	
	/**
	 * Adds a space in the channel row with the same height of the rest of the rows
	 */
	public void addSpaceBetweenChannels(){
		
		LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height_of_rows);
		params.setMargins(0, 0, 0, 0);
		
		LinearLayout container = new LinearLayout(v.getContext());
		
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
