package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;


/* 
 * The STB selection view
 */
public class SelectSTBActivity extends Activity {
    private STBListView theView;
    private STB[] stbs;
    private ASyncSTBFinder async;
    private ProgressDialog dialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stb);
        Button scan = (Button) findViewById(R.id.button_scanforstb);
        theView = (STBListView)findViewById(R.id.list_over_stb);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	async = new ASyncSTBFinder();
            	async.execute();	
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

    }
    
    /* Updates the list with an STB array */
    private void updateList(STB[] theList) {
		theView.setList(this, theList);
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
	 * 
	 * TODO: Add a message when no STB's are found.
	 */
    private class ASyncSTBFinder extends AsyncTask<Integer,Integer,STB[]> {
    	private Discovery disc;
    	
		@Override
		protected STB[] doInBackground(Integer... params) {
        	disc = new Discovery(findSubnetAddress());
			return disc.find();
		}
		protected void onPreExecute() {
			System.out.println("Scan started.");
			
			dialog.setMessage("Scanning for new STB's in network");
			dialog.show();
		}
		protected void onPostExecute(STB[] stb) {
			try {
				stbs = stb;
				updateList(stbs);
			} catch (Exception e) { e.printStackTrace(); }
			dialog.dismiss();
			System.out.println("Scan finished.");
		}
		/*
		 * Finds the subnet of the devices network and returns a string in the form 192.168.0.
		 * TODO:
		 */
		private String findSubnetAddress() {
				WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		    	int ipAddress = myWifiInfo.getIpAddress();
		    	String str = android.text.format.Formatter.formatIpAddress(ipAddress);
		    	return str.substring(0, str.lastIndexOf('.')+1);   	
		}
    }
}
