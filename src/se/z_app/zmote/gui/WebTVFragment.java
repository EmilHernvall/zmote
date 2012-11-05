package se.z_app.zmote.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class WebTVFragment extends Fragment {
	
    private LinearLayout content_layout;
	private View view_temp;
	private MainTabActivity main;
	private String web_service;  // To know in what service are we currently (youtube, spotify...)
	private ProgressBar pb;

	
	
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
	
	/**
	 * Calls the back-end function to get the results of a search and shows them
	 */
	public void search(){
		
		// We can set a progress bar to show the user that we are searching
		 pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		EditText search_box = (EditText)view_temp.findViewById(R.id.search_box_webtv);
		String search_for_this = search_box.getText().toString();
		
		// Here we should call a function like this
		// ArrayList<Result> getResultsWebSearch(String searchForThis, String web_service);
		
		// After getting the results
		// pb.setVisibility(View.GONE);	// Quit the progress bar
		// int type = decideType(web_service);
		// showResults();
	}
	
	/**
	 * Print the results of the search on the screen
	 * @param res Set of results to show
	 * @param type Type of the results: 1-video, 2-Image, 3-Song
	 */
	/*public void showResults(ArrayList<Result> res, int type){
		// Types: 1 - video, 2-Image, 3-Song
		
		
	}*/
	
	/**
	 * Decides the type of the result looking at the service we are looking at
	 * @param web_service	The web service set at the moment
	 * @return The type of result we should show
	 */
	public int decideType(String web_service){
		int type = 0;
		if(web_service.compareTo("Youtube") == 0){
			type = 1;
		}else if(web_service.compareTo("Spotify") == 0){
			type = 3;
		}else if(web_service.compareTo("TED") == 0){
			type = 1;
		}
		
		return type;
	}
	
}
