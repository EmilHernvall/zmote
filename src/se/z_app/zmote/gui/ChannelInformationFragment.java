package se.z_app.zmote.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;
import android.R.color;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	
	private Program currentProgram = null;
	private Program nextProgram = null;
	private Program nextProgram2 = null;
	private Program nextProgram3 = null;
	private Program nextProgram4 = null;
	public ProgramInfo programInf[] = new ProgramInfo[10];	//We will see the next 10 programs
	
	public ChannelInformationFragment(){
		
	}
	
	public ChannelInformationFragment(MainTabActivity main){
		this.main = main;
	}
	
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
    	
    	Iterator<Channel> itr = epg.iteratorByNr();
    	
    	while(itr.hasNext()){
    		Channel channel = itr.next();
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
    	
    	// "White box" parameters
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width_screen-40, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(15, 15, 15, 30);
        
        // Standard parameters
        LinearLayout.LayoutParams wrapContentParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       
        // Separator parameters
        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
        
        // Prepare the "white box" for the program information
    	LinearLayout channel_ly = new LinearLayout(view_temp.getContext()); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(0xFFFFFFFF);
    	channel_ly.setPadding(10, 5, 10, 5);
    	channel_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	
    	// The information part of the box will be scrollable
    	ScrollView channel_content = new ScrollView(view_temp.getContext());
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
    	getPrograms(ch);
    	TextView cur_info = new TextView(view_temp.getContext());
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
    	cur_info.setText(currentProgram.getLongText());
    	cur_info.setTextColor(0xFF000000);
    	cur_info.setPadding(15, 5, 15, 5);
    	
    	// Adding the view elements
    	top_info_ly.addView(cur_date);
    	top_info_ly.addView(cur_time);
    	top_info_ly.addView(cur_name);
    	top_ly.addView(top_info_ly, wrapContentParams);
    	top_ly.addView(new_btn, wrapContentParams);
    	
    	channel_ly.addView(top_ly);
    	channel_ly.addView(separator, separatorParams);
    	
    	//channel_info_ly.addView(cur_info);
    	channel_info_ly.addView(cur_info);
    	
    	// BOTTOM BUTTONS SECTION
    	// Now we add the Imdb, Fav and Social buttons
    	LinearLayout but_menu = new LinearLayout(view_temp.getContext());
    	but_menu.setOrientation(0);	// Vertical 1; Horizontal 0
    	but_menu.setPadding(15, 15, 15, 5);
    	
    	// Button
    	
    	
    	channel_info_ly.addView(but_menu);
    	// Now we can add the rest of the programs info
    	
    	// TODO: Shorten the above code
    	// Adding the rest of the programs
    	Program[] pr = new Program[5];
    	pr[1] = nextProgram;	// We prefer the programs in an array
    	pr[2] = nextProgram2;	// this way we can use a for loop
    	pr[3] = nextProgram3;	// Don't used array as global variable because
    	pr[4] = nextProgram4;	// we were unable to avoid a weird error
    	
    	for(int i=1; i<5; ++i){
    		
    		// Separator between program names
    		View separator_temp = new View(view_temp.getContext());
        	separator_temp.setBackgroundColor(0xFF000000);
        	// Program name
    		String time = new SimpleDateFormat("HH:mm").format(pr[i].getStart());
    		TextView nextName = new TextView(view_temp.getContext());
    		nextName.setText(time+" - "+pr[i].getName());
    		nextName.setTextColor(0xFF000000);
    		nextName.setPadding(15, 5, 15, 5);
    		// Add both informations to the screen
    		channel_info_ly.addView(separator_temp, separatorParams);
    		channel_info_ly.addView(nextName);
    	}
    	
    	channel_content.addView(channel_info_ly);
    	channel_ly.addView(channel_content);
    	content_layout.addView(channel_ly, layoutParams);
    	
    }
    
    
	private class AsyncDataLoader extends AsyncTask<Integer, Integer, EPG>{

		@Override
		protected EPG doInBackground(Integer... params) {
			return getFullEPG();
		}
		
		/**
		 * Fetch de EPG information asynchronously
		 */
		@Override
		protected void onPostExecute(EPG epgPassed) {
			pb.setVisibility(View.GONE);
			epg = epgPassed;
			addAllChannelsInformationToLayout();
			
		}
		
	} // End of class definition

	/**
	 * Fetch and set the description of the current program and the names
	 * of the next programs.
	 * @return Success of the operation
	 */
	private int getPrograms(Channel ch){
		
		Channel channel = ch;
		Date now = new Date(System.currentTimeMillis());
		
		// Get list of programs for this channel
		for (Program program : channel) {
			if(now.compareTo(program.getStart()) >= 0){
				currentProgram = program;
			}else if(nextProgram == null){
				nextProgram = program;
			}else if(nextProgram2 == null){
				nextProgram2 = program;
			}else if(nextProgram3 == null){
				nextProgram3 = program;
			}else if(nextProgram4 == null){
				nextProgram4 = program;
			}else{
				break;
			}
		}
		
		if(currentProgram == null){
			return -1;
		}

		return 0;
		//END*/
	}
	
	
	public String setText(Program p){
		String t ="";
		t.concat(p.getName());
		t.concat(new SimpleDateFormat("HH:mm").format(p.getStart()));
		t.concat(p.getLongText());
		return t;
	}
	/**
	 * Shorten a string to a desired size
	 * @param s		Original string we want to cut
	 * @param max	Maximum amount of characters
	 * @return	The shortened string
	 */
	private String trimString(String s, int max){
		if(s.length() > max){
			s = s.substring(0, max-4) + "...";
		}
		return s;
	}
	
	public class ProgramInfo{
		public String name;
		public String info;
		public String date;
		
		public void setName(String n){
			name = n;
		}
	}

}
