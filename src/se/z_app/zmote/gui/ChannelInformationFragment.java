package se.z_app.zmote.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 *  Channel information view: View with the information of the channel
 *  It is shown when we click on an EPG program
 * */
public class ChannelInformationFragment extends Fragment{
	
    private LinearLayout content_layout;
	private View view_temp;
	private EPG epg;
	private String temp;
	private int i_tmp;
	private EPGQuery query = new EPGQuery();
	private EPG epgFetched;
	private boolean fetched = false;
	private MainTabActivity main;
	private ProgressBar pb;
	private ImageButton fav_but;
	private ImageButton imdb_but;
	private ImageButton social_but;
	
	private Program currentProgram = null;
	private ScrollView channel_content = null;
	private LinearLayout scrollTo = null;
	private Program focusOnThis = null;
	private TextView scrollToFocused = null;
	private LinearLayout ly_temp = null;
	
	/**
	 * Default constructor of ChannelInformationFragment
	 */
	public ChannelInformationFragment(){
		
	}
	
	/**
	 * Default constructor of ChannelInformationFragment
	 * @param program Program in which we want to focus
	 */
	public ChannelInformationFragment(Program program){
		focusOnThis = program;
	}
	
	/**
	 * Default constructor of ChannelInformationFragment
	 * @param main
	 */
	public ChannelInformationFragment(MainTabActivity main){
		this.main = main;
	}
	
	/**
	 * This to do when the instance of ChannelInformationFragment is created
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		view_temp = inflater.inflate(R.layout.fragment_channel_information, null);
		content_layout = (LinearLayout)view_temp.findViewById(R.id.content_ly);
		pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		
		new AsyncDataLoader().execute();
	
    	return view_temp;
    }    
	
	/**
	 * Return if the EPG is already fetched or not
	 */
	public boolean isFetchedTheEPG(){
		return fetched;
	}
	
	/** 
	 * Fetch the EPG from the STB 
	 */
	public void fetchEPG(){
		epgFetched = query.getEPG();
		fetched = true;
	}
	
	/**
	 * Return the EPG after fetching it (just do it one time now)
	 */
	public EPG getFullEPG(){
		if(!fetched)
			fetchEPG();
		return epgFetched;
	}
	
	/**
	 * Add all channels information to the layout
	 */
    public void addAllChannelsInformationToLayout(){

    	for(Channel channel: epg){
    		addChannelItemToLayout(channel);
    	}

    }
    
