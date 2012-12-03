package se.z_app.zmote.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import se.z_app.stb.EPG;
import se.z_app.stb.MediaItem;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.WebTVCommand;
import se.z_app.zmote.webtv.MediaStreamer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

/**
 * The fragment from which one can play media form the phone, currently supports .mp3 music and .mp4 videos.
 * @author Christian Tennstedt & Marcus Widegren
 *
 */
public class PlayMediaFilesFragment extends Fragment {

	private MainTabActivity tab;
	private float screenWidth = 0;
	private View view_temp;
	private File tmpFile;


	/**
	 * Constructor for the PlayMediaFilesFragment
	 * @param mainTabActivity The tab
	 */
	public PlayMediaFilesFragment(MainTabActivity mainTabActivity) {
		this.tab = mainTabActivity;
	}

	/**
	 * Creation of the view
	 * @return view_temp
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	

		view_temp = inflater.inflate(R.layout.fragment_stream_file, null); 
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		 //the program waits until this line is executed, we could make this better to load the fragment faster	
		AsyncTask<Integer, Integer, ConcurrentSkipListMap> async = new AsyncSearch().execute();
		return view_temp;
	}

	/**
	 * Show the media files in the device.
	 * @param res
	 */
	private void showResults(ConcurrentSkipListMap res){

		// some layout stuff
		LinearLayout results_ly = (LinearLayout) view_temp.findViewById(R.id.search_results_ly);
		results_ly.removeAllViewsInLayout(); 
		LinearLayout.LayoutParams item_container_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams item_params = new LinearLayout.LayoutParams((int)(screenWidth*0.85),LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams icon_params = new LinearLayout.LayoutParams(64,64);
		LinearLayout.LayoutParams item_params2 = new LinearLayout.LayoutParams((int)(screenWidth*0.15),LayoutParams.MATCH_PARENT);

		while(!res.isEmpty()){
			Object key = res.lastKey();
			File fileToAdd = (File)res.remove(key);

			LinearLayout item_container = new LinearLayout(view_temp.getContext());
			item_container.setBackgroundColor(0xFFCCCCCC);
			item_container.setPadding(4, 4, 4, 4);

			final LinearLayout item2 = new LinearLayout(view_temp.getContext());
			item2.setBackgroundColor(0xFF999999);
			item2.setMinimumHeight(30);
			item2.setClickable(true);		

			final LinearLayout item = new LinearLayout(view_temp.getContext());
			item.setBackgroundColor(0xFF999999);
			item.setMinimumHeight(30);
			item.setClickable(true);

			ImageView icon = new ImageView(view_temp.getContext());
			String fileName = fileToAdd.toString();
			if (fileName.endsWith(".mp3")){
				icon.setBackgroundResource(R.drawable.ic_music);
			}
			else if (fileName.endsWith(".mp4")){
				icon.setBackgroundResource(R.drawable.ic_video);
			}

			else if (fileName.endsWith(".jpg")){
				icon.setBackgroundResource(R.drawable.ic_photo);
			}
			else{
				icon.setBackgroundResource(R.drawable.ic_action_search);
			}
			
			TextView title = new TextView(view_temp.getContext());
			title.setText(fileToAdd.getName());
			title.setPadding(10, 0, 0, 0);
			title.setTextColor(0xFF000000);

			item.addView(icon, icon_params);
			item.addView(title);

			item_container.addView(item, item_params);
			item_container.addView(item2, item_params2);
			results_ly.addView(item_container, item_container_params);

			tmpFile = fileToAdd;
			item.setOnClickListener(new View.OnClickListener() {
				File file = tmpFile;
				@Override
				public void onClick(View v) {
					tab.vibrate();
					MediaItem item_n = MediaStreamer.instance().addFile(file);
					Log.i("files","launcing file "+ file.getAbsolutePath());
					RemoteControl.instance().launch(item_n);
					item.setBackgroundColor(0x8833B5E5);
					item2.setBackgroundColor(0x8833B5E5);
					
					//To restore the color after a few milliseconds
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(90);
							} catch (InterruptedException e) {

							}
							tab.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									item.setBackgroundColor(0xFF999999);
									item2.setBackgroundColor(0xFF999999);
								}
							});
						}
					}).start();

				}
			});

		}


	}

	/**
	 * Asynchronous task.
	 */
	private class AsyncSearch extends AsyncTask<Integer, Integer, ConcurrentSkipListMap>{

		private ConcurrentSkipListMap getResult(){
			ConcurrentSkipListMap mediaFiles = new ConcurrentSkipListMap();
			LinkedList<File> files = new LinkedList<File>();
			files.addLast(Environment.getExternalStorageDirectory());
			files.addLast(Environment.getDataDirectory());
			files.addLast(Environment.getDownloadCacheDirectory());

			Log.i("Files", "Starting scan...");
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
								String filename = child.getName().toLowerCase();
								if(filename.endsWith(".mp4") || filename.endsWith(".mp3") || filename.endsWith(".jpg")){
									mediaFiles.put(child.lastModified(),child);
									Log.i("Files", "Found file..."+ filename);
								}
							}
						}
					}
				}
			}
			return mediaFiles;
		}
		
		/**
		 * Skips the list map
		 */
		@Override
		protected ConcurrentSkipListMap doInBackground(Integer... params) {
			return getResult();
		}

		/**
		 * Post execution
		 */
		protected void onPostExecute(ConcurrentSkipListMap results) {

			view_temp.findViewById(R.id.progressBarStream).setVisibility(View.INVISIBLE);
			showResults(results);
		}	
	}
}
