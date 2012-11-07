package se.z_app.zmote.gui;


import java.util.ArrayList;
import java.util.List;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.zmote.epg.EPGQuery;
import se.z_app.zmote.webtv.WebTVQuery;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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


public class WebTVFragment extends Fragment {
	
    private LinearLayout content_layout;
	private View view_temp;
	private MainTabActivity main;
	private String web_service;  // To know in what service are we currently (youtube, spotify...)
	private ProgressBar pb;
	private Spinner spinner;
	private ArrayAdapter<Drawable> dataAdapter;
	
	
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
		
		new AsyncWebServiceLoader().execute();
		
		// Set the listener for the search button
		ImageButton search_button = (ImageButton)view_temp.findViewById(R.id.search_button_webtv);
		search_button.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// Start a new search
				search();
			}
		});
		
		// pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		// new AsyncDataLoader().execute();
	
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
		WebTVQuery query = new WebTVQuery();
		WebTVService service[] = query.getService();
		
		//System.out.println(service[0].getName().toString());
		//System.out.println(query.getService().toString());
		WebTVItem[] elements= query.search(search_for_this, service[0]);
		//System.out.println(elements[0].getTitle().toString());
		
		// After getting the results
		// pb.setVisibility(View.GONE);	// Quit the progress bar
		showResults(elements);
		
	}
	
	/**
	 * Print the results of the search on the screen
	 * @param res Set of results to show
	 */
	public void showResults(WebTVItem[] res){
		
		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		LinearLayout.LayoutParams item_container_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		item_container_params.setMargins(4, 4, 4, 0);
		LinearLayout.LayoutParams item_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		for(WebTVItem x: res){
			LinearLayout item_container = new LinearLayout(view_temp.getContext());
			item_container.setBackgroundColor(0xFF999999);
			item_container.setPadding(4, 4, 4, 4);
			LinearLayout item = new LinearLayout(view_temp.getContext());
			item.setPadding(4, 4, 4, 4);
			item.setBackgroundColor(0xFF666666);
			item.setMinimumHeight(30);
			ImageView icon = new ImageView(view_temp.getContext());
			icon.setImageBitmap(x.getIcon());
			TextView title = new TextView(view_temp.getContext());
			title.setText(x.getTitle());
			title.setTextColor(0xFF000000);
			
			item.addView(icon);
			item.addView(title);
			item_container.addView(item, item_params);
			results_ly.addView(item_container, item_container_params);
		}	
		
	}
	
	/* add items into spinner (drop-down menu with services) dynamically*/
	public void addItemsOnSpinner(WebTVService services[]) {
		 
			spinner = (Spinner)view_temp.findViewById(R.id.webtv_spinner);
			List<Bitmap> list = new ArrayList<Bitmap>();
			
		   	
		   	for(WebTVService serv : services){
		   		list.add(serv.getIcon());
		   	}
		 
		
		   	Bitmap servicesImg[] = new Bitmap[list.size()];
		   	list.toArray(servicesImg);

			ImageAdapter ia = new ImageAdapter(this.getActivity(), android.R.layout.simple_spinner_item, servicesImg);	
			Spinner spinner = (Spinner)view_temp.findViewById(R.id.webtv_spinner); // this returns null
		
			 //dataAdapter = new ArrayAdapter<Drawable>(this.getActivity(), android.R.layout.simple_spinner_item, list);
			
			//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
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
    		
 
            return icon;
            }
        }

	private class AsyncWebServiceLoader extends AsyncTask<Integer, Integer, WebTVService[]>{

		@Override
		protected WebTVService[] doInBackground(Integer... params) {
			WebTVQuery query = new WebTVQuery();
			WebTVService services[] = query.getService();
			query.populateWithIcon(services);
			return services;
		}

		@Override
		protected void onPostExecute(WebTVService services[]) {
			addItemsOnSpinner(services);
		}
	}
	
}
