package se.z_app.zmote.gui;


import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGContentHandler;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;


/**
 * First screen loaded with the current STB logo and loading animation
 * @author Francisco
 *
 */
public class SplashActivity extends Activity {
	/* TODO
	 * Right now we're using some established time
	 * We should figure out when to leave the screen
	 *  (modules load finish, for example.)
	 * Set the display time, in milliseconds (or extract it out as a configurable parameter) */
	
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	EPGContentHandler.setContext(this.getApplicationContext());
        EPGContentHandler.instance();
        setContentView(R.layout.activity_splash);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash, menu);
        return true;
    }
 
    @Override
    protected void onResume() {
        super.onResume();

        /* TODO here:
         	1- Access the BD
        	2- Get the operator logo
         	3- Set @id/opLogo to the image we get    */
        
        ImageView iView = (ImageView) findViewById(R.id.opLogo);
        iView.setImageResource(R.drawable.tele2);
        
        new Handler().postDelayed(new Runnable() {
        	
            
            public void run() {
				RemoteControl.instance();
               
				//Finish the splash activity so it can't be returned to.
                SplashActivity.this.finish(); 		
              
                // Create an Intent that will start the main activity.
                Intent mainIntent = new Intent(SplashActivity.this, 
                								SelectSTBActivity.class); 
                
                SplashActivity.this.startActivity(mainIntent);
                
            }
            
        }, SPLASH_DISPLAY_LENGTH);
        
    }
    
    
    
}
