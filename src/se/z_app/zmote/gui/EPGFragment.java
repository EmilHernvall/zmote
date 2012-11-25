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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

/**
 * 
 * The fragment that handles displaying the EPG View
 * 
 * @author Thed Mannerlof, Ralf Nilsson, Francisco Valladares, Maria Platero
 *
 */
public class EPGFragment extends Fragment{
	public static Program eventProgram = null;
	private Channel temp;
	private EPG epg;
	private RelativeLayout view;
	private ScrollView scroll_view;
	private MainTabActivity main;
	private LinearLayout i_layout;
	private LinearLayout p_layout;
	private LinearLayout vt_scroll;
	private HorizontalScrollView hz_scroll;
	private LinearLayout timebar_hz_scroll;
	private HorizontalScrollView hz_scroll_time;
	private int height_of_rows = 80;
	private int number_of_channels = 0;
	private int height=80;
	private int width=80;
	private Program program_temp;
	private Date start;
	private Date end;
	private int screen_width = 0;
	private int schedule_lenght_in_hours = 48;
	
	private OnTouchListener toutch;
	private int currentX = -1, currentY = -1;

	public EPGFragment(MainTabActivity main){
		this.main = main;
	}
    
	
    @SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		view = (RelativeLayout) inflater.inflate(R.layout.fragment_epg, null);

		scroll_view = (ScrollView)view.findViewById(R.id.scroll_parent);
		i_layout = (LinearLayout)view.findViewById(R.id.channel_icons);
		i_layout.setBackgroundColor(0x66000000);
		vt_scroll = (LinearLayout)view.findViewById(R.id.channel_programs);
		hz_scroll = (HorizontalScrollView)view.findViewById(R.id.hz_scroll);
		hz_scroll_time = (HorizontalScrollView)view.findViewById(R.id.hz_timeline_parent);
		timebar_hz_scroll = (LinearLayout)view.findViewById(R.id.timebar_hz_scroll);
			
