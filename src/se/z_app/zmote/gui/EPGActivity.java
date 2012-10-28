package se.z_app.zmote.gui;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.zmote.epg.EPGQuery;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EPGActivity extends Activity {
	private EPGQuery q = new EPGQuery();
    private EPG epg = q.getEPG();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epg);
        mainEPG();
        
        
    
    }

    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_epg, menu);
        return true;
    }


void mainEPG(){
	
	for (Channel channel : epg) {
    	addIconToLayout(channel);
    	for (Program program : channel) {
			addProgramsToLayout(program);	
			}
	
     }	
}

void addIconToLayout(Channel ch){
	
	
	
}

void addProgramsToLayout(Program pg){



}






}