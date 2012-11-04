package se.z_app.zmote.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;

import se.z_app.stb.Channel;
import se.z_app.stb.Program;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.RemoteControl.Button;

import se.z_app.zmote.epg.EPGQuery;
import se.z_app.zmote.gui.R.drawable;

import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class MainViewFragment extends Fragment implements OnGestureListener{

	private MainTabActivity main;
	private View v;
	private ImageView left;
	private ImageView leftleft;
	private ImageView center;
	private ImageView right;
	private ImageView rightright;
	private TextView text;
	private int screenwidth;
	private int screenheight;
	private int imagewidth = -1;
	private int imagehight = -1;
	

	private RelativeLayout r;
	private float alpha = 0.25F;
	private float middleAlpha = 0.5F;
	private float defaultAlpha = 0.7F;

	private boolean posVar = false;

	private float leftleftX;
	private float leftleftY;
	private float leftleftScale;
	
	private float leftX;
	private float leftY;
	private float leftScale;

	private float centerX;
	private float centerY;
	private float centerScale;

	private float rightX;
	private float rightY;
	private float rightScale;
	
	private float rightrightX;
	private float rightrightY;
	private float rightrightScale;
	
	private boolean isAnimationRunning = false;
	private int animationDuration = 400;


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
			float dirX = x-currentX;
			float dirY = y-currentY;
			
			
			if(Math.abs(dirX) > threshhold || Math.abs(dirY) > threshhold ){
				activeGest = false;
				if(Math.abs(dirX) > Math.abs(dirY)){	
					//Changing Channel from swipe
					if(dirX < 0 )
						rotateRight();
					else if (dirX > 0)	
						rotateLeft();
					
					Channel channel = channelList.get(currentChannelNr);
					RemoteControl.instance().launch(channel.getUrl());
				}else{
					//Changing Volume from swipe
					if(dirY < 0 )
						RemoteControl.instance().sendButton(Button.VOLPLUS);
					else if (dirY > 0)
						RemoteControl.instance().sendButton(Button.VOLMINUS);
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
	
		
		screenwidth = v.getResources().getDisplayMetrics().widthPixels;
		screenheight = v.getResources().getDisplayMetrics().heightPixels;
		
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

		//Log.i("TestZ", "Current nr: " +currentChannelNr );
		//Log.i("TestZ", "Targer nr: " +channelNr );

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

		ImageView newLeft = imageList.get((currentChannelNr+imageList.size()-2)%imageList.size());
		
		newLeft.setVisibility(View.INVISIBLE);
		
		ObjectAnimator.ofFloat(newLeft, "x", -300).setDuration(0).start();
		ObjectAnimator.ofFloat(newLeft, "y", -300).setDuration(0).start();
		
		newLeft.setVisibility(View.VISIBLE);
		
		try{
			r.addView(newLeft);
		}catch(RuntimeException e){}
		

		
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(rightright, "x", rightrightX + 300),
				ObjectAnimator.ofFloat(rightright, "y", -300)
				);
		
		tmpInt = turns;
		animatorSet.addListener(new AnimatorListener() {
			ImageView tmp = rightright;
			int turns = tmpInt;
			
			@Override
			public void onAnimationStart(Animator animation) {
				isAnimationRunning = true;
				text.setText("");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);
				isAnimationRunning = false;	
				
				turns--;
				if(turns>0){
					rotateRight(turns);
				}else{
					text.setText(generateText());
				}

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);
				isAnimationRunning = false;
				
				turns--;
				if(turns>0){
					rotateRight(turns);
				}else{
					text.setText(generateText());
				}

			}
		
		});

		animatorSet.playTogether(
				ObjectAnimator.ofFloat(right, "x", rightrightX),
				ObjectAnimator.ofFloat(right, "y", rightrightY),
				ObjectAnimator.ofFloat(right, "scaleX", rightrightScale),
				ObjectAnimator.ofFloat(right, "scaleY", rightrightScale),
				ObjectAnimator.ofFloat(right, "alpha", defaultAlpha)
			);

		animatorSet.playTogether(
				ObjectAnimator.ofFloat(center, "x", rightX),
				ObjectAnimator.ofFloat(center, "y", rightY),
				ObjectAnimator.ofFloat(center, "scaleX", rightScale),
				ObjectAnimator.ofFloat(center, "scaleY", rightScale),
				ObjectAnimator.ofFloat(center, "alpha", middleAlpha)
			);
		

		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(left, "x", centerX),
				ObjectAnimator.ofFloat(left, "y", centerY),
				ObjectAnimator.ofFloat(left, "scaleX", centerScale),
				ObjectAnimator.ofFloat(left, "scaleY", centerScale),
				ObjectAnimator.ofFloat(left, "alpha", alpha)
			);
		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(leftleft, "x", leftX),
				ObjectAnimator.ofFloat(leftleft, "y", leftY),
				ObjectAnimator.ofFloat(leftleft, "scaleX", leftScale),
				ObjectAnimator.ofFloat(leftleft, "scaleY", leftScale),
				ObjectAnimator.ofFloat(leftleft, "alpha", middleAlpha)
			);
		

		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(newLeft, "x", leftleftX),
				ObjectAnimator.ofFloat(newLeft, "y", leftleftY),
				ObjectAnimator.ofFloat(newLeft, "scaleX", leftleftScale),
				ObjectAnimator.ofFloat(newLeft, "scaleY", leftleftScale),
				ObjectAnimator.ofFloat(newLeft, "alpha", defaultAlpha)
			);
		
		
