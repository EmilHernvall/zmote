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
	
	private ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	private ArrayList<Channel> channelList = new ArrayList<Channel>();
	private int currentChannelNr;
	
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
    
    	// "White box" parameters
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(15, 15, 15, 15);
        
        // Prepare the "white box" for the program information
    	LinearLayout channel_ly = new LinearLayout(view_temp.getContext()); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(0xFFFFFFFF);
    	channel_ly.setPadding(10, 5, 10, 5);
    	channel_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	
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
    	TextView info = new TextView(view_temp.getContext());
    	info.setText(generateText(ch));
    	//info.setTextColor(0x00000000);
    	
    	// Adding the view elements
    	channel_ly.addView(new_btn);
    	channel_ly.addView(info);
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
			
			currentChannelNr = 0;
			for (Channel channel : epg) {
				Drawable draw = new BitmapDrawable(channel.getIcon());
				ImageView i = new ImageView(view_temp.getContext());
				i.setImageDrawable(draw);	
				i.setBackgroundColor(0xFFFFFFFF );
				i.setAdjustViewBounds(true);
				i.setMaxHeight(150);
				i.setMaxHeight(150);
				i.invalidate();
				
				
				imageList.add(i);
				channelList.add(channel);
				
				i.setOnClickListener(new View.OnClickListener() {
					int i = currentChannelNr;
					@Override
					public void onClick(View v) {
						//setChannel(i);
						// Change STB to the channel we clicked on	
					}
				});
				
				currentChannelNr++;
			}	// END of FOR loop
			
		}	//End of OnPostExecute
		
	} // End of class definition

	/**
	 * Fetch and set the description of the current program and the names
	 * of the next programs.
	 * @return The text inside a String
	 */
	private String generateText(Channel ch){
		String t = "";
		Channel channel = ch;
		Program currentProgram = null;
		Program nextProgram = null;
		Program nextNextProgram = null;
		Date now = new Date(System.currentTimeMillis());
		
		for (Program program : channel) {
			if(now.compareTo(program.getStart()) >= 0)
				currentProgram = program;
			else if(nextProgram == null){
				nextProgram = program;
			}else if(nextNextProgram == null){
				nextNextProgram = program;
			}else{
				break;
			}
		}
		
		if(currentProgram == null)
			return "";
		
		String name = currentProgram.getName();
		String startTime = new SimpleDateFormat("HH:mm").format(currentProgram.getStart());
		String info = currentProgram.getLongText();
		
		//name = trimString(name, 29);
		//info = trimString(info, 260);
		t = "> " + startTime +  " - " + name + "\n" ; 
		t += info + "\n\n";
		
		if(nextProgram != null){
			String nextName = nextProgram.getName();
			String nextStartTime = new SimpleDateFormat("HH:mm").format(nextProgram.getStart());
			nextName = trimString(nextName, 29);
			t += "> " + nextStartTime +  " - " + nextName + "\n";
		}
		
		if(nextNextProgram != null){
			String nextNextName = nextNextProgram.getName();
			String nextNextStartTime = new SimpleDateFormat("HH:mm").format(nextNextProgram.getStart());
			nextNextName = trimString(nextNextName, 29);
			t += "> " + nextNextStartTime +  " - " + nextNextName;
		}
		
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

}
