package se.z_app.zmote.gui;


import java.util.Observable;
import java.util.Observer;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RCProxy;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGContentHandler;
import se.z_app.zmote.epg.EPGQuery;
import se.z_app.zmote.gui.SnapHorizontalScrollView.MyGestureDetector;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


/**
 * Creates the view with the remote control buttons, the channel icons list and some other buttons
 * @author Thed Mannerlšf & Ralph Nilsson & Mar’a Platero
 * 
 */
public class RemoteControlFragment extends Fragment implements Observer {
	private Button arrow_up_button;
	private Button arrow_down_button;
	private Button arrow_left_button;
	private Button arrow_right_button;
	private Button confirm_button;
	private Button store_button;
	private Button undo_button;
	private Button mute_volume_button;
	private Button info_button;
	private Button exit_button;
	private MainTabActivity main;
	private View view;
	
	private Channel active_ch;
	private Channel temp;
	private LinearLayout channel_icons_layout;
	private EPG epg;
	private EPGQuery query = new EPGQuery();
	private boolean fetched = false;
	private boolean muted = false;
	private ImageButton active;

	/**
	 * Creates the remote control
	 * @param main
	 * @author 
	 */
	public RemoteControlFragment(MainTabActivity main) {
		this.main = main;
	}

	public static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Updates and highlight the current channel asynchronously and in other thread
	 * @author Rasmus
	 */
	@Override
	public void update(Observable observable, Object data) {

		main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				highlightChannel();
			}
		});
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		EPGContentHandler.instance().addObserver(this);
		
		/*
		 * TODO: Follow the current channel in the icon bar
		 * change the background when changing channel
		 * */
		view = inflater.inflate(R.layout.fragment_remote_control, null);
		channel_icons_layout = (LinearLayout)view.findViewById(R.id.channel_icons_ly);
	
		active_ch = query.getCurrentChannel();

		btnListeners(view);
		new AsyncDataLoader().execute();

		return view;
	}

	/**
	 * Adds vibrate function to application
	 * @author
	 */
	public void vibrate() {
		main.vibrate();
	}

	/**
	 * Initiate button listeners
	 * @param v
	 * @author 
	 */
	public void btnListeners(final View view) {
		arrow_up_button = (Button) view.findViewById(R.id.arrow_up_button);
		arrow_down_button = (Button) view.findViewById(R.id.arrow_down_button);
		arrow_left_button = (Button) view.findViewById(R.id.arrow_left_button);
		arrow_right_button = (Button) view.findViewById(R.id.arrow_right_button);
		confirm_button = (Button) view.findViewById(R.id.confirm_button);
		store_button = (Button) view.findViewById(R.id.store_button);
		undo_button = (Button) view.findViewById(R.id.undo_button);
		mute_volume_button = (Button) view.findViewById(R.id.mute_volume_button);
		info_button = (Button) view.findViewById(R.id.info_button);
		exit_button = (Button) view.findViewById(R.id.exit_button);

		// Sanity check
		if(active_ch == null) active_ch = epg.getChannel(1);
		
		// Listener with visual feedback for the button
		arrow_up_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
            	    vibrate();
   					RCProxy.instance().up();			
   					highlightChannel();
                    arrow_up_button.setBackgroundColor(0xFFFFFFFF);
                    // Put here the "light" button
                    arrow_up_button.setBackgroundResource(R.drawable.up_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	arrow_up_button.setBackgroundColor(0xFF000000);
                	arrow_up_button.setBackgroundResource(R.drawable.up_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Listener with visual feedback for the button
		arrow_down_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
            	    vibrate();
					RCProxy.instance().down();
					highlightChannel();
                    arrow_down_button.setBackgroundColor(0xFFFFFFFF);
                    // Put here the "light" button
                    arrow_down_button.setBackgroundResource(R.drawable.down_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	arrow_down_button.setBackgroundColor(0xFF000000);
                	arrow_down_button.setBackgroundResource(R.drawable.down_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Visual feedback for the button
		arrow_left_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
            	   	vibrate();
   					RCProxy.instance().left();
   					highlightChannel();
                    arrow_left_button.setBackgroundColor(0xFFFFFFFF);
                    // Put here the "light" button
                    arrow_left_button.setBackgroundResource(R.drawable.left_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	arrow_left_button.setBackgroundColor(0xFF000000);
                	arrow_left_button.setBackgroundResource(R.drawable.left_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Visual feedback for the button
		arrow_right_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	   				RCProxy.instance().right();
	   				highlightChannel();
                    arrow_right_button.setBackgroundColor(0xFFFFFFFF);
                    // Put here the "light" button
                    arrow_right_button.setBackgroundResource(R.drawable.right_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	arrow_right_button.setBackgroundColor(0xFF000000);
                	arrow_right_button.setBackgroundResource(R.drawable.right_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });
		
		// Listener with visual feedback for the button
		confirm_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().ok();
                    confirm_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
                    confirm_button.setBackgroundResource(R.drawable.middle_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	confirm_button.setBackgroundColor(0xFF000000);
                	confirm_button.setBackgroundResource(R.drawable.middle_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Listener with visual feedback for the button
		store_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().menu();
	            	store_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
	            	store_button.setBackgroundResource(R.drawable.settings_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	store_button.setBackgroundColor(0xFF000000);
                	store_button.setBackgroundResource(R.drawable.settings_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Listener with visual feedback for the button
		undo_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().back();
	            	undo_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
	            	undo_button.setBackgroundResource(R.drawable.back_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	undo_button.setBackgroundColor(0xFF000000);
                	undo_button.setBackgroundResource(R.drawable.back_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });
	
		// Listener with visual feedback for the button
		mute_volume_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().mute();
	            	mute_volume_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
	            	if(muted == true){
	            		mute_volume_button.setBackgroundResource(R.drawable.unmute_pressed);
	            	}else{
	            		mute_volume_button.setBackgroundResource(R.drawable.mute_pressed);	
	            	}
	            	
                    return true;
                    
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                	mute_volume_button.setBackgroundColor(0xFF000000);
                	
	            	if(muted == true){
	            		mute_volume_button.setBackgroundResource(R.drawable.unmute_normal);
	            	}else{
	            		mute_volume_button.setBackgroundResource(R.drawable.mute_normal);
	            	}
	            	 
	            	if(muted == true){
	              	   muted = false;
	                }else{
	              	   muted = true;
	                }
	            	
                    return true;
                }else{
                    return false;
                }
               
              
            }
       });

		// Listener with visual feedback for the button
		info_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().info();
	            	info_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
	            	info_button.setBackgroundResource(R.drawable.info_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	info_button.setBackgroundColor(0xFF000000);
                	info_button.setBackgroundResource(R.drawable.info_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });

		// Listener with visual feedback for the button
		exit_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
               if(event.getAction() == MotionEvent.ACTION_DOWN){
	            	vibrate();
	            	RCProxy.instance().exit();
	            	exit_button.setBackgroundColor(0xFFFFFFFF);
                    
                    // Put here the "light" button
	            	exit_button.setBackgroundResource(R.drawable.cancel_pressed);
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                	exit_button.setBackgroundColor(0xFF000000);
                	exit_button.setBackgroundResource(R.drawable.cancel_normal);	
                    return true;
                }else{
                    return false;
                }
            }
        });
		
	}

	
	/**
	 * Fetch the EPG from the STB 
	 * @author Francisco Valladares
	 */
	public void fetchEPG(){
		epg = query.getEPG();
		fetched = true;
		// The upper line should be on the OnCreate method
		// This way will be fetched only one time and used several times
	}
	
	/**
	 * Return the EPG after fetching it (just do it one time now)
	 * @author Francisco Valladares
	 */
	public EPG getFullEPG(){
		if(!fetched)
			fetchEPG();
		return epg;
	}

	
    /**
     *  This function is suppose to add the whole list of channels to the view
     *  It uses the function addChannelItemToLayout iteratively
     *  @author Francisco Valladares
     */
    public void addAllChannelsToLayout(){
    	
    	getFullEPG();
    	for(Channel channel: epg){
    		addChannelItemToLayout(channel);
    	}
    	
    }
	
    /**
     * This functions check the current channel and highlights it when
     * pressed in the arrows of the remote control
     * @author Maria Platero
     */
    public void highlightChannel(){
    	
    	/*
		 * TODO: FIX getCurrentChannel();
		 * check that is still working when currentChannel() is working
		 * it crash if you press an arrow before pressing any channel in the 
		 * icon list
		 */
		final EPGQuery query_t = new EPGQuery();
		epg = query_t.getEPG();
		getFullEPG();
		
		active_ch = query_t.getCurrentChannel();
		for (Channel channel : epg) {		
			if(active_ch.getUrl().toLowerCase().contains(channel.getUrl().toLowerCase())){
				active_ch = channel;
				break;
			}
		}
			
			if(active_ch != null){
				int num = active_ch.getNr();
				num = num + 100;
				System.out.println("ID when click: "+num);
			
				ImageButton boton = (ImageButton) view.findViewById(num);
				if(boton != null){
					boton.setBackgroundResource(R.color.abs__background_holo_light);
					boton.setFocusableInTouchMode(true);
					boton.requestFocus();
				}
				
				if(boton != active && active != null){				
					active.setBackgroundResource(0);
					active = boton;
				}
			}
		
    }	
    
	/**
	 *  This function is suppose to load a new channel in the main activity view
	 *  That means: put the icon of the channel in the list and assign it a function
	 * @param ch
	 * @author Francisco Valladares 
	 */
    public void addChannelItemToLayout(Channel ch){
    
    	final ImageButton new_btn = new ImageButton(view.getContext());
    	new_btn.setId(ch.getNr()+100);	// ID of the button: ChannelNr+100
    	System.out.println("ID when adding: "+new_btn.getId());
    	new_btn.setImageBitmap(ch.getIcon());
    	new_btn.setBackgroundResource(0);	// Set the background transparent
    	new_btn.setClickable(true);
    	new_btn.setPadding(0, 0, 0, 0);
 	

    	temp = ch;
    	new_btn.setOnClickListener(new View.OnClickListener() {

    		Channel channel = temp;
    		@Override
			public void onClick(View v) {
    		
    			if(new_btn != active && active !=null){
    				active.setBackgroundResource(0);	
    			}
    			
    			new_btn.setBackgroundResource(R.color.abs__background_holo_light);
    			RemoteControl.instance().launch(channel);
				main.vibrate();
				active = new_btn;
				
				
			}
    		
		});
    	

    	channel_icons_layout.addView(new_btn);	
    }
	
    /**
	 * Loads the information asynchronously
	 * @author Francisco Valladares
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
			view.findViewById(R.id.progressBarRemote).setVisibility(View.GONE);
			
			addAllChannelsToLayout();
		}	

	}

}
