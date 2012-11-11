package se.z_app.zmote.gui;


import java.util.ArrayList;
import java.util.List;


import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.zmote.webtv.WebTVQuery;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Class used to display webTV and search on the different webTV items 
 * @author Francisco Valladres, Maria Jesus Platero, Emma Axelsson  
 *
 */
public class WebTVFragment extends Fragment {
	
	private View view_temp;
	private MainTabActivity main;
	private int web_service = 0;  // To know in what service are we currently (youtube, spotify...)
	private ProgressBar pb;
	private WebTVService services[];
	private String search_for_this = null;	
	public WebTVFragment(){
		
	}
	
	public WebTVFragment(MainTabActivity mainTabActivity) {
		this.main = main;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		view_temp = inflater.inflate(R.layout.fragment_web_tv, null);
		new AsyncWebServiceLoader().execute();
		
		// Set the listener for the search button
		ImageButton search_button = (ImageButton)view_temp.findViewById(R.id.search_button_webtv);
		search_button.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// Start a new search
				search();
				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.GONE);
				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.VISIBLE);
				LinearLayout linLayTopList = (LinearLayout) view_temp.findViewById(R.id.top_list);
				linLayTopList.setVisibility(View.GONE);
			}
		});
		
		
		ImageButton search_button_back = (ImageButton)view_temp.findViewById(R.id.search_button_webtv_result);
		search_button_back.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// Go back to first view
				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.VISIBLE);
				LinearLayout linLayTopList = (LinearLayout) view_temp.findViewById(R.id.top_list);
				linLayTopList.setVisibility(View.VISIBLE);
				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.GONE);
				
			}
		});
		
		LinearLayout linLayStart = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
		linLayStart.setVisibility(View.GONE);
		
    	return view_temp;
    }    
	
	/**
	 * Calls the back-end function to get the results of a search and shows them
	 */
	public void search(){
		
		
		// We can set a progress bar to show the user that we are searching
		pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		
		EditText search_box = (EditText)view_temp.findViewById(R.id.search_box_webtv);
		
		search_for_this = search_box.getText().toString();
		
		TextView resultText = (TextView) view_temp.findViewById(R.id.result_webtv);
		resultText.setText("Result for: '"+ search_for_this+"'");
		// Here we should call a function like this
		if(search_for_this != null)
			new AsyncWebSearch().execute();
		

		// After getting the results
		// pb.setVisibility(View.GONE);	// Quit the progress bar		
	}
	
	/**
	 * Print the results of the search on the screen
	 * @param res Set of results to show
	 */
	public void showResults(WebTVItem[] res){
		
		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		results_ly.removeAllViewsInLayout(); 
		LinearLayout.LayoutParams item_container_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		item_container_params.setMargins(4, 4, 4, 0);
		LinearLayout.LayoutParams item_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams icon_params = new LinearLayout.LayoutParams(100,80);

		for(WebTVItem x: res){
			LinearLayout item_container = new LinearLayout(view_temp.getContext());
			item_container.setBackgroundColor(0xFFDDDDDD);
			item_container.setPadding(4, 4, 4, 4);
			LinearLayout item = new LinearLayout(view_temp.getContext());
			item.setPadding(4, 4, 4, 4);
			item.setBackgroundColor(0xFFC0C0C0);
			item.setMinimumHeight(30);
			ImageView icon = new ImageView(view_temp.getContext());
			icon.setImageBitmap(x.getIcon());
			TextView title = new TextView(view_temp.getContext());
			title.setText(x.getTitle());
			title.setPadding(10, 0, 0, 0);
			title.setTextColor(0xFF000000);
			
			item.addView(icon, icon_params);
			item.addView(title);
			item_container.addView(item, item_params);
			results_ly.addView(item_container, item_container_params);
		}	
		
	}
	
	/* add items into spinner (drop-down menu with services) dynamically*/
	public void addItemsOnSpinner(WebTVService services[]) {
		 
			List<Bitmap> list = new ArrayList<Bitmap>();
			
		   	
		   	for(WebTVService serv : services){
		   		list.add(serv.getIcon());
		   	}

		   	Bitmap servicesImg[] = new Bitmap[list.size()];
		   	list.toArray(servicesImg);

			ImageAdapter ia = new ImageAdapter(this.getActivity(), android.R.layout.simple_spinner_item, servicesImg);	
			Spinner spinner = (Spinner)view_temp.findViewById(R.id.webtv_spinner); // this returns null
			ia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(ia);
			
		  }
	
	
	public class ImageAdapter extends ArrayAdapter<Bitmap>{
		
		Bitmap[] services;
        public ImageAdapter(Context context, int textViewResourceId, Bitmap[] services) {
            super(context, textViewResourceId, services);
            this.services = services;
        }
 
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
 
        public View getCustomView(int position, View convertView, ViewGroup parent) {
 
    		ImageView icon = new ImageView(view_temp.getContext());
    		icon.setImageBitmap(services[position]);
    		// Set the same image for the results image
    		if(position == web_service){
	    		ImageView result_icon = (ImageView) view_temp.findViewById(R.id.webtv_icon_result);
	    		result_icon.setImageBitmap(services[position]);
    		}
 
            return icon;
            }
        }

	private class AsyncWebServiceLoader extends AsyncTask<Integer, Integer, WebTVService[]>{

		@Override
		protected WebTVService[] doInBackground(Integer... params) {
			WebTVQuery query = new WebTVQuery();
			services = query.getService();
			query.populateWithIcon(services);
			return services;
		}

		@Override
		protected void onPostExecute(WebTVService services[]) {
			addItemsOnSpinner(services);
		}
	}
	
	/**
	 * Makes a search asynchronously to avoid failure of the execution in newer versions
	 * of android
	 * @author Francisco Valladares
	 *
	 */
	private class AsyncWebSearch extends AsyncTask<Integer, Integer, WebTVItem[]>{

		@Override
		protected WebTVItem[] doInBackground(Integer... arg0) {
			
			WebTVQuery query = new WebTVQuery();
			WebTVItem[] elements= query.search(search_for_this, services[0]);
			query.populateWebTVItemsWithIcon(elements);
			//System.out.println(services[0].getName().toString());
			//System.out.println(elements[0].getTitle().toString());
			return elements;
		}
		
		@Override
		protected void onPostExecute(WebTVItem elements[]) {
			showResults(elements);
		}
		
	}
	
}