		//2D Scrolling, TODO: Fling needs to be implemented
		toutch = new View.OnTouchListener() {
			long startTime = System.currentTimeMillis();

			int firstX = 0;
			int firstY = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				synchronized (toutch) {
					
					switch (event.getAction()) {
	
			        case MotionEvent.ACTION_MOVE: {
			            if(currentX == -1 || currentY == -1){
			            	currentX = (int) event.getRawX();
				            currentY = (int) event.getRawY();
				            firstX = currentX;
				            firstY = currentY;
				            startTime = System.currentTimeMillis();
			            }
			        	
			        	int x2 = (int) event.getRawX();
			            int y2 = (int) event.getRawY();
			            
			            
			            scroll_view.scrollBy(currentX - x2 , currentY - y2);
			            hz_scroll.scrollBy(currentX - x2 , currentY - y2);
			            hz_scroll_time.scrollBy(currentX - x2, 0);	// Synchronization with timebar
			            currentX = x2;
			            currentY = y2;
			            break;
			        }   
			        case MotionEvent.ACTION_UP: {
			        	
			        	long time = System.currentTimeMillis()-startTime;
			        	if(time/20==0){
			        		break;
			        	}
			        	int vx = 40*(int)((currentX - firstX)/(time/20));
						int vy = 40*(int)((currentY - firstY)/(time/20));
						System.out.println("vx: " + vx);
						System.out.println("vy: " + vy);
			        	currentX = -1;
			            currentY = -1;
			        	
			        	if(time < 300){
			        		hz_scroll_time.fling((int)-vx);		// Timebar synch
			        		hz_scroll.fling((int)-vx);
							scroll_view.fling((int)-vy);
			        	}
			        		
			        	break;
			            
			        }
			        }
					
			        return true;
				}
			}
		};
			
		hz_scroll.setOnTouchListener(toutch);
		scroll_view.setOnTouchListener(toutch);
		
		// Get the size of the screen in pixels
		screen_width = getResources().getDisplayMetrics().widthPixels;
		
		new AsyncDataLoader().execute();

		return view;
	}

    @Override
    public void onResume(){
    	
    	if(eventProgram != null){
    		// Load channel information view and focus on the program
    		main.showChannelInformation(eventProgram);
    		eventProgram = null;	// And reset variable
    	}
    	super.onResume();
    }
    
    /**
     * Sets the listener for the fliping button
     */
    public void setFlipButton(){
    	
    	ImageView flipButton = (ImageView) view.findViewById(R.id.flip_button);
    	flipButton.setVisibility(View.VISIBLE);
    	flipButton.setClickable(true);
    	
    	flipButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(view.getContext(), EpgHorizontalActivity.class);
				EPGFragment.this.startActivity(intent);
			}
			
		});
	
    }
    
    
    /**
     * Sets the timeBar in 30min intervals starting from the hour passed by "start"
     * @param start		Starting time for the time bar
     */
    public void setProgramTimeBar(){
    	
    	Date start_tmp = start;
    	LinearLayout program_timebar = new LinearLayout(view.getContext());
    	LinearLayout.LayoutParams pt_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,30);
    	program_timebar.setOrientation(0);
    	
    	for(int i=0; i<(schedule_lenght_in_hours*2); ++i){
			
			TextView time = new TextView(view.getContext());
			time.setTextColor(0xFFFF8000);
			time.setTypeface(null, Typeface.BOLD);
			time.setText(new SimpleDateFormat("HH:mm").format(start_tmp) );
			time.setWidth(screen_width/2);
			time.setHeight(30);
			program_timebar.addView(time);
			
    		// Adding 1 hour
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(start_tmp);
		    calendar.add(Calendar.MINUTE, 30);
		    start_tmp = calendar.getTime();
    	}
    	
    	timebar_hz_scroll.setBackgroundColor(0xAA000000);	// Transparent background
    	timebar_hz_scroll.addView(program_timebar, pt_params);
    }
    
    /**
     * Sets the line that represent the current time
     */
    public void setNowLine(){
    			
    	Date now = new Date(System.currentTimeMillis());
    	long difference = now.getTime() - start.getTime();
    	int distance = (int)(difference/60000)*(screen_width/60);
    	
    	// We just change the margin of the line according to the current time
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(2,height_of_rows*number_of_channels);
    	params.setMargins(distance+110, 0, 0, 0);	// TODO: Needed some tune here
    	LinearLayout line = (LinearLayout)view.findViewById(R.id.now_line);
    	line.setVisibility(LinearLayout.VISIBLE);
    	line.setLayoutParams(params);
    	//line.invalidate();	// Not sure if needed
    	
    	// Now label
    	RelativeLayout.LayoutParams text_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	text_params.setMargins(distance+110, 0, 0, 0);
    	TextView now_text = (TextView)view.findViewById(R.id.now_text);
    	now_text.setVisibility(TextView.VISIBLE);
    	now_text.setLayoutParams(text_params);
    	now_text.setTypeface(null, Typeface.BOLD);
    	now_text.setBackgroundColor(0xBB000000);
    	//now_text.invalidate();	//Not sure if needed
    	
    	// Center the screen on the now line
    	hz_scroll.scrollBy(distance, 0);
    	hz_scroll_time.scrollBy(distance, 0);

    }
    /**
     * Gets the time of the earlier program of the epg
     * @author Francisco Valladares
     */
    void getStartTime(){
    	
    	Calendar cal = Calendar.getInstance(); // creates calendar
    	start = null;
    	end = null;
    	Date temp = null;
    	// We will check the start time of the first program of every channel
    	// and get the starting hour of the earlier one
    	for(Channel channel: epg){
    		
    		// Check for the latest and earlier program
    		for(Program prog: channel){
    			
    			cal.setTime(prog.getStart()); // sets calendar time/date
    		    cal.add(Calendar.SECOND, prog.getDuration()); // adds one hour
    		    temp = cal.getTime();

    		    if(end == null)
    				end = temp;
    			else if(end.compareTo(temp) < 0){
	    		    end = temp;
    			}
    			
    			if(start == null)
    				start = prog.getStart();
    			else if(start.compareTo(prog.getStart()) > 0)
    				start = prog.getStart();
    		}
    	}
    	
    	// Get the length of the schedule in hours
    	long duration = 0;
    	if(start != null && end != null)
    		duration = end.getTime() - start.getTime();		// Duration in milliseconds
    	schedule_lenght_in_hours = (int) (duration / (60*60*1000));

    }
    
    /**
     * Fetches the channels from the EPG and adds the channels and programs to the layout
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
		setFlipButton();
	}

	/**
	 * Adds a new button with channel icon to the layout
	 * @param ch channel which the icon belongs to
	 */
	void addIconToLayout(Channel ch){
		
		ImageButton new_btn = new ImageButton(view.getContext());
		new_btn.setPadding(0, 0, 0, 0);
		new_btn.setId(ch.getNr()+200);	// ID of the button: ChannelNr+200
		new_btn.setImageBitmap(getResizedBitmap(ch.getIcon(),height,width));
		new_btn.setBackgroundResource(0);	// Set the background transparent
		new_btn.setClickable(true);
		temp = ch;
		
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

	/**
	 * Adding programs to the layout
	 * @param pg the program to add
	 * @param n_program number of the program to add
	 */
	void addProgramToLayout(Program pg, int n_program){
		
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
		textParams.setMargins(1,1,1,1);
		long difference = 0;
		LinearLayout.LayoutParams params = null;
		float length = 0;
		
		if(n_program == 0){	// Only for the first program
			
			if( pg.getStart().compareTo(start) > 0){	
				difference = pg.getStart().getTime() - start.getTime();	
			}else if( pg.getStart().compareTo(start) == 0){
				difference = 0;
			}
			
			length = (int)(difference/60000)*(screen_width/60);
			
			params= new LinearLayout.LayoutParams((int)length, height_of_rows);
			params.setMargins(0, 0, 0, 0);
			LinearLayout starting_space = new LinearLayout(view.getContext());
			p_layout.addView(starting_space, params);

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
				
				main.showChannelInformation(p);
				
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
	 * Method for changing the dimensions of a bitmap picture
	 * @param bm the bitmap picture to be re-sized
	 * @param newHeight the height of the re-sized bitmap picture
	 * @param newWidth the width of the re-sized bitmap picture
	 * @return resizedBitmap the re-sized bitmap picture
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	
		if(bm == null)
			return null;
		int width = bm.getWidth();
		int height = bm.getHeight();
		
		float scaleHeight = ((float) newHeight) / height;
		float scaleWidth =scaleHeight;// ((float) newWidth) / width;
		
		Matrix matrix = new Matrix();		// Create a matrix for the manipulation
		matrix.postScale(scaleWidth, scaleHeight);	// Resize the bit map
		
		// Recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		
		return resizedBitmap;	
	}


	/**
	 * Loads the information asynchronously
	 * @author Rasmus Holm, Francisco Valladares
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
			mainEPG();
		}	

	}

}
