package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import se.z_app.social.Feed;

import se.z_app.social.zchat.ZChatAdapter;
import se.z_app.social.Post;
import se.z_app.stb.Program;

import se.z_app.zmote.epg.EPGContentHandler;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity that handles the GUI for the ZChat
 * 
 * @author Linus Back
 */
public class ZChatActivity extends SherlockActivity {
	
	public static Program targetProgram;
	private final static ZChatAdapter adapter = new ZChatAdapter();
	
	private Program myProgram;
	private Feed myFeed;
	private String userName = "Linus";
	
	public ZChatActivity(){
		myProgram = targetProgram;
		targetProgram = null;
	}


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ZChatActivity myActivity = this;
		setContentView(R.layout.activity_zchat);
	 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ListView postList = (ListView) findViewById(R.id.list_over_post);
		
		TextView textView = (TextView) findViewById(R.id.feed_name);
		//textView.setText(myProgram.getName());

		//TODO fix so it depends on the last added post.
		textView = (TextView) findViewById(R.id.time_of_feedUpdate);
		textView.setText("50 min ago");


		new AsyncDataLoader(this, postList).execute();


		
		Button postButton = (Button) findViewById(R.id.post_button);
		postButton.setOnClickListener(new PostButtonListener(myActivity, postList));

	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	           
	        	super.onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	




	private class FragmentAdabter extends BaseAdapter{

		private ZChatActivity zChatActivity;
		private Feed feed;
		private ArrayList<Post> list;


		public FragmentAdabter(ZChatActivity zChatActivity , Feed feed){
			this.zChatActivity = zChatActivity;
			this.feed = feed;
			Iterator<Post> iter = feed.iterator();
			list = new ArrayList<Post>();
			while(iter.hasNext()){
				list.add(0, iter.next());

			}


		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Post getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("The number of comments are: "+list.get(position).getCommentsAsCollection().size());

			View vi=convertView;
			if(convertView==null){
				vi = ((LayoutInflater)zChatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zchat_list, null);
			}

			TextView userName = (TextView) vi.findViewById(R.id.post_user_name);
			TextView date = (TextView) vi.findViewById(R.id.post_date);
			TextView content = (TextView) vi.findViewById(R.id.post_content);
			TextView nrOfComments = (TextView) vi.findViewById(R.id.post_nr_of_comments);


			userName.setText(list.get(position).getUserName());
			date.setText(list.get(position).getDateOfCreation().toString());
			content.setText(list.get(position).getContent());
			nrOfComments.setText(list.get(position).getComments().length + " comments");



			return vi;
		}



	}

	private class AsyncDataLoader extends AsyncTask<Integer, Integer, Feed>{

		private ListView postList;
		private ZChatActivity activity;
		public AsyncDataLoader(ZChatActivity activity ,ListView postList){
			this.postList = postList;
			this.activity = activity;
		}
		@Override
		protected Feed doInBackground(Integer... arg0) {


			//TODO return adapter.getFeed(programName);
			System.out.println(EPGContentHandler.instance().getEPG().iterator().next().iterator().next().getName());
			return adapter.getFeed(EPGContentHandler.instance().getEPG().iterator().next().iterator().next());


		}

		protected void onPostExecute(Feed feed){
			myFeed = feed;
			postList.setAdapter(new FragmentAdabter(activity, feed));

		}

	}

	private class CommitPost extends AsyncTask<Integer, Integer, Feed>{
		private ListView postList;
		private ZChatActivity activity;
		private String content;

		public CommitPost(ZChatActivity activity ,ListView postList, String content){
			this.postList = postList;
			this.activity = activity;
			this.content = content;
		}
		@Override
		protected Feed doInBackground(Integer... arg0) {
			System.out.println("Starting Do in backround");
			Post post = new Post();
			post.setContent(content);
			post.setFeed(myFeed);
			post.setUserName(userName);
			post.setDateOfCreation(new Date());
			post.setLastUpdate(new Date());
			System.out.println("Created post");
			
			Feed newFeed = adapter.commitPost(myFeed, post);
			System.out.println("got feed");
			return newFeed;
		}

		protected void onPostExecute(Feed feed){
			//TODO remove te sysout
			System.out.println("On post on the commitPost was done atleast: " + feed.equals(myFeed));
			myFeed = feed;
			postList.setAdapter(new FragmentAdabter(activity, feed));

		}
	}

	private class PostButtonListener implements OnClickListener{

		private ZChatActivity activity;
		private ListView postList;
		private String content;
		public PostButtonListener(ZChatActivity activity ,ListView postList){
			this.activity = activity;

			this.postList = postList;
		}
		@Override
		public void onClick(View v) {
			//TODO remove the sysouyt
			System.out.println("Post was Pressed mutcherfucker");
			EditText edit = (EditText) findViewById(R.id.new_post);
			
			new CommitPost(activity, postList, edit.getText().toString()).execute();
			
		}

	}

}




