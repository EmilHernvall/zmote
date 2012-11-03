package se.z_app.zmote.gui;

import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.RemoteControl.Button;
import android.app.ActionBar;
import android.app.ActionBar.Tab;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainTabActivity extends FragmentActivity implements ActionBar.TabListener {

	private Tab tabRC;
	private Tab tabMain;
	private Tab tabEPG;
	private Tab tabFav;
	private Tab tabWeb;
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
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_tab, menu);
     // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
        tabRC = actionBar.newTab().setIcon(R.drawable.ic_dialog_dialer);
        tabMain = actionBar.newTab().setIcon(R.drawable.av_play);
        tabEPG = actionBar.newTab().setIcon(R.drawable.collections_go_to_today);
        tabFav = actionBar.newTab().setIcon(R.drawable.rating_favourite);
        tabWeb = actionBar.newTab().setIcon(R.drawable.location_web_site);
        
        actionBar.addTab(tabRC.setTabListener(this));
        actionBar.addTab(tabEPG.setTabListener(this));
        actionBar.addTab(tabMain.setTabListener(this));
        actionBar.addTab(tabWeb.setTabListener(this));
        actionBar.addTab(tabFav.setTabListener(this));
        
        
        
        return true;
    }

    

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        
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
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    

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


}
