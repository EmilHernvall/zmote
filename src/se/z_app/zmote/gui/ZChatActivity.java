package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import se.z_app.social.Comment;
import se.z_app.social.Feed;
import se.z_app.social.PostInterface;

import se.z_app.social.zchat.ZChatAdapter;
import se.z_app.social.Post;
import se.z_app.stb.Program;


import android.accounts.Account;
import android.accounts.AccountManager;

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
	private final ZChatAdapter adapter = new ZChatAdapter();

	private Program myProgram;
	private Feed myFeed;
	private String userName = null;
	private ZChatActivity myActivity;
	private ListView postList;
	private Object syncLock = new Object();
	private int timeBeforeFirstUpdate = 5000;
	private int timeBetweenUpdates = 5000;


	public ZChatActivity(){
		myProgram = targetProgram;
		targetProgram = null;

	}


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setUserName();
		myActivity = this;
		setContentView(R.layout.activity_zchat);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		postList = (ListView) findViewById(R.id.list_over_post);
		postList.setItemsCanFocus(true);

		TextView textView = (TextView) findViewById(R.id.feed_name);
		textView.setText("Feed for: '"+getMyProgram().getName()+"'");


		new AsyncDataLoader(this, postList).execute();



		Button postButton = (Button) findViewById(R.id.post_button);

		postButton.setOnClickListener(new PostButtonListener(myActivity, postList));
		new Timer().schedule(new TimedUpdate(), timeBeforeFirstUpdate, timeBetweenUpdates);

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
	



	/**
	 * Sets the user name to a user name of the device.
	 * First tries to set it to facebook name, if non exists
	 * sets it to the google account.
	 */
	private void setUserName(){
		AccountManager accountManager = AccountManager.get(this); 
		// Account[] account = accountManager.getAccountsByType("com.google");
		Account[] accounts = accountManager.getAccounts();
		boolean foundGoogleAcc= false;
		for(int i=0; i<accounts.length;i++){
			if(accounts[i].type.equals("com.sec.android.app.sns3.facebook")){
				userName = accounts[i].name;
				return;
			}
			if(accounts[i].type.equals("com.google")&& !foundGoogleAcc){
				userName = accounts[i].name.split("@")[0].replace(".", " ");
				foundGoogleAcc = true;
			}
		}

		if(userName!=null){
			return;
		}
		else if(accounts[0]!=null){
			String temp = accounts[0].name;
			if(temp.contains("@")){
				userName = accounts[0].name.split("@")[0].replace(".", " ");
				return;
			}else{
				userName = accounts[0].name.replace(".", " ");
				return;
			}


		}
		userName = "userName";   



	}

	private void setFeed(Feed feed){
		synchronized (syncLock) {
			myFeed = feed;
		}
	}

	private Feed getFeed(){
		synchronized (syncLock) {
			return myFeed;
		}
	}
	private synchronized ZChatAdapter getAdapter(){
		return adapter;
	}

	private synchronized Program getMyProgram(){
		return myProgram;
	}


	private class FragmentAdabter extends BaseAdapter{

		private ZChatActivity zChatActivity;

		private ArrayList<PostInterface> list;


		public FragmentAdabter(ZChatActivity zChatActivity , Feed feed){
			this.zChatActivity = zChatActivity;

			TextView textView = (TextView) findViewById(R.id.time_of_feedUpdate);
			if(feed.getLastUpdated().compareTo(new Date(0))!=0){
				textView.setText(feed.getLastUpdated().toString());
			}
			Iterator<Post> iter = feed.iterator();
			list = new ArrayList<PostInterface>();
			while(iter.hasNext()){
				Post post = iter.next();
				list.add(0, post);
				list.addAll(1, post.getCommentsAsCollection());

			}


		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public PostInterface getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi=convertView;
			if(list.get(position) instanceof Post){
				Post post = (Post) list.get(position);

				vi = ((LayoutInflater)zChatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zchat_list, null);

				TextView userName = (TextView) vi.findViewById(R.id.post_user_name);
				TextView date = (TextView) vi.findViewById(R.id.post_date);
				TextView content = (TextView) vi.findViewById(R.id.post_content);
				TextView nrOfComments = (TextView) vi.findViewById(R.id.post_nr_of_comments);


				userName.setText(post.getUserName());
				date.setText(post.getDateOfCreation().toString());
				content.setText(post.getContent());

				nrOfComments.setText(post.getComments().length + " comments");
				Button commentButton = (Button) vi.findViewById(R.id.comment_button);
				commentButton.setOnClickListener(new CommentButtonListener(post, myActivity, postList, vi));



			}else{
				Comment comment = (Comment) list.get(position);

				vi = ((LayoutInflater)zChatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zchat_list_comment, null);


				TextView userName = (TextView) vi.findViewById(R.id.comment_user_name);
				TextView date = (TextView) vi.findViewById(R.id.comment_date);
				TextView content = (TextView) vi.findViewById(R.id.comment_content);


				userName.setText(comment.getUserName());
				date.setText(comment.getDateOfCreation().toString());
				content.setText(comment.getContent());




			}

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
			return getAdapter().getFeed(getMyProgram());
		}

		protected void onPostExecute(Feed feed){
			setFeed(feed);
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

			Post post = new Post();
			post.setContent(content);
			post.setFeed(getFeed());
			post.setUserName(userName);
			post.setDateOfCreation(new Date());
			post.setLastUpdate(new Date());

			Feed newFeed = getAdapter().commitPost(getFeed(), post);

			return newFeed;
		}
		protected void onPostExecute(Feed feed){

			setFeed(feed);
			postList.setAdapter(new FragmentAdabter(activity, feed));

		}
	}


	private class CommitComment extends AsyncTask<Integer, Integer, Feed>{
		private ZChatActivity zChat;
		private ListView list;
		private Post post;
		private String content;

		public CommitComment(ZChatActivity zChat, ListView list, Post post,
				String string) {
			this.content = string;
			this.list = list;
			this.zChat = zChat;
			this.post = post;
		}

		@Override
		protected Feed doInBackground(Integer... params) {
			Comment comment = new Comment(post);
			comment.setContent(content);
			comment.setDateOfCreation(new Date());
			comment.setUserName(userName);
			Feed feed = getAdapter().commitComment(getFeed(), post, comment);
			return feed;
		}
		protected void onPostExecute(Feed feed){

			setFeed(feed);
			list.setAdapter(new FragmentAdabter(zChat, feed));

		}


	}

	private class CommentButtonListener implements OnClickListener{

		private Post post;
		private ZChatActivity zChat;
		private ListView list;
		private View view;

		public CommentButtonListener(Post post, ZChatActivity zChat, ListView list, View view){
			this.list = list;
			this.post = post;
			this.zChat = zChat;
			this.view = view;
		}
		@Override
		public void onClick(View v) {
			EditText edit = (EditText) view.findViewById(R.id.new_comment);
			new CommitComment(zChat, list, post, edit.getText().toString()).execute();

		}

	}
	private class PostButtonListener implements OnClickListener{

		private ZChatActivity activity;
		private ListView postList;


		public PostButtonListener(ZChatActivity activity ,ListView postList){
			this.activity = activity;

			this.postList = postList;
		}
		@Override
		public void onClick(View v) {

			EditText edit = (EditText) findViewById(R.id.new_post);

			new CommitPost(activity, postList, edit.getText().toString()).execute();

		}

	}


	private class TimedUpdate extends TimerTask{

		@Override
		public void run() {
			Feed newFeed = getAdapter().getFeed(getMyProgram());
			if(newFeed.getLastUpdated().after(getFeed().getLastUpdated())){
				setFeed(newFeed);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						postList.setAdapter(new FragmentAdabter(myActivity, getFeed()));
						
					}
				});
			}

		}

	}





}