//Playing animation
		animatorSet.setDuration(animationDuration).start();
		
		rightright = right;
		right = center;
		center = left;
		left = leftleft;
		leftleft= newLeft;
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
		ImageView newRight = imageList.get((currentChannelNr+2)%imageList.size());

		
		newRight.setVisibility(View.INVISIBLE);
		
		ObjectAnimator.ofFloat(newRight, "x", (rightX + 300)).setDuration(0).start();
		ObjectAnimator.ofFloat(newRight, "y", -300).setDuration(0).start();
		
		newRight.setVisibility(View.VISIBLE);
		
		try{
			r.addView(newRight);
		}catch(RuntimeException e){}

	
		AnimatorSet animatorSet = new AnimatorSet();
		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(leftleft, "x", -300),
				ObjectAnimator.ofFloat(leftleft, "y", -300)
			);
		
		tmpInt = turns;
		animatorSet.addListener(new AnimatorListener() {
			ImageView tmp = leftleft;
			int turns = tmpInt;
			@Override
			public void onAnimationStart(Animator animation) {
				isAnimationRunning = true;
				text.setText("");
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);	
				isAnimationRunning = false;
				turns--;
				if(turns>0){
					rotateLeft(turns);
				}else{
					text.setText(generateText());
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);			
				isAnimationRunning = false;
				turns--;
				if(turns>0){
					rotateLeft(turns);
				}else{
					text.setText(generateText());
				}
			}
		});
		
		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(left, "x", leftleftX),
				ObjectAnimator.ofFloat(left, "y", leftleftY),
				ObjectAnimator.ofFloat(left, "scaleX", leftleftScale),
				ObjectAnimator.ofFloat(left, "scaleY", leftleftScale),
				ObjectAnimator.ofFloat(left, "alpha", defaultAlpha)
			);
		
	
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(center, "x", leftX),
				ObjectAnimator.ofFloat(center, "y", leftY),
				ObjectAnimator.ofFloat(center, "scaleX", leftScale),
				ObjectAnimator.ofFloat(center, "scaleY", leftScale),
				ObjectAnimator.ofFloat(center, "alpha", middleAlpha)
			);


		animatorSet.playTogether(
				ObjectAnimator.ofFloat(right, "x", centerX),
				ObjectAnimator.ofFloat(right, "y", centerY),
				ObjectAnimator.ofFloat(right, "scaleX", centerScale),
				ObjectAnimator.ofFloat(right, "scaleY", centerScale),
				ObjectAnimator.ofFloat(right, "alpha", alpha)
			);
		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(rightright, "x", rightX),
				ObjectAnimator.ofFloat(rightright, "y", rightY),
				ObjectAnimator.ofFloat(rightright, "scaleX", rightScale),
				ObjectAnimator.ofFloat(rightright, "scaleY", rightScale),
				ObjectAnimator.ofFloat(rightright, "alpha", middleAlpha)
			);
		
		
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(newRight, "x", rightrightX),
				ObjectAnimator.ofFloat(newRight, "y", rightrightY),
				ObjectAnimator.ofFloat(newRight, "scaleX", rightrightScale),
				ObjectAnimator.ofFloat(newRight, "scaleY", rightrightScale),
				ObjectAnimator.ofFloat(newRight, "alpha", defaultAlpha)
			);
		