	/**
	 * Loads the information about the channel to the layout
	 * @param ch
	 */
    public void addChannelItemToLayout(Channel ch){
    
    	// Get the width of the screen
    	int width_screen = getResources().getDisplayMetrics().widthPixels;
    	int icon_size = ch.getIcon().getWidth();
    	
    	// "White box" parameters
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width_screen-40, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(15, 15, 15, 30);
        
        // WrapContent parameters
        LinearLayout.LayoutParams wrapContentParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       
        // Top_info_ly parameters
        // Width is the screen size minus the margin(40) minus the padding(20)
        LinearLayout.LayoutParams topInfoLyParams = new LinearLayout.LayoutParams(width_screen-60-icon_size, LayoutParams.WRAP_CONTENT);
        
        // Separator parameters
        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
        
        // Prepare the "white box" for the program information
    	LinearLayout channel_ly = new LinearLayout(view_temp.getContext()); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(0xFFFFFFFF);
    	channel_ly.setPadding(10, 5, 10, 5);
    	channel_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	channel_ly.setId(ch.getNr()*1000);	// The ID of the channel information box is ChannelNR*1000
    	
    	// The information part of the box will be scrollable
    	channel_content = new ScrollView(view_temp.getContext());
        LinearLayout channel_info_ly = new LinearLayout(view_temp.getContext());
        channel_info_ly.setOrientation(1);
        // Check if we need to set up the parameters in the previous ones
    	
    	// Head of the info (Current program + Channel icon)
    	LinearLayout top_ly = new LinearLayout(view_temp.getContext());
    	top_ly.setOrientation(0);	// Vertical 1; Horizontal 0
    	
    	// Current program: left side of the Head
    	LinearLayout top_info_ly = new LinearLayout(view_temp.getContext());
    	top_info_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	top_info_ly.setPadding(15, 5, 15, 0);
    	
    	// Separator line
    	View separator = new View(view_temp.getContext());
    	separator.setBackgroundColor(0xFF000000);
    	
    	// CHANNEL BUTTON
    	// Prepare the channel icon and its ClickListener
    	ImageButton new_btn = new ImageButton(view_temp.getContext());
    	new_btn.setId(ch.getNr()+100);	// ID of the button: ChannelNr+100
    	new_btn.setImageBitmap(ch.getIcon());
    	new_btn.setBackgroundResource(0);	// Set the background transparent
    	new_btn.setClickable(true);
    	new_btn.setPadding(0, 0, 0, 0);
    	
    	// Set listeners to execute this
    	//RemoteControl.instance().launch(ch.getUrl()); //
    	temp = ch.getUrl();
    	i_tmp = ch.getNr();
    	new_btn.setOnClickListener(new View.OnClickListener() {
    		//int channelNr = i_tmp;
			String url = temp;
    		@Override
			public void onClick(View v) {
    			
				RemoteControl.instance().launch(url);
				main.vibrate();
			}
		});
    	
    	// GETTING THE PROGRAM INFORMATION
    	// Preparing text of the white box
    	getCurrentProgram(ch);
    	TextView cur_name = new TextView(view_temp.getContext());
    	TextView cur_time = new TextView(view_temp.getContext());
    	TextView cur_date = new TextView(view_temp.getContext());
    	cur_date.setText(new SimpleDateFormat("EEEEEEEEEE dd MMM").format(currentProgram.getStart()));
    	cur_date.setTextColor(0xFF000000);
    	cur_date.setTypeface(null, Typeface.BOLD);
    	cur_time.setText(new SimpleDateFormat("HH:mm").format(currentProgram.getStart()));
    	cur_time.setTextColor(0xFF000000);
    	cur_name.setText(currentProgram.getName());
    	cur_name.setTextColor(0xFF000000);
    	
    	// Adding the view elements
    	top_info_ly.addView(cur_date);
    	top_info_ly.addView(cur_time);
    	top_info_ly.addView(cur_name);
    	top_ly.addView(top_info_ly, topInfoLyParams);
    	top_ly.addView(new_btn, wrapContentParams);
    	
    	channel_ly.addView(top_ly);
    	channel_ly.addView(separator, separatorParams);
    	
    	// Now we can add the rest of the programs info
    	// Adding the rest of the programs
    	addNextPrograms(channel_info_ly, ch);
    	ly_temp = channel_info_ly;
    	
    	channel_content.addView(channel_info_ly);
    	channel_content.post(new Runnable() {
    		LinearLayout channel_info_ly = ly_temp;
    		ScrollView x = channel_content;
    		LinearLayout i = scrollTo;
    		TextView scroll_when_focused = scrollToFocused;
            public void run() {
            	
            	// If we have a focus target and its parent is the current channel
            	if(scroll_when_focused != null && scroll_when_focused.getParent() == channel_info_ly){
            		x.scrollTo(0, scroll_when_focused.getTop());
            		x.setFocusableInTouchMode(true);
            		x.requestFocus();
            	}else{
            		x.scrollTo(0, i.getTop());
            	}
            	
            }
        });
    	
    	channel_ly.addView(channel_content);
    	content_layout.addView(channel_ly, layoutParams);
    	
    }
    
