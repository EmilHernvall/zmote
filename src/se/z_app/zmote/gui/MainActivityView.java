package se.z_app.zmote.gui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
}
