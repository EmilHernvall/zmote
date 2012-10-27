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

import java.util.Iterator;

import android.R.color;
import android.os.Bundle;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import se.z_app.stb.Channel;
import android.graphics.Bitmap;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;
import android.widget.TextView;


public class MainActivityView2 extends ZmoteActivity {

	private EPGQuery query = new EPGQuery();
	private EPG epg;
	String temp;
	int i_tmp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_view2);
        
        //Import the whole EPG
        epg = query.getEPG();
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
    	Bitmap icon = ch.getIcon();
    	//String name = ch.getName();
    	LinearLayout h_layout = (LinearLayout) findViewById(R.id.channel_icons_ly);
    	ImageButton new_btn = new ImageButton(this);
    	new_btn.setImageBitmap(icon);
    	new_btn.setBackgroundResource(0);	// Set the background transparent
    	new_btn.setClickable(true);

    	// Set listeners to execute this
    	//RemoteControl.instance().launch(ch.getUrl()); //
    	temp = ch.getUrl();
    	i_tmp = ch.getNr();
    	new_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RemoteControl.instance().launch(temp);
				LinearLayout elem = (LinearLayout) findViewById(i_tmp);
				elem.requestFocus();
				System.out.println("Listener:"+i_tmp);
				// It's not working propertly
				}
			});
    	
    	//new_btn.setId(R.id.channel1);
    	h_layout.addView(new_btn);

    	// Now we load the information about the channel in the middle section
    	LinearLayout c_layout = (LinearLayout) findViewById(R.id.content_ly);
    	LinearLayout channel_ly = new LinearLayout(this); // Check arguments (correct?)
    	channel_ly.setBackgroundColor(color.white);
    	channel_ly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 50));
    	TextView ch_name = new TextView(this);
    	// Right now we just load the name
    	ch_name.setText(ch.getName());
    	channel_ly.addView(ch_name);
    	channel_ly.setId(ch.getNr());	// We will try to identify them by ch number
    	channel_ly.setPadding(30, 5, 30, 5);
    	channel_ly.setMinimumWidth(300);
    	// Add the information of the channel to the middle section
    	c_layout.addView(channel_ly);
    	
    } 

}
