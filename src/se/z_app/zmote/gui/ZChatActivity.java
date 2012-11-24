package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.Iterator;

import se.z_app.social.Feed;
import se.z_app.social.PostInterface;
import se.z_app.social.zchat.ZChatAdapter;
import se.z_app.social.Post;
import se.z_app.stb.Program;
import se.z_app.stb.STB;
import se.z_app.stb.api.EPGData;
import se.z_app.zmote.epg.EPGContentHandler;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ZChatActivity extends Activity {

	private MainTabActivity main;
	private ZChatAdapter zChatadapter;
	private ListView mListView;
	private View v;



	 public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_zchat);
		 TextView textView = (TextView) findViewById(R.id.feed_name);
		 textView.setText(getIntent().getExtras().getString("program"));
		 textView = (TextView) findViewById(R.id.time_of_feedUpdate);
		 textView.setText("50 min ago");
		 ListView postList = (ListView) findViewById(R.id.list_over_post);

		 new AsyncDataLoader(this, postList).execute();
		 
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
			ZChatAdapter adapter = new ZChatAdapter();
			System.out.println(EPGContentHandler.instance().getEPG().iterator().next().iterator().next().getName());
			return adapter.getFeed(EPGContentHandler.instance().getEPG().iterator().next().iterator().next());
			

		}
		
		protected void onPostExecute(Feed feed){
			 postList.setAdapter(new FragmentAdabter(activity, feed));
			
		}
		
	}
}




