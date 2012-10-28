package se.z_app.zmote.gui;


import se.z_app.stb.api.RCProxy;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RemoteControlActivity extends ZmoteActivity {
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

	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnListeners();
        setButtonsBarListeners();	// Set the listeners for the buttons bar menu
        setSTBName();	// General func.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remote_control, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
 	public void btnListeners(){
 		arrow_up_button=(Button)findViewById(R.id.arrow_up_button);
 		arrow_down_button=(Button)findViewById(R.id.arrow_down_button);
 		arrow_left_button=(Button)findViewById(R.id.arrow_left_button);
 		arrow_right_button=(Button)findViewById(R.id.arrow_right_button);
 		confirm_button=(Button)findViewById(R.id.confirm_button);
 		store_button=(Button)findViewById(R.id.store_button);
 		undo_button=(Button)findViewById(R.id.undo_button);
 		mute_volume_button=(Button)findViewById(R.id.mute_volume_button);
 		info_button=(Button)findViewById(R.id.info_button);
 		exit_button=(Button)findViewById(R.id.exit_button);
 		
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
