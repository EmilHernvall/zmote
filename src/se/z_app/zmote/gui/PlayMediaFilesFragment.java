package se.z_app.zmote.gui;

import java.io.File;
import java.util.LinkedList;

import se.z_app.stb.WebTVItem;
import se.z_app.stb.api.WebTVCommand;



import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

public class PlayMediaFilesFragment extends Fragment {
	private MainTabActivity tab;
	private float screenWidth = 0;
	private View view_temp;


	public PlayMediaFilesFragment(MainTabActivity mainTabActivity) {
		this.tab = mainTabActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	

		view_temp = inflater.inflate(R.layout.fragment_web_tv, null);  //TODO: DO we need this?
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		new AsyncSearch().execute();
		return view_temp;
	}
	
	public void showResults(ConcurrentSkipListMap res){

		// some layout stuff
		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		results_ly.removeAllViewsInLayout(); 
		LinearLayout.LayoutParams item_container_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams item_params = new LinearLayout.LayoutParams((int)(screenWidth*0.85),LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams icon_params = new LinearLayout.LayoutParams(100,80);
		LinearLayout.LayoutParams item_params2 = new LinearLayout.LayoutParams((int)(screenWidth*0.15),LayoutParams.MATCH_PARENT);

			
		while(!res.isEmpty()){
//			File fetchingFile = res.
//
//			LinearLayout item_container = new LinearLayout(view_temp.getContext());
//			item_container.setBackgroundColor(0xFFCCCCCC);
//			item_container.setPadding(4, 4, 4, 4);

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
//			title.setText(res.getTitle());
			title.setPadding(10, 0, 0, 0);
			title.setTextColor(0xFF000000);

			item.addView(icon, icon_params);
			item.addView(title);

//			new AsyncImageLoader(icon, x).execute();  //remove, we can add the image later
//
//			item_container.addView(item, item_params);
//			item_container.addView(item2, item_params2);
//			results_ly.addView(item_container, item_container_params);
//
//			tempItem = x;

			item.setOnClickListener(new View.OnClickListener() {
//				WebTVItem resultItem = tempItem;
				@Override
				public void onClick(View v) {
//					main.vibrate();
//					WebTVCommand.instance().play(resultItem);
					//TODO Change color when press (only if time)
				}
			});

			queueButton.setOnClickListener(new View.OnClickListener() {
//				WebTVItem queueItem = tempItem;
				@Override
				public void onClick(View v) {
//					main.vibrate();
//					WebTVCommand.instance().queue(queueItem);
					//TODO Change color when press (only if time)
				}
			});
		}
	}

	private class AsyncSearch extends AsyncTask<Integer, Integer, ConcurrentSkipListMap>{

		private ConcurrentSkipListMap getResult(){
			ConcurrentSkipListMap mediaFiles = new ConcurrentSkipListMap();
			LinkedList<File> files = new LinkedList<File>();
			files.addLast(Environment.getExternalStorageDirectory());
			files.addLast(Environment.getDataDirectory());
			files.addLast(Environment.getDownloadCacheDirectory());
			
			Log.i("Files", "Starting scan...");
			long time = System.currentTimeMillis();
			while(!files.isEmpty()){
				File file = files.removeFirst();
				File children[] = file.listFiles();
				if(children != null){
					for (File child : children) {
						if(!child.isHidden()){
							if(child.isDirectory()){
								files.addLast(child);
							}
							else{
//								Log.i("files","in the deepest deep the file name is: "+child.getName());
								String filename = child.getName().toLowerCase();
								if(filename.endsWith(".mp4") || filename.endsWith(".mp3")){
									mediaFiles.put(child.lastModified(),child);
									Log.i("Files", "Found file..."+ filename);
								}
							}
						}
					}
				}
			}

//			Log.i("Files", "Scan time: " + (System.currentTimeMillis()-time));
//			for (File file : mediaFiles) {
//				Log.i("Files", "Found: " + file.getAbsolutePath());
//			}
			return mediaFiles;
		}
		@Override
		protected ConcurrentSkipListMap doInBackground(Integer... params) {
			return getResult();
		}


	}
}