    /**
     * Adds the information of the next programs (the non-current ones)
     * @param channel_info_ly	Layout where it will add the information
     * @param ch				Channel whose programs we want to add
     */
    public void addNextPrograms(LinearLayout channel_info_ly, Channel ch){
    	
    	// Separator parameters
        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		
    	for(Program program: ch){
    		
    		LinearLayout nextInfo_container = new LinearLayout(view_temp.getContext());
    		nextInfo_container.setOrientation(1);
    		
    		// Separator between program names
    		View separator_temp = new View(view_temp.getContext());
    		View separator_temp2 = new View(view_temp.getContext());
        	separator_temp.setBackgroundColor(0xFF000000);
        	separator_temp2.setBackgroundColor(0xFF000000);
        	// Program name
    		String time = new SimpleDateFormat("HH:mm").format(program.getStart());
    		TextView nextName = new TextView(view_temp.getContext());
    		TextView nextInfo = new TextView(view_temp.getContext());
    		nextName.setText(time+" - "+program.getName());
    		nextName.setTextColor(0xFF000000);
    		nextName.setPadding(15, 5, 15, 5);
    		nextInfo.setText(program.getLongText());
    		nextInfo.setTextColor(0xFF444444);	// A little of grey for the non-current channel descriptions
    		nextInfo.setPadding(15, 5, 15, 5);
    		nextInfo_container.setId(ch.getNr()*program.getEventID());	// Program info ID = ch.getNr()*pr[i].getEventID()
    		
    		// We want to initially display only the information of the current program
    		if(currentProgram == program){
    			nextName.setTypeface(null, Typeface.BOLD);
    			nextInfo_container.setVisibility(LinearLayout.VISIBLE);
    			scrollTo = nextInfo_container;
    		}else{
    			nextInfo_container.setVisibility(LinearLayout.GONE);
    		}
    		
    		// Focus on the program we clicked on
    		if(focusOnThis != null && focusOnThis == program){
    			nextInfo_container.setVisibility(LinearLayout.VISIBLE);
    			scrollToFocused = nextName;
    		}
    		
    		i_tmp = ch.getNr()*program.getEventID();
    		
    		// Show/hide program information by clicking on its name
    		nextName.setClickable(true);
    		nextName.setOnClickListener(new View.OnClickListener() {
				int id = i_tmp;
				@Override
				public void onClick(View v) {
					
					LinearLayout x = (LinearLayout)view_temp.findViewById(id);
					if(x.getVisibility() == LinearLayout.GONE)
						x.setVisibility(LinearLayout.VISIBLE);
					else
						x.setVisibility(LinearLayout.GONE);
				}
			});

    		// Add both informations to the screen
    		channel_info_ly.addView(separator_temp, separatorParams);
    		channel_info_ly.addView(nextName);
    		channel_info_ly.addView(separator_temp2, separatorParams);
    		nextInfo_container.addView(nextInfo);
        	nextInfo_container.addView(addProgramButtons());	// BOTTOM BUTTONS SECTION
    		channel_info_ly.addView(nextInfo_container);
    		
    	}

    }
    
