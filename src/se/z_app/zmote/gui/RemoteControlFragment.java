package se.z_app.zmote.gui;


import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RCProxy;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class RemoteControlFragment extends Fragment {
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
	
	private Channel temp;
	private LinearLayout channel_icons_layout;
	private EPG epg;
	private EPGQuery query = new EPGQuery();
	private boolean fetched = false;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/*
		 * TODO: Follow the current channel in the icon bar
		 * change the background when changing channel
		 * */
		view = inflater.inflate(R.layout.fragment_remote_control, null);
		channel_icons_layout = (LinearLayout)view.findViewById(R.id.channel_icons_ly);
		

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
	public void btnListeners(View view) {
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

		arrow_up_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().up();
			}
		});

		arrow_down_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().down();
			}
		});

		arrow_left_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().left();
			}
		});

		arrow_right_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().right();
			}
		});

		confirm_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().ok();
			}
		});

		store_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().menu();
			}
		});

		undo_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().back();
			}
		});

		mute_volume_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().mute();
			}
		});

		info_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().info();
			}
		});
		exit_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				RCProxy.instance().exit();
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
	 *  This function is suppose to load a new channel in the main activity view
	 *  That means: put the icon of the channel in the list and assign it a function
	 * @param ch
	 * @author Francisco Valladares
	 */
    public void addChannelItemToLayout(Channel ch){
    
    	final ImageButton new_btn = new ImageButton(view.getContext());
    	new_btn.setId(ch.getNr()+100);	// ID of the button: ChannelNr+100
    	new_btn.setImageBitmap(ch.getIcon());
    	new_btn.setBackgroundResource(0);	// Set the background transparent
    	new_btn.setClickable(true);
    	new_btn.setPadding(0, 0, 0, 0);
   
    	
    	// Set listeners to execute this
    	//RemoteControl.instance().launch(ch.getUrl()); //

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
