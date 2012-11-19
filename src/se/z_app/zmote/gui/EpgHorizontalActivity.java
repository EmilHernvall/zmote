package se.z_app.zmote.gui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EpgHorizontalActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_epg_horizontal);
        setContentView(R.layout.fragment_epg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_epg_horizontal, menu);
        return true;
    }
}