    /**
     * Returns the set of buttons for a program (Fav, Imdb, Social)
     * @return	View containig the buttons
     */
    public View addProgramButtons(){
    	
    	// Get the width of the screen
    	int width_screen = getResources().getDisplayMetrics().widthPixels;
    	
    	// Now we add the Imdb, Fav and Social buttons
    	LinearLayout.LayoutParams but_params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	but_params.setMargins(0, 0, 0, 0);
    	LinearLayout.LayoutParams but_left_space_params = new LinearLayout.LayoutParams(width_screen-205, LayoutParams.WRAP_CONTENT);
    	but_left_space_params.setMargins(0, 0, 0, 0);
    	LinearLayout but_left_space = new LinearLayout(view_temp.getContext());
    	but_left_space.setOrientation(1);
    	LinearLayout but_space = new LinearLayout(view_temp.getContext());
    	but_space.setOrientation(0);
    	LinearLayout but_menu = new LinearLayout(view_temp.getContext());
    	but_menu.setOrientation(0);	// Vertical 1; Horizontal 0
    	but_menu.setPadding(15, 0, 15, 5);
    	
    	// Buttons
    	social_but = new ImageButton(view_temp.getContext());
    	social_but.setImageResource(R.drawable.social_icon);
    	social_but.setBackgroundResource(0);
    	fav_but = new ImageButton(view_temp.getContext());
    	fav_but.setImageResource(R.drawable.rating_not_favorite);
    	fav_but.setBackgroundResource(0);
    	fav_but.setPadding(0, 10, 0, 0);
    	imdb_but = new ImageButton(view_temp.getContext());
    	imdb_but.setImageResource(R.drawable.imdb_icon);
    	imdb_but.setBackgroundResource(0);

    	
    	// Set functions to the buttons (listeners)
    	fav_but.setOnClickListener(new OnClickListener() {
			ImageButton thisOne = fav_but;
			@Override
			public void onClick(View v) {
				
				// We will use setTag to associate custom data to the button
				// In this case, the custom data is if its favorite or not
				if(thisOne.getTag() == (Object)0){
					thisOne.setTag(1);
					thisOne.setImageResource(R.drawable.favorite_icon);
				}else{
					thisOne.setTag(0);
					thisOne.setImageResource(R.drawable.rating_not_favorite);
				}
				/*
				 * TODO: Add here some code to add this channel to the favorite list
				 */
			}
		});
    	
    	social_but.setOnClickListener(new OnClickListener() {
			//ImageButton thisOne = social_but;
			@Override
			public void onClick(View v) {
				
				/*
				 * TODO: Add here some code to add this channel to the favorite list
				 */
			}
		});
    	
    	imdb_but.setOnClickListener(new OnClickListener() {
			//ImageButton thisOne = imdb_but;
			@Override
			public void onClick(View v) {
				
				/*
				 * TODO: Add here some code to add this channel to the favorite list
				 */
			}
		});
    	
    	// Add the buttons to the layout
    	but_menu.addView(imdb_but, but_params);
    	but_menu.addView(fav_but, but_params);
    	but_menu.addView(social_but, but_params);
    	but_space.addView(but_left_space, but_left_space_params);
    	but_space.addView(but_menu);
    	return but_space;
    }
    
    /**
     * Extension of AsyncTask created to load asynchronously the EPG data
     *
     */
	private class AsyncDataLoader extends AsyncTask<Integer, Integer, EPG>{

		/**
		 * Fetch the EPG asynchronously
		 */
		@Override
		protected EPG doInBackground(Integer... params) {
			return getFullEPG();
		}
		
		/**
		 * Add the channels information when the EPG is fetched
		 */
		@Override
		protected void onPostExecute(EPG epgPassed) {
			pb.setVisibility(View.GONE);
			epg = epgPassed;
			addAllChannelsInformationToLayout();
			
		}
		
	} // End of class definition

	/**
	 * Get the current program
	 * @return Success of the operation
	 */
	private int getCurrentProgram(Channel ch){
		
		Channel channel = ch;
		Date now = new Date(System.currentTimeMillis());
		
		// Get list of programs for this channel
		for (Program program : channel) {
			if(now.compareTo(program.getStart()) >= 0){
				currentProgram = program;
			}else{
				break;
			}
		}
		
		if(currentProgram == null){
			return -1;
		}

		return 0;
	}
	
	/**
	 * Set the focus on a specific item
	 * @param id	Item we want to set the focus on
	 */
	public void focusOnLinearLayout(int id){
		LinearLayout elem = (LinearLayout) view_temp.findViewById(id);
		elem.setFocusableInTouchMode(true);
		elem.requestFocus();
	}
	
	/**
	 * Show specific item
	 * @param id	Item we want to set the focus on
	 */
	public void showInformation(int id){
		TextView elem = (TextView) view_temp.findViewById(id);
		elem.setVisibility(TextView.VISIBLE);
	}
	
	/**
	 * Hide specific item
	 * @param id	Item we want to set the focus on
	 */
	public void hideInformation(int id){
		TextView elem = (TextView) view_temp.findViewById(id);
		elem.setVisibility(TextView.GONE);
	}

}
