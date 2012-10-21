package se.z_app.zmote.gui;

import se.z_app.stb.api.RCProxy;
import android.os.Bundle;
import android.app.Activity;
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
	 
	 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        btnListeners();
        
        
     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remote_control, menu);
        return true;
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
 		
 		 arrow_up_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().up();   
             }
         });
 		 
 		arrow_down_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().down();    
             }
         });
 		 
 		arrow_left_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().left(); 	 
             }
         });
 		 
 		arrow_right_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
           	 RCProxy.instance().right(); 
             }
         });
 		 
 		confirm_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
           	 RCProxy.instance().ok(); 
             }
         });
 		 
 		 store_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().menu();        
             }
         });
 		 
 		 undo_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().back();   
             }
         });
 		 
 		 mute_volume_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().mute();   
             }
         });
 		 
 		 info_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
             RCProxy.instance().info();    
             }
         });
 		
 		
 	
 	
 	}  
    


















}
