package se.z_app.zmote.gui;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ImageView;

public class MainActivityView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_view, menu);
        return true;
    }
    
    /* Version 0.1
     * Add a channel to the view. The information we need to give
     * to this function is:
     * - Icon image
     * - Identifier
     * - EPG info
     * 
     * Right now it only adds the icon to the top list */
    public void addChannel(int id, Drawable dr){
    	
    	LinearLayout mainView = (LinearLayout) findViewById(R.id.channel_icons_ly);
    	ImageView ch_btn = new ImageView(this);
    	ch_btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
    			ViewGroup.LayoutParams.WRAP_CONTENT));
    	ch_btn.setImageDrawable(dr);
    	ch_btn.setId(id);
    	mainView.addView(ch_btn);

    }
    
    
}
