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

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.HorizontalScrollView;
import android.widget.Button;
import se.z_app.stb.Channel;
import android.graphics.Bitmap;


public class MainActivityView2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_view2);
    }    
    
    // DUMMY
    // This function is suppose to add the whole list of channels to the view
    // It uses the function addChannelItemToLayout iteratively
    public void addAllChannelsToLayout(){
    	
    }
    
    // DUMMY
    // This function is suppose to load a new channel in the main activity view
    // That means: put the icon of the channel in the list and assign it a function
    public void addChannelItemToLayout(Channel ch){
    	Bitmap icon = ch.getIcon();
    	String name = ch.getName();
    	HorizontalScrollView h_layout = (HorizontalScrollView) findViewById(R.id.channel_icons_ly);
    	Button new_btn = new Button(this);
    	new_btn.setText(name);
    	//new_btn.setId(R.id.channel1);
    	//new_btn.setImage... --> to icon
    	h_layout.addView(new_btn);
    	
    	
    	// Listener should be added when the item exist on the layout
    	//new_btn=(Button)findViewById(R.id.recently_added_button);
    	//new_btn.setOnClickListener(new OnClickListener(){
    		
    	//});
    }
    

}
