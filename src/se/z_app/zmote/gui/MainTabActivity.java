package se.z_app.zmote.gui;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import se.z_app.stb.STB;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.RemoteControl.Button;
import android.app.ActionBar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * The main Fragment activity, extends the Sherlock library to support compability.
 * 
 * @author refrectored by: Linus Back
 * 
 */
public class MainTabActivity extends SherlockFragmentActivity implements TabListener{

	private com.actionbarsherlock.app.ActionBar actionBar;
	private Tab tabRC;
	private Tab tabMain;
	private Tab tabEPG;
	private Tab tabFav;
	private Tab tabWeb;
	private Spinner mySpinner;
	private ArrayList<String> STBNames;
	private Vibrator vibe;	
	private static Handler myHandler;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private RemoteControlFragment rcfragment;// = new RemoteControlFragment(this);
	private EPGFragment epgfragment;// = new EPGFragment(this);
	private WebTVFragment webfragment;// = new WebTVFragment(this);
	private MainViewFragment mainfragment;// = new MainViewFragment(this);
    private ChannelInformationFragment chinfragment;// = new ChannelInformationFragment(this);

	/**
	 * Standard create function for the fragment activity.
	 * Sets the layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;

		myHandler = new Handler(){
			public void handleMessage(Message mst){
				super.handleMessage(mst);
				setAlive(mst.what);
			}
		};
		new Thread(new MyTimedTask()).start();
	}

	/**
	 * Allows you to set the orientation of the screen from outside of the class
	 * @param i
	 */
    public void setOrientation(int i){
    		setRequestedOrientation(i);
    }

	/**
	 * Vibrates the phone for 95 milliseconds.
	 */
	public void vibrate(){
		vibe.vibrate(95);
	}
	
	/**
	 * vibrates the phone a number a number of milliseconds.
	 * @param ms number of milliseconds the the phone vibrates
	 */
	public void vibrate(int ms){
		vibe.vibrate(ms);
	}

