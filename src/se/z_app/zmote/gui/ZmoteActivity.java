package se.z_app.zmote.gui;

import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.RemoteControl.Button;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;

public abstract class ZmoteActivity extends Activity{
	Vibrator vibe; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE) ;
	}

	public void vibrate(){
		vibe.vibrate(95);
	}
	public void vibrate(int ms){
		vibe.vibrate(ms);
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
