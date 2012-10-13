package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.support.v4.app.NavUtils;


/* 
 * Whoop.
 */
public class SelectSTBActivity extends Activity {
    private Discovery disc;
    private String ipaddress;
    public STB[] stbs;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stb);
        Button scan = (Button) findViewById(R.id.button_scanforstb);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar_scan);
            	System.out.println(progressBar);
            	ipaddress = findSubnet();
            	disc = new Discovery(ipaddress, stbs, view, progressBar);
            	disc.execute();
            }
        });
        Button show = (Button) findViewById(R.id.button_showstb);
        show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	System.out.println(disc.getStatus());
            }
        });
    }
    
    public void runOPE() {
    	System.out.println("Hej");
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
    
	/*
	 * Finds the subnet of the devices network and returns a string in the form 192.168.0.
	 * TODO: Should be in STBDiscovery.java but I dunno how.
	 */
	public String findSubnet() {
			WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
	    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
	    	int ipAddress = myWifiInfo.getIpAddress();
	    	System.out.println(ipAddress);
	    	String str = android.text.format.Formatter.formatIpAddress(ipAddress);
	    	return str.substring(0, str.lastIndexOf('.')+1);
	    	
	}
//		return "130.236.248.";
}
