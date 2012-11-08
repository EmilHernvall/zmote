package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.STBDiscovery;


import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;


/**
 * The STB Activity screen. Here is the main screen where the STB list is shown as well as the find STB button.
 * @author viktordahl + others + mariaplatero (AddSTB)
 *
 * The screen where the STB list is shown as well as the find STB button.
 * 
 * @author Viktor Dahl, + others
 * 
 */
public class SelectSTBActivity extends Activity {
    private SelectSTBListView theView;
    private STB new_stb;
    private ASyncSTBFinder async;
    private ProgressDialog dialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stb);
        
        Button scan = (Button) findViewById(R.id.button_scanforstb); 	//Scan for STB button
        Button add = (Button) findViewById(R.id.button_addstb); 		//Add manually button
        
        theView = (SelectSTBListView)findViewById(R.id.list_over_stb);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	async = new ASyncSTBFinder();
            	async.execute();	
            }
        });
        
        theView = (SelectSTBListView)findViewById(R.id.list_over_stb);
        add.setOnClickListener(new View.OnClickListener() {	
        @Override
			public void onClick(View v) {
				setContentView(R.layout.activity_add_stb);
				SelectSTBActivity.this.addSTB();
        	}
        });
	
        theView.setList(this, STBContainer.instance().getSTBs());
		
		
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);


}
    
        	

    
    /**
     * Updates the STB list with an STB array
     * @param theList
     */
     private void updateList(STB[] theList) {
 		
 		for(int i = 0; i <  theList.length; i++) {
 			STBContainer.instance().addSTB(theList[i]);
 		}
 		theView.setList(this, STBContainer.instance().getSTBs());
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

    
     @Override
     public void onResume() {
     	if(theView != null)
     		theView.notifyAdapter();
     	super.onResume();
     }
	


	/**
	 * Calls the STBDiscovery.find() for searching after STB's in an async task.
	 * TODO: Add a message when no STB's are found.
	 * 
	 * @return Array of STB's
	 */
	private class ASyncSTBFinder extends AsyncTask<Integer, Integer, STB[]> {

		@Override
		protected STB[] doInBackground(Integer... params) {
			STBDiscovery stbDisc = new STBDiscovery(findSubnetAddress());
			//STBDiscovery stbDisc = new STBDiscovery("130.236.248."); // -- Test to scan our test boxes
			return stbDisc.find();
		}

		protected void onPreExecute() {
			System.out.println("Scan started.");
			dialog.setMessage("Scanning for new STB's in network");
			dialog.show();
		}

		protected void onPostExecute(STB[] stb) {
			System.out.print("Vicks: Number of STB's:"+stb.length+". Boxnames: ");
			for (STB box : stb) {
				System.out.print(box.getBoxName()+" ,");
			}
			System.out.print("\n\n");
			
			try {	updateList(stb);
				} catch (Exception e) { e.printStackTrace(); }
			
			dialog.dismiss();
			System.out.println("Scan finished.");
		}

		/**
		 * Finds the subnet of the devices network
		 * 
		 * @return the ip in a string in the form 192.168.0. (with the last
		 *         "."!)
		 */
		private String findSubnetAddress() {
				WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		    	int ipAddress = myWifiInfo.getIpAddress();
		    	
		    	@SuppressWarnings("deprecation")
				String str = android.text.format.Formatter.formatIpAddress(ipAddress);
		    	return str.substring(0, str.lastIndexOf('.')+1);   	
		}
    }
	
    
    /**
     * View and function to add STB manually with name and IP
     * 
     */
    private void addSTB(){
    	
    
     /*Save Button*/
    /*
     * TODO
     * Do we need to check if the STB is valid ?
     * Do we need to check for empty fields?
     * Jump from one field to another in the IP when one is fulfilled 
     * with 3 digits
     * */
   	 Button savebutton = (Button) findViewById(R.id.activity_add_stb_save_button); 
		 savebutton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					EditText e = (EditText) findViewById(R.id.activity_add_stb_editNameid);
					
					String n = e.getText().toString();
				 	
					new_stb = new STB();
				  	new_stb.setBoxName(n);
					
				  	/*We get the IP in 4 pieces and we put it all together to set the STB*/
					EditText ip1 = (EditText) findViewById(R.id.Add_stb_IP1);
					EditText ip2 = (EditText) findViewById(R.id.Add_stb_IP2);		
					EditText ip3 = (EditText) findViewById(R.id.Add_stb_IP3);
					EditText ip4 = (EditText) findViewById(R.id.Add_stb_IP4);
			
					String temp1 = ip1.getText().toString();
					String temp2 = ip2.getText().toString();
					String temp3 = ip3.getText().toString();
					String temp4 = ip4.getText().toString();
					String final_ip = temp1+"."+temp2+"."+temp3+"."+temp4;
								
					new_stb.setIP(final_ip);
					
					/*
					 * TODO 
					 * Needs to provide a choice of type for the user. 
					 * A suggestion is to implement a drop down list (spinner).
					 * That gets all the STB enums and just choices a BOX.
					 */
					new_stb.setType(STB.STBEnum.ZENTERIO);
					
					STB tempStb[] = new STB[1];
					tempStb[0] = new_stb;
					updateList(tempStb);
					
				 	
					Intent intent = new Intent(SelectSTBActivity.this, SelectSTBActivity.class);
					SelectSTBActivity.this.startActivity(intent);	
				}	
			});
		
		 	/*Cancel Button*/
		 	Button cancelButton = (Button) findViewById(R.id.activity_add_stb_cancel_button);
			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(SelectSTBActivity.this, SelectSTBActivity.class);
					SelectSTBActivity.this.startActivity(intent);
				}
			});
    }

}