	/**
	 * Restores the navigation state.
	 */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM) && (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)) {
        	getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

	/**
	 * Saves the navigation state.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
				getSupportActionBar().getSelectedNavigationIndex());
	}

	/**
	 * Creates and shows the Action bar, including the navigations tabs and the
	 * drop-down list.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		actionBar = getSupportActionBar();

		/*Calls the private function to create return the view containing the 
    	spinner*/
		View dropDownView = createDotAndDropDown();

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(dropDownView);


		actionBar.setLogo(R.drawable.green_button2);
		actionBar.setDisplayUseLogoEnabled(true);        
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		addTheNavigationTabs();

		return true;
	}




	/**
	 * Used to bind events to the physical volume buttons of the device.
	 * In this case, it increase and decreases the volume of the STB.
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				RemoteControl.instance().sendButton(Button.VOLPLUS);
				vibrate();
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				RemoteControl.instance().sendButton(Button.VOLMINUS);
				vibrate();
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

	
	/**
	 * Sets and displays the fragment that the user selects from the tabs.
	 * If new fragments are implemented they should be set here.
	 */
	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		Fragment fragment = null;
    	boolean isNew = false;
    	
    	if(tab.equals(tabRC)){
    		Log.i("FragmentLog", "RC");
    		if(rcfragment == null){
    			rcfragment = new RemoteControlFragment(this);
    			isNew = true;
    		}
    		fragment = rcfragment;
    		
    	}
    	else if(tab.equals(tabEPG)){
    		Log.i("FragmentLog", "EPG");
    		if(epgfragment == null){
    			epgfragment = new EPGFragment(this);
    			isNew = true;
    		}
    		fragment = epgfragment;
    	}
    	else if(tab.equals(tabWeb)){
    		Log.i("FragmentLog", "WebTV");
    		if(webfragment == null){
    			webfragment = new WebTVFragment(this);
    			isNew = true;
    		}
    		fragment = webfragment;
    		
    	}
		else if(tab.equals(tabFav)){
			Log.i("FragmentLog", "Fav");
			//WARNING! : provisional function
			if(chinfragment == null){
				chinfragment = new ChannelInformationFragment(this);
				isNew = true;
    		}
			fragment = chinfragment;
					
		}else if(tab.equals(tabMain)){
			Log.i("FragmentLog", "Main");
			if(mainfragment == null){
				mainfragment = new MainViewFragment(this);
				isNew = true;
			}
			fragment = mainfragment;
		}

    	
		if(fragment != null)
			if(isNew)
				getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
			else
				getSupportFragmentManager().beginTransaction().show(fragment).commit();
    	

	}


	/**
	 * Auto-generated method. Does nothing.
	 */
	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {

		Fragment fragment = null;
		if(tab.equals(tabRC)){
    		Log.i("Detaching FragmentLog", "RC");
    		fragment = rcfragment;
    	}
    	else if(tab.equals(tabEPG)){
    		Log.i("Detaching FragmentLog", "EPG");
    		fragment = epgfragment;
    	}
    	else if(tab.equals(tabWeb)){
    		Log.i("Detaching FragmentLog", "WebTV");
    		fragment = webfragment;
    	}
		else if(tab.equals(tabFav)){
			Log.i("Detaching FragmentLog", "Fav");
			//WARNING! : provisional function 
			fragment = chinfragment;
			
		}
		else if(tab.equals(tabMain)){
			Log.i("Detaching FragmentLog", "Main");
			fragment = mainfragment;
		}
    	
		if (fragment != null) {
	         //ft.detach(fragment);
			
	        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
	    }
    	 
		

	}


	/**
	 * Auto-generated method. Does nothing.
	 */
	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {


	}

	/**
	 * Adds all the navigation tabs to the action bar.
	 */
	private void addTheNavigationTabs() {
		// For each of the sections in the app, add a tab to the action bar.
		tabRC = actionBar.newTab().setIcon(R.drawable.ic_dialog_dialer);
		tabMain = actionBar.newTab().setIcon(R.drawable.ic_new_home);
		tabEPG = actionBar.newTab().setIcon(R.drawable.collections_go_to_today);
		tabFav = actionBar.newTab().setIcon(R.drawable.rating_favourite);
		tabWeb = actionBar.newTab().setIcon(R.drawable.location_web_site);

		// Add the tabs to the action bar
		actionBar.addTab(tabMain.setTabListener(this));
		actionBar.addTab(tabRC.setTabListener(this));
		actionBar.addTab(tabEPG.setTabListener(this));
		actionBar.addTab(tabWeb.setTabListener(this));
		actionBar.addTab(tabFav.setTabListener(this));

	}


	/**
	 * Creates the spinner for the drop down menu.
	 * @return
	 */
	private View createDotAndDropDown(){

		View myView = getLayoutInflater().inflate(
				R.layout.activity_main_tab_actionbar, null);
		mySpinner = (Spinner) myView.findViewById(R.id.action_bar_spinner);

		/*gets all the boxes from the STB container. And saves the index of the
    	 selected one so that the spinners defoult value is correct.*/
		Iterator<STB> iterator = STBContainer.instance().iterator();
		STBNames = new ArrayList<String>();
		int temp = 0;
		int selected = 0;
		STB stb;
		
		while(iterator.hasNext()){ 		
			if(STBContainer.instance().getActiveSTB().equals(stb = iterator.next())){
				selected = temp;
			}
			STBNames.add(stb.getBoxName());
			temp++;
		}

		//Adds the Edit button to the Spinner
		STBNames.add("Edit...");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, STBNames);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.
				simple_spinner_dropdown_item);
		// Apply the adapter to the spinner, also aplies a listner.
		mySpinner.setAdapter(adapter);
		mySpinner.setSelection(selected);

		mySpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		return myView;				
	}

	public void setAlive(int isAlive){

		if(actionBar == null) //TODO need to fixed, added so it won't crash due to other changes //Emma
			return;
		if(isAlive==1){
			actionBar.setLogo(R.drawable.green_button2);
		}else{
			actionBar.setLogo(R.drawable.red_dot);
		}
	}

	/**
	 * Private class that implements OnItemSelectedListener. The reason for this
	 * is that i got some sort of conflict when i tried to implement this 
	 * interface in the mainTabActivity class, possible because of the Sherlock 
	 * library.
	 * 
	 * There might be a better solution for this, but this works right now.
	 * @author Linus Back (linba708)
	 *
	 */
	private class MyOnItemSelectedListener implements OnItemSelectedListener{

		/**
		 * Sets the active STB in the STB container based on what the user 
		 * selects in the drop down menu.
		 */
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//Checks if the chosen item is the edit one.
			if(arg2==arg0.getAdapter().getCount()-1){
				Intent mainIntent = new Intent(MainTabActivity.this,
						SelectSTBActivity.class); 
				MainTabActivity.this.startActivity(mainIntent);
			}
			else{
				STBContainer.instance().setActiveSTB(STBContainer.instance().getSTBs()[arg2]);
			}
		}

		/**
		 * Autogenerated function. Does nothing right now.
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {


		}


	}
	private class MyTimedTask implements Runnable{

		int timeout= 500;
		int timer = 100;
		boolean boxactive = false;
		boolean newBoxactive = false;


		@Override
		public void run() {
			while(true){
				try {
					newBoxactive = Inet4Address.getByName(STBContainer.instance().getActiveSTB().getIP()).isReachable(timeout);
					
					if(!newBoxactive)
						newBoxactive = Inet4Address.getByName(STBContainer.instance().getActiveSTB().getIP()).isReachable(timeout);
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(boxactive == newBoxactive){

				}
				else if(newBoxactive){		
					myHandler.sendEmptyMessage(1);
					boxactive = newBoxactive;
				}
				else{
					myHandler.sendEmptyMessage(0);
					boxactive = newBoxactive;
				}
				try {
					Thread.sleep(timer);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}


}
