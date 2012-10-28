/* This class purpose is to try functions about filling the main view with content:
 * Channels, Icons, EPG info about them and STB name 
 * Please, don't erase it. If you feel that this functions should be in other file,
 * just move it and note this change in the commit message.
 * 
 * P.D: activity_main_activity_view is just a copy of the main_activity_view layout
 * 		At the end of the edition of this functions implemented on this file, this
 * 		 .xml layout should be erased or renamed to the main activity view layout.
 * 																					*/

package se.z_app.zmote.gui;

import java.util.Date;
import java.util.Iterator;

import android.R.color;
import android.os.Bundle;


import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import se.z_app.stb.Channel;
import se.z_app.stb.Program;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RemoteControl;
import android.widget.TextView;


public class MainActivityView2 extends ZmoteActivity {

	private EPG epg;
	String temp;
	int i_tmp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_view2);
        
        setButtonsBarListeners();	// Set the listeners for the buttons bar menu
        
        //Import the whole EPG
        fetchEPG();
        epg = getFullEPG();
    	// This should be done in other place because now only loads the first stb
    	// you push, but doesn't change of stb never because the method OnCreate is
    	// not executing again
        
        // Change the STB name
    	TextView stbName = (TextView) findViewById(R.id.stb_name);
    	stbName.setText( epg.getStb().getBoxName() );

        // We add the channels to the view
    	addAllChannelsToLayout();

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
    
    	LinearLayout h_layout = (LinearLayout) findViewById(R.id.channel_icons_ly);
    	ImageButton new_btn = new ImageButton(this);
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
				LinearLayout elem = (LinearLayout) findViewById(channelNr);
				elem.setFocusableInTouchMode(true);
				elem.requestFocus();
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
    	LinearLayout c_layout = (LinearLayout) findViewById(R.id.content_ly);
    	LinearLayout channel_ly = new LinearLayout(this); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(color.white);	// This is not doing anything
    	channel_ly.setLayoutParams(new LayoutParams(300, 500));
    	channel_ly.setOrientation(1);	// Vertical 1; Horizontal 0
    	
    	// Set listeners for the descriptions to link to the channel icon
    	i_tmp = ch.getNr();
    	channel_ly.setOnClickListener(new View.OnClickListener() {
    		int channelNr = i_tmp+100;
    		@Override
			public void onClick(View v) {
				
				ImageButton elem = (ImageButton) findViewById(channelNr);
				elem.setFocusableInTouchMode(true);
				elem.requestFocus();
				}
			});
    	
    	
    	TextView ch_name = new TextView(this);
    	TextView pr_name = new TextView(this);
    	TextView pr_short_desc = new TextView(this);
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
