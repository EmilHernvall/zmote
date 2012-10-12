package se.z_app.zmote.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;
import se.z_app.stb.api.STBDiscovery;
import se.z_app.stb.api.zenterio.Discovery;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;


/* 
 * TODO: Fix everything. Like findSubnet. It's shit right now // Viktor D
 */
public class SelectSTBActivity extends Activity {
	Discovery Disc = new Discovery();
    STB[] stbs;
    STBDiscovery stbDisc = new STBDiscovery();
    LinkedList<InetAddress> boxes = new LinkedList<InetAddress>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stb);
        
        /*
         * A button for scanning for STB's
         */
        Button next = (Button) findViewById(R.id.button_scanforstb);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	System.out.println("Button presdsed.");
            	stbs = Disc.find();
            	
            	}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_stb, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
