package se.z_app.zmote.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class WebTVFragment extends Fragment {
	
    private LinearLayout content_layout;
	private View view_temp;
	private MainTabActivity main;

	
	
	public WebTVFragment(){
		
	}
	
	public WebTVFragment(MainTabActivity mainTabActivity) {
		this.main = main;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view_temp = inflater.inflate(R.layout.fragment_web_tv, null);
		content_layout = (LinearLayout)view_temp.findViewById(R.id.content_ly);
		
		
	//	pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		
	//	new AsyncDataLoader().execute();
	
    	return view_temp;
    }    
	
	
}
