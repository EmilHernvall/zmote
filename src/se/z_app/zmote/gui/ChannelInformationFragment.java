package se.z_app.zmote.gui;

import java.util.Date;
import java.util.Iterator;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;
import android.R.color;

import android.graphics.Picture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * DEPRECATED
 *  Main view: View with the channels and their information 
 * 
 * */
public class ChannelInformationFragment extends Fragment{
	LinearLayout h_layout;
    LinearLayout c_layout;
	
	private View v;
	private EPG epg;
	String temp;
	int i_tmp;
	private EPGQuery query = new EPGQuery();
	private EPG epgFetched;
	private boolean fetched = false;
	private MainTabActivity main;
	
	public ChannelInformationFragment(MainTabActivity main){
		this.main = main;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 v = inflater.inflate(R.layout.fragment_channel_information, null);
        
		 h_layout = (LinearLayout)v.findViewById(R.id.channel_icons_ly);
		 c_layout = (LinearLayout)v.findViewById(R.id.content_ly);
       // setButtonsBarListeners();	// Set the listeners for the buttons bar menu
        epg = getFullEPG();
    	// This should be done in other place because now only loads the first stb
    	// you push, but doesn't change of stb never because the method OnCreate is
    	// not executing again
        
    	
    	addAllChannelsToLayout();
    	
    	return v;
    }    
	
	/*
	 * Return if the EPG is already fetched or not
	 */
	public boolean isFetchedTheEPG(){
		return fetched;
	}
	
	/* 
	 * Fetch the EPG from the STB 
	 */
	public void fetchEPG(){
		epgFetched = query.getEPG();
		fetched = true;
		// The upper line should be on the OnCreate method
		// This way will be fetched only one time and used several times
	}
	
	/*
	 * Return the EPG after fetching it (just do it one time now)
	 */
	public EPG getFullEPG(){
		if(!fetched)
			fetchEPG();
		return epgFetched;
	}
	

	
	
    
    // DUMMY
    // This function is suppose to add the whole list of channels to the view
    // It uses the function addChannelItemToLayout iteratively
    public void addAllChannelsToLayout(){
    	
    	Iterator<Channel> itr = epg.iteratorByNr();
    	
    	while(itr.hasNext()){
    		Channel channel = itr.next();
    		addChannelItemToLayout(channel);
    	}

    }
    
    // DUMMY
    // This function is suppose to load a new channel in the main activity view
    // That means: put the icon of the channel in the list and assign it a function
    
    
    public void addChannelItemToLayout(Channel ch){
    
    	
    	ImageButton new_btn = new ImageButton(v.getContext());
    	new_btn.setId(ch.getNr()+100);	// ID of the button: ChannelNr+100
    	new_btn.setImageBitmap(ch.getIcon());
    	new_btn.setBackgroundResource(0);	// Set the background transparent
    	new_btn.setClickable(true);
    	
    	// Set listeners to execute this
    	//RemoteControl.instance().launch(ch.getUrl()); //
    	temp = ch.getUrl();
    	i_tmp = ch.getNr();
    	new_btn.setOnClickListener(new View.OnClickListener() {
    		int channelNr = i_tmp;
			String url = temp;
    		@Override
			public void onClick(View v) {
				
				RemoteControl.instance().launch(url);
				LinearLayout elem = (LinearLayout) c_layout.findViewById(channelNr);
				elem.setFocusableInTouchMode(true);
				elem.requestFocus();
				main.vibrate();
			}
		});
    	
    	h_layout.addView(new_btn);

    	// GETTING THE PROGRAM INFORMATION
    	// Check this code, especially the initialization of the iterator
    	// With currentProgram = itr; does not work
    	// But, what would happen if we need to show the FIRST program?
    	Iterator<Program> itr = ch.iterator();
    	Program currentProgram = itr.next();
    	Program nextProgram = currentProgram;
    	Date now = new Date(System.currentTimeMillis());
    	while(itr.hasNext()){
    		Program program = (Program)itr.next();
    	
    		if(now.compareTo(program.getStart()) < 0){
    			currentProgram = program;
    		}else{
    			nextProgram = program;
    			break;
    		}
    		
    	}
    	
    	
    	// Now we load the information about the channel in the middle section
    	
    	LinearLayout channel_ly = new LinearLayout(v.getContext()); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(color.white);	// This is not doing anything
    	channel_ly.setLayoutParams(new LayoutParams(300, 500));
    	channel_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	
    	// Set listeners for the descriptions to link to the channel icon
    	i_tmp = ch.getNr();
    	channel_ly.setOnClickListener(new View.OnClickListener() {
    		int channelNr = i_tmp+100;
    		@Override
			public void onClick(View v) {
				
				ImageButton elem = (ImageButton)h_layout.findViewById(channelNr);
				elem.setFocusableInTouchMode(true);
				elem.requestFocus();
				main.vibrate();
				}
			});
    	
    	
    	TextView ch_name = new TextView(v.getContext());
    	TextView pr_name = new TextView(v.getContext());
    	TextView pr_short_desc = new TextView(v.getContext());
    	
    	// Right now we just load the name
    	ch_name.setText(ch.getName());
    	pr_name.setSingleLine(false);
    	pr_short_desc.setSingleLine(false);
    	
    	@SuppressWarnings("deprecation")
		String curTime = currentProgram.getStart().getHours()+":"+currentProgram.getStart().getMinutes();
    	@SuppressWarnings("deprecation")
    	String nexTime = nextProgram.getStart().getHours()+":"+nextProgram.getStart().getMinutes();
    	pr_name.setText("\n"+curTime+"- "+currentProgram.getName()+"\n"
    						+nexTime+"- "+nextProgram.getName()+"\n");
    	pr_short_desc.setText(currentProgram.getLongText());
    	
    	// We add the new items and give the LinearLayout a new id and some properties
    	channel_ly.addView(ch_name);
    	channel_ly.addView(pr_name);
    	channel_ly.addView(pr_short_desc);
    	channel_ly.setId(ch.getNr());	// We will try to identify them by ch number
    	channel_ly.setPadding(30, 5, 30, 5);
    	channel_ly.setMinimumWidth(300);
    	
    	// Add the information of the channel to the middle section
    	c_layout.addView(channel_ly);
    	
    } 

}
