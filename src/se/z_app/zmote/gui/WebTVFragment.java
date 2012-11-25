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
	private ImageButton play_button;
	private ImageButton next_button;
	private ImageButton previous_button;
	private int web_service = 0;  // To know in what service are we currently (youtube, spotify...)
	private ProgressBar pb;
	private WebTVService services[];
	private String search_for_this = null;	
	private float screenWidth = 0;
	private float screenHeight = 0;
	private WebTVItem tempItem;
	private LinearLayout current_item;
	private LinearLayout current_item2;


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
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		new AsyncWebServiceLoader().execute();

		// Set the listener for the search button
		ImageButton search_button = (ImageButton)view_temp.findViewById(R.id.search_button_webtv);
		search_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start a new search
				LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
				LinearLayout noSearchLine = (LinearLayout) view_temp.findViewById(R.id.noSearchLine);
				if(!search()){
					noSearch.setVisibility(View.VISIBLE);
					noSearchLine.setVisibility(View.VISIBLE);
					LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
					results_ly.removeAllViewsInLayout();
					return;
				}
				//Loads the correct webTv icon
				noSearch.setVisibility(View.GONE);
				noSearchLine.setVisibility(View.GONE);
				web_service = spinner.getSelectedItemPosition();
				ImageView result_icon = (ImageView) view_temp.findViewById(R.id.webtv_icon_result);
				result_icon.setImageBitmap(servicesIcons[web_service]);

				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.GONE);
				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.VISIBLE);	
				addPlayBar();
			}

		});

		ImageButton search_button_back = (ImageButton)view_temp.findViewById(R.id.search_button_webtv_result);

		search_button_back.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {

				// Go back to first view
				LinearLayout linLay = (LinearLayout) view_temp.findViewById(R.id.searchBar);
				linLay.setVisibility(View.VISIBLE);
				LinearLayout linLayResult = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
				linLayResult.setVisibility(View.GONE);
				LinearLayout playBarLine = (LinearLayout) view_temp.findViewById(R.id.playBarLine);
				playBarLine.setVisibility(View.GONE);
				LinearLayout playBar = (LinearLayout) view_temp.findViewById(R.id.play_results_ly);
				playBar.setVisibility(View.GONE);
				LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
				noSearch.setVisibility(View.GONE);
				LinearLayout noSearchLine = (LinearLayout) view_temp.findViewById(R.id.noSearchLine);
				noSearchLine.setVisibility(View.GONE);

			}
		});

		//Default view when first entering the webTV view
		LinearLayout linLayStart = (LinearLayout) view_temp.findViewById(R.id.resultsBar);
		linLayStart.setVisibility(View.GONE);	
		LinearLayout playBarLine = (LinearLayout) view_temp.findViewById(R.id.playBarLine);
		playBarLine.setVisibility(View.GONE);
		LinearLayout playBar = (LinearLayout) view_temp.findViewById(R.id.play_results_ly);
		playBar.setVisibility(View.GONE);
		LinearLayout noSearch = (LinearLayout) view_temp.findViewById(R.id.noSearch);
		noSearch.setVisibility(View.GONE);
		LinearLayout noSearchLine = (LinearLayout) view_temp.findViewById(R.id.noSearchLine);
		noSearchLine.setVisibility(View.GONE);

		return view_temp;
	}    

	/**
	 * Calls the back-end function to get the results of a search and shows them
	 * @author Francisco
	 */
	public boolean search(){

		// We can set a progress bar to show the user that we are searching
		pb = (ProgressBar)view_temp.findViewById(R.id.progressLodingEpgChannelInformation);
		EditText search_box = (EditText)view_temp.findViewById(R.id.search_box_webtv);
		search_for_this = search_box.getText().toString();
		TextView resultText = (TextView) view_temp.findViewById(R.id.result_webtv);
		resultText.setText("Result for: '"+ search_for_this+"'");

		if(checkEmpty(search_box) == false){  //Check if the search box is empty
			new AsyncWebSearch().execute();
			return true;
		}

		else {

			return false;
		}
	}

	/**
	 * Check if there is an input in the text box or not
	 * @Author Thed and Ralf 
	 * @param text from text box
	 * @return boolean
	 */
	private boolean checkEmpty(EditText thaText) {
		if(thaText.getText().toString().trim().length() > 0){
			return false;
		}

		else{
			return true;
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

		//Sets the results view in webTV
		for(WebTVItem x: res){
			LinearLayout item_container = new LinearLayout(view_temp.getContext());
			item_container.setBackgroundColor(0xFFCCCCCC);
			item_container.setPadding(4, 4, 4, 4);

			final LinearLayout item2 = new LinearLayout(view_temp.getContext());
			item2.setBackgroundColor(0xFF999999);
			item2.setMinimumHeight(30);
			item2.setClickable(true);

			ImageButton queueButton = new ImageButton(view_temp.getContext());
			Drawable d = (Drawable) view_temp.getResources().getDrawable(R.drawable.queue_button);
			queueButton.setBackgroundDrawable(d); //Check if ok, should not be used with API 16
			item2.addView(queueButton);

			final LinearLayout item = new LinearLayout(view_temp.getContext());
			item.setBackgroundColor(0xFF999999);
			item.setMinimumHeight(30);
			item.setClickable(true);

			ImageView icon = new ImageView(view_temp.getContext());
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

			//Play webTV item
			item.setOnClickListener(new View.OnClickListener() {
				WebTVItem resultItem = tempItem;
				@Override
				public void onClick(View v) {
					main.vibrate();
					WebTVCommand.instance().play(resultItem);
			    	if(current_item != null && current_item2 != null){
			    		current_item.setBackgroundColor(0xFF999999);
			    		current_item2.setBackgroundColor(0xFF999999);
			    	}
					item.setBackgroundColor(0xFFCCCCCC);
					item2.setBackgroundColor(0xFFCCCCCC);
					current_item = item;	
					current_item2 = item2;	
				}
			});

			//Queue webTV item
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
	 * Method that sets the play-/pause-/next-/forward buttons
	 * @Author Emma Axelsson & Mar’a Platero
	 */
	public void addPlayBar(){
		System.out.println("TEST 2");
		LinearLayout playBarLine = (LinearLayout) view_temp.findViewById(R.id.playBarLine);
		LinearLayout playBar = (LinearLayout) view_temp.findViewById(R.id.play_results_ly);
		playBar.setVisibility(View.VISIBLE);
		playBar.setBackgroundColor(0x8833B5E5);
		playBarLine.setVisibility(View.VISIBLE);

		play_button = (ImageButton) view_temp.findViewById(R.id.play_button);
		next_button = (ImageButton) view_temp.findViewById(R.id.next_button);
		previous_button = (ImageButton) view_temp.findViewById(R.id.previous_button);

		// Listener with visual feedback for the play/pause_button
		play_button.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//If the user swipes
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					main.vibrate();
					//TODO: Function to perform HERE	

					play_button.setBackgroundColor(0xFFFFFFFF);
					// Put here the "light" button
					play_button.setBackgroundResource(R.drawable.play_pressed);
					RemoteControl.instance().sendButton(se.z_app.stb.api.RemoteControl.Button.TOGGLEPAUSEPLAY);
					return true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					play_button.setBackgroundColor(0xFF000000);
					play_button.setBackgroundResource(R.drawable.play_button);	
					return true;
				}else{
					return false;
				}
			}
		});

		// Listener with visual feedback for the next_button
		next_button.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//If the user swipes
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					main.vibrate();
					//TODO: Function to perform HERE	

					next_button.setBackgroundColor(0xFFFFFFFF);
					// Put here the "light" button
					next_button.setBackgroundResource(R.drawable.next_pressed);		
					RemoteControl.instance().sendButton(se.z_app.stb.api.RemoteControl.Button.RIGHT);
					return true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					next_button.setBackgroundColor(0xFF000000);
					next_button.setBackgroundResource(R.drawable.next_button);	
					return true;
				}else{
					return false;
				}
			}
		});

		// Listener with visual feedback for the previous_button
		previous_button.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//If the user swipes
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					main.vibrate();
					//TODO: Function to perform HERE	

					previous_button.setBackgroundColor(0xFFFFFFFF);
					// Put here the "light" button
					previous_button.setBackgroundResource(R.drawable.previous_pressed);
					RemoteControl.instance().sendButton(se.z_app.stb.api.RemoteControl.Button.LEFT);
					return true;
				}

				else if(event.getAction() == MotionEvent.ACTION_UP){
					previous_button.setBackgroundColor(0xFF000000);
					previous_button.setBackgroundResource(R.drawable.previous_button);	
					return true;
				}else{
					return false;
				}
			}
		});
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
