package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.Iterator;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;


import se.z_app.stb.STB;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.STBContainer;
import se.z_app.stb.api.RemoteControl.Button;
import android.app.ActionBar;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



public class MainTabActivity extends SherlockFragmentActivity implements TabListener {

	private com.actionbarsherlock.app.ActionBar actionBar;
	private Tab tabRC;
	private Tab tabMain;
	private Tab tabEPG;
	private Tab tabFav;
	private Tab tabWeb;
	private Spinner mySpinner;
	private ArrayList<String> STBNames;
	private Vibrator vibe;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;
	}

	
	
	public void vibrate(){
		vibe.vibrate(95);
	}
	public void vibrate(int ms){
		vibe.vibrate(ms);
	}

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
        	getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
        		getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	actionBar = getSupportActionBar();




    	View myView = getLayoutInflater().inflate(R.layout.activity_main_tab_actionbar, null);
    	mySpinner = (Spinner) myView.findViewById(R.id.action_bar_spinner);

    	

    	Iterator<STB> iterator = STBContainer.instance().iterator();
    	STBNames = new ArrayList<String>();
    	
    	while(iterator.hasNext()){
    		STBNames.add(iterator.next().getBoxName());
    	}
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STBNames);

    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
		mySpinner.setAdapter(adapter);
		

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(myView);
        
        
        actionBar.setLogo(R.drawable.green_button);
        actionBar.setDisplayUseLogoEnabled(true);        
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
        tabRC = actionBar.newTab().setIcon(R.drawable.ic_dialog_dialer);
        tabMain = actionBar.newTab().setIcon(R.drawable.av_play);
        tabEPG = actionBar.newTab().setIcon(R.drawable.collections_go_to_today);
        tabFav = actionBar.newTab().setIcon(R.drawable.rating_favourite);
        tabWeb = actionBar.newTab().setIcon(R.drawable.location_web_site);
        
        // Add the tabs to the action bar
        actionBar.addTab(tabRC.setTabListener(this));
        actionBar.addTab(tabEPG.setTabListener(this));
        actionBar.addTab(tabMain.setTabListener(this));
        actionBar.addTab(tabWeb.setTabListener(this));
        actionBar.addTab(tabFav.setTabListener(this));
        
        return true;
    }

  
    /**
     * Used to bind events to the physical volum buttons of the device.
     * In this case, it increase and decreases the volum of the STB.
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



	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
  	Fragment fragment = null;
    	
    	if(tab.equals(tabRC)){
    		Log.i("FragmentLog", "RC");
    		fragment = new RemoteControlFragment(this);
    		
    	}
    	else if(tab.equals(tabEPG)){
    		Log.i("FragmentLog", "EPG");
    		fragment = new EPGFragment(this);
    	}
    	else if(tab.equals(tabWeb)){
    		Log.i("FragmentLog", "Web");
    		return;
    	}
		else if(tab.equals(tabFav)){
			Log.i("FragmentLog", "Fav");
			//WARNING! : provisional function 
			fragment = new ChannelInformationFragment(this);
			
		}
		else if(tab.equals(tabMain)){
			Log.i("FragmentLog", "Main");
			fragment = new MainViewFragment(this);//new ChannelInformationFragment(this);
		}
    	
        
        Bundle args = new Bundle();
        
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
		
	}



	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


}
