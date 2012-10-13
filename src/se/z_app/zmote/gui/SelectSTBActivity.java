package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;


/* 
 * Whoop.
 */
public class SelectSTBActivity extends Activity {
    private Discovery disc;
    private STBListView theView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stb);
        Button scan = (Button) findViewById(R.id.button_scanforstb);
        theView = (STBListView)findViewById(R.id.list_over_stb);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            		disc = new Discovery();
            		Log.e("Output", ""+disc.find().length);
            		STB[] List = new STB[2];
            		List[0] = new STB();
            		List[0].setBoxName("box1");
            		List[1] = new STB();
            		List[1].setBoxName("box2");
            		updateList(List);
//            	}
            }
        });
    }
    
    public void updateList(STB[] theList) {
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
}
