package se.z_app.zmote.gui;

import se.z_app.stb.STB;

import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity that handles re-naming STBs
 * 
 * @author Christian Vestman, Marcus Widegren
 */
public class EditSTBActivity extends Activity {

	//TODO add private/public attributes!!!!!!!!!!!!!!!!!!!!!!
	STB stb;
	int theIndex;

	/**
	 * Method that sets the modified
	 * STB name
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		theIndex = (Integer) getIntent().getSerializableExtra("index");
		stb = SelectSTBList.instance().getList().get(theIndex);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_stb);
		TextView tv;
		
		if (stb.getBoxName() != null) {
			tv = (TextView) findViewById(R.id.activity_edit_stb_editNameid);
			tv.setText(stb.getBoxName());
		}
		if (stb.getMAC() != null) {
			tv = (TextView) findViewById(R.id.activity_edit_stb_dynamicMACid);
			tv.setText(stb.getMAC());
		}
		if (stb.getIP() != null) {
			tv = (TextView) findViewById(R.id.activity_edit_stb_dynamicIPid);
			tv.setText(stb.getIP());
		}
		if (stb.getType() != null) {
			tv = (TextView) findViewById(R.id.activity_edit_stb_dynamicTypeid);
			tv.setText(stb.getType().toString());
		}
		
		Button saveButton = (Button) findViewById(R.id.activity_edit_stb_save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText e = (EditText) findViewById(R.id.activity_edit_stb_editNameid);
				String n = e.getText().toString();
				stb.setBoxName(n);
				EditSTBActivity.this.finish();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.activity_edit_stb_cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditSTBActivity.this.finish();
			}
		});
	}

	//TODO add method description
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_stb, menu);
		return true;
	}

}
