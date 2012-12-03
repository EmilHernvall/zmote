package se.z_app.zmote.gui;


import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;


/**
 * First screen loaded with the current STB logo and loading animation
 * @author Francisco Valladares
 *
 */
public class SplashActivity extends Activity {
	
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new Bootstrap(this.getApplicationContext(), (WifiManager) getSystemService(WIFI_SERVICE));
        
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
        
        ImageView iView = (ImageView) findViewById(R.id.opLogo);
        iView.setImageResource(R.drawable.tele2);
        
        new Handler().postDelayed(new Runnable() {
        	       
           public void run() {
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
