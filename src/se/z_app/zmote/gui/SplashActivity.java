package se.z_app.zmote.gui;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	// At the start, we are going to use just some time, then we will use
	// other condition (modules load finish, for example.)
	// Set the display time, in milliseconds (or extract it out as a configurable parameter)
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // To do here
        // Access the BD
        // Get the operator logo
        // Set @id/opLogo to the image we get
        ImageView iView = (ImageView) findViewById(R.id.opLogo);
        iView.setImageResource(R.drawable.tele2);
        
        new Handler().postDelayed(new Runnable() {
        	
            
            public void run() {
            	
                //Finish the splash activity so it can't be returned to.
                SplashActivity.this.finish();
                // Create an Intent that will start the main activity.
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
