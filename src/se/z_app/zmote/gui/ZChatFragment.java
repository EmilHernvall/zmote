package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.z_app.social.Feed;
import se.z_app.social.PostInterface;
import se.z_app.social.zchat.ZChatAdapter;
import se.z_app.social.Post;
import se.z_app.social.Comment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ZChatFragment extends Fragment {

	private MainTabActivity main;
	private ZChatAdapter zChatadapter;
	private ListView mListView;
	private View v;

	public ZChatFragment(MainTabActivity mainTabActivity) {
		this.main = mainTabActivity;
		this.zChatadapter = new ZChatAdapter();
	}
	public ZChatFragment(){

	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
				return container;
		/*
		v = inflater.inflate(R.layout.fragment_zchat, null);
		mListView = (ListView) v.findViewById(R.id.list);
		List<? extends Map<String, ?>> data = GetSampleData();







		SimpleAdapter adapter = new SimpleAdapter(v.getContext(), data,
				R.layout.zchat_list, new String[] { "username", "usertext" },
				new int[] { R.id.username, R.id.usertext });


		mListView.setAdapter(adapter);
		return v;
	}

	// "userIcon", R.id.userIcon,
	private	List<HashMap<String, ?>> GetSampleData(){
		List<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();

		HashMap<String, String> map = new HashMap<String, String>();
	//	map.put("userIcon", R.drawable.green_button);
		map.put("username", "Shen");
		map.put("usertext", "This is a simple sample for SimpleAdapter");
		list.add(map);
		map = new HashMap<String, String>();
	//	map.put("userIcon", R.drawable.red_dot);
		map.put("username", "Ricardo");
		map.put("usertext", "This is a simple sample for nothing!");
		list.add(map);
		return list;*/

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
}




