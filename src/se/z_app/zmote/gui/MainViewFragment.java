package se.z_app.zmote.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import se.z_app.stb.Channel;
import se.z_app.stb.Program;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;
import se.z_app.stb.api.RemoteControl;
import se.z_app.zmote.epg.EPGQuery;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class MainViewFragment extends Fragment implements OnGestureListener{

	private MainTabActivity main;
	private View v;
	private ImageView left;
	private ImageView center;
	private ImageView right;
	private TextView text;


	private RelativeLayout r;
	private float alpha = 0.25F;
	private float defaultAlpha = 0.7F;

	private boolean posVar = false;

	private float leftX;
	private float leftY;
	private float leftScale;

	private float centerX;
	private float centerY;
	private float centerScale;

	private float rightX;
	private float rightY;
	private float rightScale;
	private boolean isAnimationRunning = false;


	private ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	private ArrayList<Channel> channelList = new ArrayList<Channel>();

	private int currentChannelNr;
	
	public MainViewFragment(MainTabActivity main){
		this.main = main;
	}

	boolean clicked = false;

	
	float threshhold = 20;
	boolean activeGest = false;
	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		if(activeGest){
			float currentX = event.getX();
			float currentY = event.getY(); //TODO use with volume
			//TODO Get CurrenChannel +/- 1 from channelList, add new channel when rotating, 
			//rotate first, change channel on TV next to currentChannel
			float dir = x-currentX;
			
				if(Math.abs(dir) > threshhold){
				
				if(dir < 0 ){
					activeGest = false;
					rotateRight();
				}else if (dir > 0){
					activeGest = false;
					rotateLeft();
				}
			}
		}
		
	}
	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		activeGest = false;
	}
	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		activeGest= false;
	}

	float x;
	float y;
	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		
		activeGest = true;
		x = event.getX();
		y = event.getY();
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_main_view, null);
		r = (RelativeLayout)v.findViewById(R.id.rellativIconSpinner);
	

		GestureOverlayView gestures = (GestureOverlayView) v.findViewById(R.id.gestures);
		gestures.addOnGestureListener(this);	

		
		new AsyncDataLoader().execute();

		return v;
	}



	public boolean setChannel(Channel targert){
		if (!isAnimationRunning) {	
			for(int i = 0; i< channelList.size(); i++){
				if(channelList.get(i).getUrl().contains(targert.getUrl())){
					setChannel(i);
					return true;
				}
			}
		}
		return false;		
	}

	public void setChannel(int channelNr){		
		if(currentChannelNr == channelNr)
			return;

		Log.i("TestZ", "Current nr: " +currentChannelNr );
		Log.i("TestZ", "Targer nr: " +channelNr );

		int can1 = 0;
		int can2 = 0;
		int fin = 0;
		int size = imageList.size(); 
		for(int i = 0; i< size; i++){
			if((can1+currentChannelNr)%size == channelNr){
				fin = can1;
				break;
			}
			if((currentChannelNr-can2+size)%size == channelNr){
				fin = -can2;
				break;
			}
			can2++;
			can1++;
		}

		if(fin < 0)
			rotateRight(fin*(-1));
		else
			rotateLeft(fin);

	}


	private void rotateRight(){
		rotateRight(1);
	}


	private int tmpInt;
	private void rotateRight(int turns){
		if(isAnimationRunning){
			return;
		}
		isAnimationRunning = true;
		
		if(!posVar){
			setVariables();
			posVar = true;
		}					



		currentChannelNr = (currentChannelNr+imageList.size()-1)%imageList.size();

		ImageView newLeft = imageList.get((currentChannelNr+imageList.size()-1)%imageList.size());
		newLeft.setY(-300);
		newLeft.setX(-300);
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		newLeft.setLayoutParams(params1);
		newLeft.setVisibility(View.VISIBLE);
		newLeft.setAlpha(defaultAlpha);
		//r.removeView(newLeft);
		
		try{
			r.addView(newLeft);
		}catch(RuntimeException e){}
		newLeft.invalidate();


		right.animate().y(-300).x(rightX + 300).setListener(new AnimatorListener() {
			ImageView tmp = right;


			@Override
			public void onAnimationStart(Animator animation) {
				isAnimationRunning = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);
				isAnimationRunning = false;	

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);
				isAnimationRunning = false;

			}
		});


		center.animate().x(rightX).y(rightY).scaleX(rightScale).scaleY(rightScale).alpha(defaultAlpha).setListener(null);


		tmpInt = turns;
		left.animate().x(centerX).y(centerY).scaleX(centerScale).scaleY(centerScale).alpha(alpha).setListener(new AnimatorListener() {
			int turns = tmpInt;
			@Override
			public void onAnimationStart(Animator animation) {
				text.setText("");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {

				turns--;
				if(turns>0){
					rotateRight(turns);
				}else{
					text.setText(generateText());
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {}
		});

		newLeft.animate().x(leftX).y(leftY).scaleX(leftScale).scaleY(leftScale).setListener(null);



		right = center;
		center = left;
		left = newLeft;
	}

	private void rotateLeft(){
		rotateLeft(1);
	}

	private void rotateLeft(int turns){

		if(isAnimationRunning){
			return;
		}
		isAnimationRunning = true;
		
		if(!posVar){
			setVariables();
			posVar = true;
		}	

		currentChannelNr = (currentChannelNr+1)%imageList.size();
		ImageView newRight = imageList.get((currentChannelNr+1)%imageList.size());
		newRight.setY(-300);
		newRight.setX(rightX+300);
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		newRight.setLayoutParams(params1);
		newRight.setVisibility(View.VISIBLE);
		newRight.setAlpha(defaultAlpha);
		
		try{
			r.addView(newRight);
		}catch(RuntimeException e){}
		
		newRight.invalidate();



		left.animate().y(-300).x(-300).setListener(new AnimatorListener() {
			ImageView tmp = left;

			@Override
			public void onAnimationStart(Animator animation) {
				isAnimationRunning = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);	
				isAnimationRunning = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);			
				isAnimationRunning = false;
			}
		});



		center.animate().x(leftX).y(leftY).scaleX(leftScale).scaleY(leftScale).alpha(defaultAlpha).setListener(null);
		tmpInt = turns;
		right.animate().x(centerX).y(centerY).scaleX(centerScale).scaleY(centerScale).alpha(alpha).setListener(new AnimatorListener() {
			int turns = tmpInt;
			@Override
			public void onAnimationStart(Animator animation) {
				text.setText("");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				turns--;
				if(turns>0){
					rotateLeft(turns);
				}else{
					text.setText(generateText());
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {}
		});

		newRight.animate().x(rightX).y(rightY).scaleX(rightScale).scaleY(rightScale).setListener(null);




		left = center;
		center = right;
		right = newRight;




	}


	private void buildForCurrentChannel(){
		left = imageList.get((currentChannelNr+imageList.size()-1)%imageList.size());
		center = imageList.get(currentChannelNr);
		right = imageList.get((1+currentChannelNr)%imageList.size());


		left.setScaleX(2F);
		left.setScaleY(2F);
		//left.setPadding(10, 10, 10, 10);
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins(50, 30, 30, 30);
		left.setAlpha(defaultAlpha);
		left.setLayoutParams(params1);


		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.setMargins(10, 400, 10, 10);


		center.setAlpha(alpha);
		center.setLayoutParams(params1);
		center.setScaleX(4.5F);
		center.setScaleY(4.5F);



		right.setScaleX(2F);
		right.setScaleY(2F);

		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.setMargins(30, 30, 50, 30);
		right.setAlpha(defaultAlpha);
		right.setLayoutParams(params1);


		r.addView(left);
		r.addView(center);
		r.addView(right);


		text = new TextView(v.getContext());
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.ALIGN_LEFT, center.getId());
		params1.addRule(RelativeLayout.ALIGN_TOP, center.getId());
		params1.addRule(RelativeLayout.ALIGN_BOTTOM, center.getId());
		params1.addRule(RelativeLayout.ALIGN_RIGHT, center.getId());
		params1.setMargins(40, 300, 40, 60);
		text.setLayoutParams(params1);

		text.setText(generateText());
		text.setTextColor(0xFFFFFFFF);
		text.setAlpha(1);

		r.addView(text);
	}


	private String generateText(){
		String t = "";
		Channel channel = channelList.get(currentChannelNr);
		Program currentProgram = null;
		Program nextProgram = null;
		Program nextNextProgram = null;
		Date now = new Date(System.currentTimeMillis());

		for (Program program : channel) {
			if(now.compareTo(program.getStart()) >= 0)
				currentProgram = program;
			else if(nextProgram == null){
				nextProgram = program;
			}else{
				nextNextProgram = program;
				break;
			}



		}
		if(currentProgram == null)
			return "";

		String name = currentProgram.getName();
		String startTime = new SimpleDateFormat("HH:mm").format(currentProgram.getStart());
		String info = currentProgram.getLongText();

		name = trimString(name, 29);
		info = trimString(info, 260);
		t = "> " + startTime +  " - " + name + "\n" ; 
		t += info + "\n\n";

		if(nextProgram != null){
			String nextName = nextProgram.getName();
			String nextStartTime = new SimpleDateFormat("HH:mm").format(nextProgram.getStart());
			nextName = trimString(nextName, 29);
			t += "> " + nextStartTime +  " - " + nextName + "\n";
		}

		if(nextNextProgram != null){
			String nextNextName = nextNextProgram.getName();
			String nextNextStartTime = new SimpleDateFormat("HH:mm").format(nextNextProgram.getStart());
			nextNextName = trimString(nextNextName, 29);
			t += "> " + nextNextStartTime +  " - " + nextNextName;
		}

		return t;
	}

	private String trimString(String s, int max){
		if(s.length() > max){
			s = s.substring(0, max-4) + "...";
		}
		return s;
	}


	private void setVariables(){
		leftX = left.getX();
		leftY = left.getY();
		leftScale = left.getScaleX();

		centerX = center.getX();
		centerY = center.getY();
		centerScale = center.getScaleX();

		rightX = right.getX();
		rightY = right.getY();
		rightScale = right.getScaleX();
	}



	private class AsyncDataLoader extends AsyncTask<Integer, Integer, EPG>{

		@Override
		protected EPG doInBackground(Integer... params) {
			EPGQuery query = new EPGQuery();
			return query.getEPG();
		}

		@Override
		protected void onPostExecute(EPG epg) {

			v.findViewById(R.id.progressLoadingEPG).setVisibility(View.INVISIBLE);

			currentChannelNr = 0;
			for (Channel channel : epg) {
				Drawable draw = new BitmapDrawable(channel.getIcon());
				ImageView i = new ImageView(v.getContext());
				i.setImageDrawable(draw);	
				i.setBackgroundColor(0xFFFFFFFF );
				i.setAdjustViewBounds(true);
				i.setMaxHeight(150);
				i.setMaxHeight(150);
				i.invalidate();


				imageList.add(i);
				channelList.add(channel);

				i.setOnClickListener(new View.OnClickListener() {
					int i = currentChannelNr;
					@Override
					public void onClick(View v) {
						setChannel(i);


					}
				});

				currentChannelNr++;

			}

			currentChannelNr = 0;
			buildForCurrentChannel();

		}


	}



	







}
