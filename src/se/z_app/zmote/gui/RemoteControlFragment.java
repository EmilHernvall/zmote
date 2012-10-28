package se.z_app.zmote.gui;

import se.z_app.stb.api.RCProxy;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RemoteControlFragment extends Fragment{
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


	
	public RemoteControlFragment() {
	}

	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		
		View v = inflater.inflate(R.layout.activity_remote_control, null);


		btnListeners(v);

		return v;
	}


	public void vibrate(){
		
	}
	
	public void btnListeners(View v){
		arrow_up_button=(Button)v.findViewById(R.id.arrow_up_button);
		arrow_down_button=(Button)v.findViewById(R.id.arrow_down_button);
		arrow_left_button=(Button)v.findViewById(R.id.arrow_left_button);
		arrow_right_button=(Button)v.findViewById(R.id.arrow_right_button);
		confirm_button=(Button)v.findViewById(R.id.confirm_button);
		store_button=(Button)v.findViewById(R.id.store_button);
		undo_button=(Button)v.findViewById(R.id.undo_button);
		mute_volume_button=(Button)v.findViewById(R.id.mute_volume_button);
		info_button=(Button)v.findViewById(R.id.info_button);
		exit_button=(Button)v.findViewById(R.id.exit_button);

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


}