//Playing Animation		
		animatorSet.setDuration(animationDuration).start();



		leftleft = left;
		left = center;
		center = right;
		right = rightright;
		rightright = newRight;
		
		


	}


	private void buildForCurrentChannel(){
		if(imagewidth == 0){
			imagewidth = 96;
		}
		
		int padding = 20;
		
		leftleft = imageList.get((currentChannelNr+imageList.size()-2)%imageList.size());
		left = imageList.get((currentChannelNr+imageList.size()-1)%imageList.size());
		center = imageList.get(currentChannelNr);
		right = imageList.get((1+currentChannelNr)%imageList.size());
		rightright = imageList.get((2+currentChannelNr)%imageList.size());

		Log.i("Screen", "Screen width = " + screenwidth);
		Log.i("Screen", "Screen height = " + screenheight);
		Log.i("Screen", "Image width = " + imagewidth);
		Log.i("Screen", "Image hight = " + imagehight);
		
		
//LeftLeft
		
		
		leftleftScale = 5*screenwidth/imagewidth/19; //screenheight/imagehight/6;
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//params1.setMargins((int)(imagewidth*leftleftScale/4), (int)((imagehight*leftleftScale/4)), 0, 0);
		leftleft.setLayoutParams(params1);
		
		ObjectAnimator.ofFloat(leftleft, "scaleX", leftleftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(leftleft, "scaleY", leftleftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(leftleft, "alpha", defaultAlpha).setDuration(0).start();
		
		
//Left		
		leftScale = (float)screenwidth/(float)imagewidth/(float)2;
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins((int)(imagewidth*leftScale/4), (int)((imagehight*leftScale/4)+imagehight*leftleftScale+padding), 0, 0);
		//params1.setMargins(0, (int)(imagehight*rightScale/4)+imagehight+padding, (int)(imagewidth*rightScale/4), 0);
		left.setLayoutParams(params1);
		
		ObjectAnimator.ofFloat(left, "scaleX", leftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(left, "scaleY", leftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(left, "alpha", middleAlpha).setDuration(0).start();
		
		
//Center
		centerScale = (float) (screenwidth/imagewidth);
		
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.ABOVE, R.id.imageVolDown);
		//params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int voldownHight = v.findViewById(R.id.imageVolDown).getMeasuredHeight();
		
		params1.setMargins(0, 0, 0, (int)((imagehight*centerScale/4)+voldownHight));
		center.setLayoutParams(params1);
		
		ObjectAnimator.ofFloat(center, "scaleX", centerScale).setDuration(0).start();
		ObjectAnimator.ofFloat(center, "scaleY", centerScale).setDuration(0).start();
		ObjectAnimator.ofFloat(center, "alpha", alpha).setDuration(0).start();
		

		
//Right	
		rightScale = (float)screenwidth/(float)imagewidth/(float)2;
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	
		params1.setMargins(0, (int)(imagehight*rightScale/4)+imagehight+padding, (int)(imagewidth*rightScale/4), 0);
		
		right.setLayoutParams(params1);

		
		
		
		ObjectAnimator.ofFloat(right, "scaleX", rightScale).setDuration(0).start();
		ObjectAnimator.ofFloat(right, "scaleY", rightScale ).setDuration(0).start();
		ObjectAnimator.ofFloat(right, "alpha", middleAlpha).setDuration(0).start();
		
		
		
//RightRight		
		
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
		rightright.setLayoutParams(params1);
		rightrightScale = 5*screenwidth/imagewidth/19;
		ObjectAnimator.ofFloat(rightright, "scaleX", rightrightScale).setDuration(0).start();
		ObjectAnimator.ofFloat(rightright, "scaleY", rightrightScale ).setDuration(0).start();
		ObjectAnimator.ofFloat(rightright, "alpha", defaultAlpha).setDuration(0).start();
		

		r.addView(leftleft);
		r.addView(left);
		r.addView(center);
		r.addView(right);
		r.addView(rightright);
		

		text = new TextView(v.getContext());
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//params1.addRule(RelativeLayout.ABOVE, R.id.imageVolDown);
		//params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		params1.setMargins(20, screenheight/2-voldownHight, 20, 20);
		
		text.setLayoutParams(params1);

		text.setText(generateText());
		text.setTextColor(0xFFFFFFFF);
		//text.setAlpha(1);

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
		leftleftX = leftleft.getLeft();
		leftleftY = leftleft.getTop();
		
		leftX = left.getLeft();
		leftY = left.getTop();
		//leftScale = left.getScaleX();

		centerX = center.getLeft();
		centerY = center.getTop();
		//centerScale = center.getScaleX();

		rightX = right.getLeft();
		rightY = right.getTop();
		
		rightrightX = rightright.getLeft();
		rightrightY = rightright.getTop();
		//rightScale = right.getScaleX();
	}



	private class AsyncDataLoader extends AsyncTask<Integer, Integer, EPG>{

		@Override
		protected EPG doInBackground(Integer... params) {
			EPGQuery query = new EPGQuery();
			query.getEPG();
			query.getCurrentChannel();
			return query.getEPG();
		}

		@Override
		protected void onPostExecute(EPG epg) {

			if(epg == null){
				//TODO: Save this 
				return;
			}
			v.findViewById(R.id.progressLoadingEPG).setVisibility(View.INVISIBLE);

			currentChannelNr = 0;
			for (Channel channel : epg) {
				Drawable draw = new BitmapDrawable(channel.getIcon());
				ImageView i = new ImageView(v.getContext());
				i.setImageDrawable(draw);	
				i.setBackgroundColor(0xFFFFFFFF );
				i.setAdjustViewBounds(true);
				
				
				int width = draw.getMinimumWidth();
				
				if(imagewidth == -1 && width > 0){
					imagewidth = width;
				}else if(width > 0){
					imagewidth = (imagewidth+width)/2;
				}
				

				int hight = draw.getMinimumHeight();
				
				if(imagehight == -1 && hight > 0){
					imagehight = hight;
				}else if(hight > 0){
					imagehight = (imagehight+hight)/2;
				}
				
				
				i.invalidate();


				imageList.add(i);
				channelList.add(channel);

				i.setOnClickListener(new View.OnClickListener() {
					int i = currentChannelNr;
					@Override
					public void onClick(View v) {
						setChannel(i);
						Channel channel = channelList.get(i);
						RemoteControl.instance().launch(channel.getUrl());
					}
				});

				currentChannelNr++;

			}

			EPGQuery query = new EPGQuery();
			Channel target = query.getCurrentChannel();
			
			currentChannelNr = 24;
			for(int i = 0; i< channelList.size(); i++){
				if(target.getUrl().contains(channelList.get(i).getUrl())){
					currentChannelNr = i;
					break;
				}
			}
			
			
			buildForCurrentChannel();

		}


	}



	







}
