package se.z_app.zmote.gui;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Thed Mannerlof, Ralf Nilsson, Francisco Valladares, Maria Platero
 *
 */
public class EPGFragment extends Fragment{
	private Channel temp;
	private EPG epg;
	private View view;
	private MainTabActivity main;
	private LinearLayout i_layout;
	private LinearLayout p_layout;
	private LinearLayout vt_scroll;
	private int height_of_rows = 80;
	private int number_of_channels = 0;
	private int height=80;
	private int width=80;
	private Program program_temp;
	private int start_hour = 24;
	private int start_minutes = 0;
	private int screen_width = 0;
	private OrientationListener orientationListener = null;
	private int changes = 0;
	private int orientation_var = 0;	// Horiz: 0 , Vertical: 1
	private boolean epg_loaded = false;

	public EPGFragment(MainTabActivity main){
		this.main = main;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		view = inflater.inflate(R.layout.fragment_epg, null);
		i_layout = (LinearLayout)view.findViewById(R.id.channel_icons);
		i_layout.setBackgroundColor(0x66000000);
		vt_scroll = (LinearLayout)view.findViewById(R.id.channel_programs);
		
		// Get the size of the screen in pixels
		screen_width = getResources().getDisplayMetrics().widthPixels;
		
        orientationListener = new OrientationListener(view.getContext()) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				// TODO Auto-generated method stub
				if(orientation != ORIENTATION_UNKNOWN && changes != 0 && epg_loaded){
					Toast.makeText(view.getContext(), "changeeeddd", Toast.LENGTH_SHORT).show();
					if(orientation_var == 1){
						Intent intent = new Intent(view.getContext(), EpgHorizontalActivity.class);
						EPGFragment.this.startActivity(intent);
						orientation_var = 0;
					}else if(orientation_var == 0){
						// Go back to the fragment in some way
						Toast.makeText(view.getContext(), "Going back", Toast.LENGTH_SHORT).show();
					}
				}
				changes++;
			}
		};
		orientationListener.enable();
		
		new AsyncDataLoader().execute();
		
		return view;
	}

    @Override
    public void onResume() {
    	
    	//getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    	super.onResume();
    }
    
    @Override
    public void onPause() {
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // set the activity back to //whatever it needs to be when going back.
        super.onPause();
    }


    
    /**
     * Sets the timeBar in 30min intervals starting from the hour passed by "start"
     * @param start		Starting time for the time bar
     */
    public void setProgramTimeBar(){
    	
    	Date start = new Date(2012,10,10,start_hour,0);
    	LinearLayout program_timebar = new LinearLayout(view.getContext());
    	LinearLayout.LayoutParams pt_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,30);
    	program_timebar.setOrientation(0);
    	
		//Date now = new Date(System.currentTimeMillis());
    	
    	for(int i=0; i<48; ++i){
			
			TextView time = new TextView(view.getContext());
			time.setTextColor(0xFFFF8000);
			time.setTypeface(null, Typeface.BOLD);
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
     * Sets the line that represent the current time
     */
    public void setNowLine(){
    
    	Date now = new Date(System.currentTimeMillis());
    	int distance = (now.getHours()-start_hour)*screen_width + (now.getMinutes()*screen_width)/60;
    	
    	// We just change the margin of the line according to the current time
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,height_of_rows*number_of_channels);
    	params.setMargins(distance, 30, 0, 0);
    	LinearLayout line = (LinearLayout)view.findViewById(R.id.now_line);
    	line.setVisibility(LinearLayout.VISIBLE);
    	line.setLayoutParams(params);
    	//line.invalidate();	// Not sure if needed
    	
    	// Next lines are the fast way to focus on the current time in the EPG
    	line.setFocusableInTouchMode(true);		// Get the screen to the current time schedule
    	line.requestFocus();
    	
    	// Now label
    	RelativeLayout.LayoutParams text_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	text_params.setMargins(distance-15, 0, 0, 0);
    	TextView now_text = (TextView)view.findViewById(R.id.now_text);
    	now_text.setVisibility(TextView.VISIBLE);
    	now_text.setLayoutParams(text_params);
    	now_text.setTypeface(null, Typeface.BOLD);
    	now_text.setBackgroundColor(0xBB000000);
    	//now_text.invalidate();	//Not sure if needed

    }
    /**
     * Gets the time of the earlier program of the epg
     * @author Francisco Valladares
     */
    void getStartTime(){
    	// We will check the start time of the first program of every channel
    	// and get the starting hour of the earlier one
    	for(Channel channel: epg){
    		for(Program prog: channel){
    			int hours_temp = prog.getStart().getHours();
    			int minutes_temp = prog.getStart().getMinutes();
    			if( hours_temp < start_hour){
    				start_hour = hours_temp;
    				start_minutes = minutes_temp;
    			}else if(minutes_temp < start_minutes){
    				start_minutes = minutes_temp;
    			}
    			break;
    		}
    	}
    }
    
    /**
     * Fetch the channels
     */
	void mainEPG(){
		
		getStartTime();			// Decide the start time of the schedule
		setProgramTimeBar();	// Add the time bar
		number_of_channels = 0;	// Initialization 
		
		// Then, we add the channel information
		for (Channel channel : epg) {

			int programs = 0;
			
			addIconToLayout(channel);
			p_layout = new LinearLayout(view.getContext());
			p_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for (Program program : channel) {
				addProgramToLayout(program, programs);
				programs++;
			}
			// Add space separation if there is no programs for this channel
			if(programs == 0){
				addSpaceBetweenChannels();
			}
			
			vt_scroll.addView(p_layout);
			number_of_channels++;
	     }
		setNowLine();
	}

	/**
	 * Adding icon to the layout
	 * @param ch
	 */
	void addIconToLayout(Channel ch){
		
		ImageButton new_btn = new ImageButton(view.getContext());
		new_btn.setPadding(0, 0, 0, 0);
		new_btn.setId(ch.getNr()+200);	// ID of the button: ChannelNr+200
		new_btn.setImageBitmap(getResizedBitmap(ch.getIcon(),height,width));
		new_btn.setBackgroundResource(0);	// Set the background transparent
		new_btn.setClickable(true);
		temp = ch;
		
		//TODO: If you click on an icon you are supposed to change channel
		new_btn.setOnClickListener(new View.OnClickListener() {
			Channel tempChannel = temp;
			@Override
			public void onClick(View v) {
				
				RemoteControl.instance().launch(tempChannel);
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
	void addProgramToLayout(Program pg, int n_program){
		
		Date start = new Date(2012,10,10,start_hour,0);	// Starting hour
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
		textParams.setMargins(1,1,1,1);
		int hours_of_difference = 0;
		int minutes_of_difference = 0;
		LinearLayout.LayoutParams params = null;
		float length = 0;
		
		if(n_program == 0){	// Only for the first program
			
			if( pg.getStart().getHours() > start.getHours()){
				
					hours_of_difference = pg.getStart().getHours() - start.getHours();
					minutes_of_difference = 60 - pg.getStart().getMinutes();
					
			}else if( pg.getStart().getHours() == start.getHours()){
				if(pg.getStart().getMinutes() > start.getMinutes()){
					minutes_of_difference = pg.getStart().getMinutes();
				}
			}
			
			length = hours_of_difference*screen_width + minutes_of_difference*screen_width/60;
			params= new LinearLayout.LayoutParams((int)length, height_of_rows);
			params.setMargins(0, 0, 0, 0);
			LinearLayout starting_space = new LinearLayout(view.getContext());
			p_layout.addView(starting_space, params);
			/*System.out.println(pg.getName()+" empieza:"+pg.getStart().getHours()+
					":"+pg.getStart().getMinutes()+" y dura:"+pg.getDuration()/60+"min");
			System.out.println("Diferencia con hora de comienzo:"+hours_of_difference+":"+minutes_of_difference);
			*/
		}
		
		length = pg.getDuration()*screen_width/3600;
		params= new LinearLayout.LayoutParams((int)length, height_of_rows);
		params.setMargins(0, 0, 0, 0);

		LinearLayout container = new LinearLayout(view.getContext());
		container.setBackgroundColor(0xFF777777);
		
		TextView text = new TextView(view.getContext());
		text.setText(new SimpleDateFormat("HH:mm").format(pg.getStart())+" "+pg.getName());
		text.setLines(3);
		text.setPadding(2, 1, 2, 1);
		text.setClickable(true);
		text.setTextColor(0xFFFFFFFF);
		text.setBackgroundColor(0xFF222222);
		text.setGravity(Gravity.CENTER_VERTICAL);
		
		program_temp = pg;
		text.setOnClickListener(new View.OnClickListener() {
			
			Program p = program_temp;
			/* 
			 * When a program is clicked, the channel information view is loaded */
			@Override
			public void onClick(View v) {
			
				Fragment fragment = new ChannelInformationFragment(main, p);
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
		LinearLayout container = new LinearLayout(view.getContext());
		p_layout.addView(container, params);
	}
	
	/**
	 * Changes the icons size
	 * @param bm
	 * @param newHeight
	 * @param newWidth
	 * @return resizedBitmap
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	
		if(bm == null)
			return null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		
		Matrix matrix = new Matrix();		// Create a matrix for the manipulation
		matrix.postScale(scaleWidth, scaleHeight);	// Resize the bit map
		
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
			view.findViewById(R.id.progressEPGView).setVisibility(View.INVISIBLE);
			epg_loaded = true;
			mainEPG();
		}	

	}

}
