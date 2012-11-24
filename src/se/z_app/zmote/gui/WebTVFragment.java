package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.List;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.zmote.webtv.WebTVQuery;
import android.R.drawable;
import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import se.z_app.stb.api.*;

/**
 * Class used to display webTV and search on the different webTV items 
 * @author Francisco Valladres, Maria Jesus Platero, Emma Axelsson, Ralf Nilsson
 */
public class WebTVFragment extends Fragment {
	private Bitmap[] servicesIcons;
	private Spinner spinner;
	private View view_temp;
	private MainTabActivity main;
	private int web_service = 0;  // To know in what service are we currently (youtube, spotify...)
	private ProgressBar pb;
	private WebTVService services[];
	private String search_for_this = null;	
	private float screenWidth = 0;
	private WebTVItem tempItem;
	public WebTVFragment(){

	}

	public WebTVFragment(MainTabActivity mainTabActivity) {
		/*
		 * @Leonard: Changed the function this.main = main; it didn't do anything
		 */
		this.main = mainTabActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	

		view_temp = inflater.inflate(R.layout.fragment_web_tv, null);
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		new AsyncWebServiceLoader().execute();

		// Set the listener for the search button
		ImageButton search_button = (ImageButton)view_temp.findViewById(R.id.search_button_webtv);
		search_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start a new search
				search();
				//Loads the correct webTv icon
				web_service = spinner.getSelectedItemPosition();
				ImageView result_icon = (ImageView) view_temp.findViewById(R.id.webtv_icon_result);
				result_icon.setImageBitmap(servicesIcons[web_service]);

				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.GONE);
				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.VISIBLE);			
				LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
				noSearch.setVisibility(View.GONE);

				//TODO set view when noSearch string is entered

				//	LinearLayout linLayTopList = (LinearLayout) view_temp.findViewById(R.id.top_list);
				//	linLayTopList.setVisibility(View.GONE);
			}

		});

		ImageButton search_button_back = (ImageButton)view_temp.findViewById(R.id.search_button_webtv_result);

		search_button_back.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {

				// Go back to first view
				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.VISIBLE);
				//				LinearLayout linLayTopList = (LinearLayout) view_temp.findViewById(R.id.top_list);
				//				linLayTopList.setVisibility(View.VISIBLE);
				//TODO Remove commented if the toplist isn't used

				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.GONE);
				LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
				noSearch.setVisibility(View.GONE);
				//TODO fix so work
			}
		});

		LinearLayout linLayStart = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
		linLayStart.setVisibility(View.GONE);
		LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
		noSearch.setVisibility(View.GONE);

		return view_temp;
	}    

	/**
	 * Calls the back-end function to get the results of a search and shows them
	 * @author Francisco
	 */
	public void search(){

		// We can set a progress bar to show the user that we are searching
		pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		EditText search_box = (EditText)view_temp.findViewById(R.id.search_box_webtv);
		search_for_this = search_box.getText().toString();
		TextView resultText = (TextView) view_temp.findViewById(R.id.result_webtv);
		resultText.setText("Result for: '"+ search_for_this+"'");
		// Here we should call a function like this

		//TODO If no input in search field, no search shall be done //Emma
		if(search_for_this != null){  //DON'T DO ANYTHING DIFFERENT RAGARDLESS OF INPUT OR NOT 
			new AsyncWebSearch().execute();
		}
	}

	/**
	 * Print a "No results" text on the screen
	 * @author Francisco
	 */
	public void showNoResultsText(){

		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		results_ly.removeAllViewsInLayout();

		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textParams.setMargins((int)screenWidth/2 - 30, 30, 0, 0);

		TextView noResults = new TextView(view_temp.getContext());
		noResults.setText("No results");
		noResults.setTypeface(null,Typeface.BOLD);
		noResults.setTextColor(0xFFFFFFFF);
		noResults.setLayoutParams(textParams);
		results_ly.addView(noResults);
	}

	/**
	 * Print the results of the search on the screen
	 * @param res Set of results to show
	 * @author Francisco & Emma
	 */
	public void showResults(WebTVItem[] res){

		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		results_ly.removeAllViewsInLayout(); 
		LinearLayout.LayoutParams item_container_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams item_params = new LinearLayout.LayoutParams((int)(screenWidth*0.85),LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams icon_params = new LinearLayout.LayoutParams(100,80);
		LinearLayout.LayoutParams item_params2 = new LinearLayout.LayoutParams((int)(screenWidth*0.15),LayoutParams.MATCH_PARENT);

		for(WebTVItem x: res){

			LinearLayout item_container = new LinearLayout(view_temp.getContext());
			item_container.setBackgroundColor(0xFFCCCCCC);
			item_container.setPadding(4, 4, 4, 4);

			LinearLayout item2 = new LinearLayout(view_temp.getContext());
			item2.setBackgroundColor(0xFF999999);
			item2.setMinimumHeight(30);
			item2.setClickable(true);

			ImageButton queueButton = new ImageButton(view_temp.getContext());
			Drawable d = (Drawable) view_temp.getResources().getDrawable(R.drawable.queue_button);
			queueButton.setBackgroundDrawable(d); //Check if ok, should not be used with API 16
			item2.addView(queueButton);

			LinearLayout item = new LinearLayout(view_temp.getContext());
			item.setBackgroundColor(0xFF999999);
			item.setMinimumHeight(30);
			item.setClickable(true);

			ImageView icon = new ImageView(view_temp.getContext());
			//icon.setImageBitmap(x.getIcon());

			TextView title = new TextView(view_temp.getContext());
			title.setText(x.getTitle());
			title.setPadding(10, 0, 0, 0);
			title.setTextColor(0xFF000000);

			item.addView(icon, icon_params);
			item.addView(title);

			new AsyncImageLoader(icon, x).execute();

			item_container.addView(item, item_params);
			item_container.addView(item2, item_params2);
			results_ly.addView(item_container, item_container_params);

			tempItem = x;

			item.setOnClickListener(new View.OnClickListener() {
				WebTVItem resultItem = tempItem;
				@Override
				public void onClick(View v) {
					main.vibrate();
					WebTVCommand.instance().play(resultItem);
					//TODO Change color when press (only if time)
				}
			});

			queueButton.setOnClickListener(new View.OnClickListener() {
				WebTVItem queueItem = tempItem;
				@Override
				public void onClick(View v) {
					main.vibrate();
					WebTVCommand.instance().queue(queueItem);
					//TODO Change color when press (only if time)
				}
			});
		}
	}
	/**
	 * Add items into spinner (drop-down menu with services) dynamically
	 * @author Maria Jesus Platero
	 */
	public void addItemsOnSpinner(WebTVService services[]) {

		List<Bitmap> list = new ArrayList<Bitmap>();

		for(WebTVService serv : services){
			list.add(serv.getIcon());
		}

		Bitmap servicesImg[] = new Bitmap[list.size()];
		list.toArray(servicesImg);
		servicesIcons = servicesImg;
		ImageAdapter ia = new ImageAdapter(this.getActivity(), android.R.layout.simple_spinner_item, servicesImg);	
		spinner = (Spinner)view_temp.findViewById(R.id.webtv_spinner); // this returns null
		ia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(ia);
	}

	/**
	 * Method to process the services icons
	 * @author Maria Jesus Platero
	 */
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
			//	if(position == web_service){
			//		ImageView result_icon = (ImageView) view_temp.findViewById(R.id.webtv_icon_result);
			//		result_icon.setImageBitmap(services[position]);
			//}
			//TODO Probably remove commented //Emma
			return icon;
		}
	}

	/**
	 * Asynchronous loader for the WebTV services
	 * @author Maria Jesus Platero
	 */
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
	 */
	private class AsyncWebSearch extends AsyncTask<Integer, Integer, WebTVItem[]>{

		@Override
		protected WebTVItem[] doInBackground(Integer... arg0) {
			web_service = spinner.getSelectedItemPosition();
			WebTVQuery query = new WebTVQuery();
			WebTVItem[] elements= query.search(search_for_this, services[web_service]);

			return elements;
		}

		@Override
		protected void onPostExecute(WebTVItem elements[]) {
			// Sorry for the ugly way to check if the vector is empty
			int counter = 0;

			for(WebTVItem item: elements){
				counter++;
				break;
			}

			if(counter > 0)
				showResults(elements);
			else{
				showNoResultsText();
			}
		}
	}

	private class AsyncImageLoader extends AsyncTask<Integer, Integer, WebTVItem>{
		private WebTVItem item;
		private ImageView icon;

		public AsyncImageLoader(ImageView icon, WebTVItem item){
			this.icon = icon;
			this.item = item;
		}

		@Override
		protected WebTVItem doInBackground(Integer... params) {
			WebTVQuery query = new WebTVQuery();
			query.populateWithIcon(item);
			return item;
		}

		@Override
		protected void onPostExecute(WebTVItem item) {
			icon.setImageBitmap(item.getIcon());
		}
	}
}
