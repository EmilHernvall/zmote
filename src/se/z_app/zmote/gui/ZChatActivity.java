package se.z_app.zmote.gui;

import java.util.ArrayList;

import se.z_app.social.Feed;
import se.z_app.social.PostInterface;
import se.z_app.social.zchat.ZChatAdapter;
import se.z_app.social.Post;
import se.z_app.stb.Program;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		 setContentView(R.layout.zchat_list);
		 TextView textView = (TextView) findViewById(R.id.feed_name);
		 textView.setText(getIntent().getExtras().getString("program"));
		 
		 new AsyncDataLoader();
		 
	 }
	 
	
	
	private class FragmentAdabter extends BaseAdapter{

		private Fragment fragment;
		private Feed feed;
		private ArrayList<PostInterface> list;
		
		public FragmentAdabter(Fragment fragment, Feed feed){
			this.fragment = fragment;
			this.feed = feed;
			list = new ArrayList<PostInterface>();
			populateArrayList();
			
			
		}
		private void populateArrayList() {
			for(Post post : feed){
				list.add(0, post);
				list.addAll(1, post.getCommentsAsCollection());
			}
		}
		@Override
		public int getCount() {
			
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private class AsyncDataLoader extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
		
			return null;
		}
		
	}
}




