package se.z_app.zmote.gui;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.zmote.epg.EPGQuery;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EPGActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epg);
        
        
        
     /*   EPGQuery q = new EPGQuery();
        EPG epg = q.getEPG();
        
        for (Channel channel : epg) {
        	channel.getIcon()
        	
			for (Program program : channel) {
			program.ge	
			}
		}*/
    }

    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_epg, menu);
        return true;
    }
}
