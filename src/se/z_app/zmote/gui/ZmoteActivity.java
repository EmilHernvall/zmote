package se.z_app.zmote.gui;

import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.RemoteControl.Button;
import se.z_app.zmote.epg.EPGQuery;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import se.z_app.stb.EPG;




public abstract class ZmoteActivity extends Activity{
	private Vibrator vibe;
	private EPGQuery query = new EPGQuery();
	private EPG epg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;
		
		// Here is a good place to add the buttons bar menu (hardcoded, not xml).
		//fetchEPG();
	}

	public void vibrate(){
		vibe.vibrate(95);
	}
	public void vibrate(int ms){
		vibe.vibrate(ms);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				RemoteControl.instance().sendButton(Button.VOLPLUS);
				vibrate();
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				RemoteControl.instance().sendButton(Button.VOLMINUS);
				vibrate();
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
	
	public void setButtonsBarListeners(){
	   
	   //Button for the remote control view
	   android.widget.Button remote_button = (android.widget.Button) findViewById(R.id.remote_button);
       remote_button.setOnClickListener(new OnClickListener() {
    	   @Override
        	public void onClick(View v) {
    		   Intent mainIntent = new Intent(ZmoteActivity.this, RemoteControlActivity.class);
                ZmoteActivity.this.startActivity(mainIntent);
         	}
  	    });
       
       // Button for home
       remote_button = (android.widget.Button) findViewById(R.id.home_button);
       remote_button.setOnClickListener(new OnClickListener() {
    	   @Override
        	public void onClick(View v) {
    		   Intent mainIntent = new Intent(ZmoteActivity.this, MainActivityView2.class);
                ZmoteActivity.this.startActivity(mainIntent);
         	}
  	    });
       
       // Button for EPG
       remote_button = (android.widget.Button) findViewById(R.id.epg_button);
       remote_button.setOnClickListener(new OnClickListener() {
    	   @Override
        	public void onClick(View v) {
    		   Intent mainIntent = new Intent(ZmoteActivity.this, EPGActivity.class);
                ZmoteActivity.this.startActivity(mainIntent);
         	}
  	    });
       /*
       // Button for Favourites
       remote_button = (android.widget.Button) findViewById(R.id.fav_button);
       remote_button.setOnClickListener(new OnClickListener() {
    	   @Override
        	public void onClick(View v) {
    		   	Intent mainIntent = new Intent(ZmoteActivity.this, FavActivity.class);
                ZmoteActivity.this.startActivity(mainIntent);
         	}
  	    });
       
       // Button for WebTV
       remote_button = (android.widget.Button) findViewById(R.id.webtv_button);
       remote_button.setOnClickListener(new OnClickListener() {
    	   @Override
        	public void onClick(View v) {
    		   	Intent mainIntent = new Intent(ZmoteActivity.this, WebTVActivity.class);
                ZmoteActivity.this.startActivity(mainIntent);
         	}
  	    });
       */
     }
	
	/* 
	 * Fetch the EPG from the STB 
	 */
	public void fetchEPG(){
		epg = query.getEPG();
		// The upper line should be on the OnCreate method
		// This way will be fetched only one time and used several times
	}
	
	/*
	 * Return the EPG
	 */
	public EPG getFullEPG(){
		return epg;
	}

}
