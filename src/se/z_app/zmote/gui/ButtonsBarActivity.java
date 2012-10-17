package se.z_app.zmote.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ButtonsBarActivity extends Activity {
	private Button remote_button;
//	private RemoteControlActivity remote_activity;
	
	
	//Inflate Buttons Bar View
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons_bar);
        btnListeners();
      
    }

	
	public void btnListeners(){
		
		//Button for the remote control view
 		   remote_button=(Button)findViewById(R.id.remote_button);
	       remote_button.setOnClickListener(new OnClickListener() {
	    	   @Override
	        	public void onClick(View v) {
	    		   Intent mainIntent = new Intent(ButtonsBarActivity.this, RemoteControlActivity.class);
	                ButtonsBarActivity.this.startActivity(mainIntent);
             	}
	  	    });
	       
	       
 		 
      }
	
}
