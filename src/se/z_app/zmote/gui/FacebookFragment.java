package se.z_app.zmote.gui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.LoginButton;


//@TargetApi(9)
public class FacebookFragment extends Fragment {


	private LoginButton authButton;

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_facebook_login, container, false);

		authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setApplicationId(getString(R.string.app_id));

		return view;
	}
}
