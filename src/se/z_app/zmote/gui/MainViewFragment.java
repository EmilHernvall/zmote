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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * A fragment containing the main view (the view were one can scroll between channels)
 * @author Rasmus Holm
 *
 */
public class MainViewFragment extends Fragment implements OnGestureListener{

	private MainTabActivity main;
	private View v;
	private ImageView left;
	private ImageView leftleft;
	private ImageView center;
	private ImageView right;
	private ImageView rightright;

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

	private ProgressBar programProgress; 
	private TextView programText;
	private LinearLayout programWrapper;
	private TextView channelName;

	private ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	private ArrayList<Channel> channelList = new ArrayList<Channel>();

	private int currentChannelNr;

	/**
	 * Constructor for the MainViewFragment, 
	 * @param main the tab
	 */
	public MainViewFragment(MainTabActivity main){
		this.main = main;
	}

	boolean clicked = false;


	float threshhold = 20;
	boolean activeGest = false;

	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		if(activeGest){
			float currentX = event.getX();
			float currentY = event.getY(); 

			float dirX = x-currentX;
			float dirY = y-currentY;


			if(Math.abs(dirX) > threshhold || Math.abs(dirY) > threshhold ){
				activeGest = false;
				if(Math.abs(dirX) > Math.abs(dirY)){	
					//Changing Channel from swipe
					if(dirX < 0 )
						setChannel(currentChannelNr-1);
					else if (dirX > 0)	
						setChannel(currentChannelNr+1);

				}else{
					//Changing Volume from swipe
					if(dirY < 0 )
						RemoteControl.instance().sendButton(Button.VOLMINUS);
					else if (dirY > 0)
						RemoteControl.instance().sendButton(Button.VOLPLUS);
				}
			}
		}

	}
	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

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

		//		if(r != null){
		//			r.removeView(left);
		//			r.removeView(leftleft);
		//			r.removeView(center);
		//			r.removeView(rightright);
		//			r.removeView(right);
		//		}

		r = (RelativeLayout)v.findViewById(R.id.rellativIconSpinner);
		GestureOverlayView gestures = (GestureOverlayView) v.findViewById(R.id.gestures);
		gestures.addOnGestureListener(this);	
		new AsyncDataLoader().execute();

		return v;
	}

	/**
	 * A setter for the channel
	 * @param target The channel to be set
	 * @return A boolean, false if animation is running, true otherwise
	 */
	public boolean setChannel(Channel target){
		if (!isAnimationRunning) {	
			for(int i = 0; i< channelList.size(); i++){
				if(channelList.get(i).getUrl().contains(target.getUrl())){
					setChannel(i);
					return true;
				}
			}
		}
		return false;		
	}

	/**
	 * A setter for the channel
	 * @param channelNr The channelNr to be set
	 */
	public void setChannel(int channelNr){		
		if(currentChannelNr == channelNr){
			return;
		}
		int can1 = 0;
		int can2 = 0;
		int fin = 0;
		int size = imageList.size(); 
		channelNr = (channelNr+size)%size;

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

		//	Button b;
		if(fin < 0){
			rotateRight(fin*(-1));
			//		b = Button.CHANNELMINUS;
		}
		else{
			rotateLeft(fin);
			//		b = Button.CHANNELPLUS;
		}

		//	for(int i = 0; i < Math.abs(fin); i++){
		//		RemoteControl.instance().sendButton(b);
		//	}

		Channel channel = channelList.get(channelNr);
		RemoteControl.instance().launch(channel);
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
				hideText();
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
					showText();
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
					showText();
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
				hideText();
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
					showText();
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
					showText();
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

		int padding = 10;

		//TODO: imageList == 0 a bug appears, this happens when it dose not get connection to a STB		

		if(imageList.isEmpty()){
			return;
		}
		leftleft = imageList.get((currentChannelNr+imageList.size()-2)%imageList.size());
		left = imageList.get((currentChannelNr+imageList.size()-1)%imageList.size());
		center = imageList.get(currentChannelNr);
		right = imageList.get((1+currentChannelNr)%imageList.size());
		rightright = imageList.get((2+currentChannelNr)%imageList.size());


		screenwidth = r.getMeasuredWidth();
		screenheight = r.getMeasuredHeight();

		Log.i("Screen", "Screen width = " + screenwidth);
		Log.i("Screen", "Screen height = " + screenheight);
		Log.i("Screen", "Image width = " + imagewidth);
		Log.i("Screen", "Image hight = " + imagehight);

		//LeftLeft

		leftleftScale = 5*screenwidth/imagewidth/19; //screenheight/imagehight/6;
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins((int)(imagewidth*(leftleftScale-1)/2), (int)(imagehight*(leftleftScale-1)/2), 0, 0);
		//params1.setMargins((int)(imagewidth*leftleftScale/4), (int)((imagehight*leftleftScale/4)), 0, 0);
		leftleft.setLayoutParams(params1);

		ObjectAnimator.ofFloat(leftleft, "scaleX", leftleftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(leftleft, "scaleY", leftleftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(leftleft, "alpha", defaultAlpha).setDuration(0).start();


		//Left		
		leftScale = 3*(float)screenwidth/(float)imagewidth/(float)7;
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params1.setMargins((int)(imagewidth*leftScale/4), (int)((imagehight*leftScale/2)+imagehight*leftleftScale/2+padding), 0, 0);

		left.setLayoutParams(params1);

		ObjectAnimator.ofFloat(left, "scaleX", leftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(left, "scaleY", leftScale).setDuration(0).start();
		ObjectAnimator.ofFloat(left, "alpha", middleAlpha).setDuration(0).start();


		//Center
		centerScale = (float) (screenwidth/imagewidth);
		int centerHight = (int)(imagehight*centerScale);

		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params1.addRule(RelativeLayout.ABOVE, R.id.imageVolDown);
		//params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		int voldownHight = v.findViewById(R.id.imageVolDown).getMeasuredHeight();

		params1.setMargins(0, 0, 
				0, voldownHight + (int)((imagehight*(centerScale)/4)));
		center.setLayoutParams(params1);

		ObjectAnimator.ofFloat(center, "scaleX", centerScale).setDuration(0).start();
		ObjectAnimator.ofFloat(center, "scaleY", centerScale).setDuration(0).start();
		ObjectAnimator.ofFloat(center, "alpha", alpha).setDuration(0).start();


		//RightRight		

		rightrightScale = 5*screenwidth/imagewidth/19;
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.setMargins(0, (int)(imagehight*(rightrightScale-1)/2), (int)(imagewidth*(rightrightScale-1)/2), 0);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
		rightright.setLayoutParams(params1);

		ObjectAnimator.ofFloat(rightright, "scaleX", rightrightScale).setDuration(0).start();
		ObjectAnimator.ofFloat(rightright, "scaleY", rightrightScale ).setDuration(0).start();
		ObjectAnimator.ofFloat(rightright, "alpha", defaultAlpha).setDuration(0).start();

		//Right	
		rightScale = 3*(float)screenwidth/(float)imagewidth/(float)7;
		params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params1.setMargins(0, (int)((imagehight*rightScale/2)+imagehight*rightrightScale/2+padding), (int)(imagewidth*(rightScale-1)/2), 0);
		right.setLayoutParams(params1);

		ObjectAnimator.ofFloat(right, "scaleX", rightScale).setDuration(0).start();
		ObjectAnimator.ofFloat(right, "scaleY", rightScale ).setDuration(0).start();
		ObjectAnimator.ofFloat(right, "alpha", middleAlpha).setDuration(0).start();
		//		r.removeView(leftleft);
		//		r.removeView(left);
		//		r.removeView(center);
		//		r.removeView(right);
		//		r.removeView(rightright);

		r.addView(leftleft);
		r.addView(left);
		r.addView(center);
		r.addView(right);
		r.addView(rightright);

		programWrapper = new LinearLayout(r.getContext());
		programWrapper.setOrientation(LinearLayout.VERTICAL);
		params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params1.setMargins(20, screenheight-centerHight-voldownHight+2*padding, 20, voldownHight);

		programWrapper.setLayoutParams(params1);

		channelName = new TextView(v.getContext());
		channelName.setTextColor(0xFFFFFFFF);
		programProgress = new ProgressBar(v.getContext(), null, android.R.attr.progressBarStyleHorizontal);	
		programText = new TextView(v.getContext());
		programText.setTextColor(0xFFFFFFFF);

		showText();

		programWrapper.addView(channelName);
		programWrapper.addView(programProgress);
		programWrapper.addView(programText);

		r.addView(programWrapper);
	}

	private void hideText(){
		programWrapper.setVisibility(View.INVISIBLE);
	}

	private void showText(){
		String t = "";
		Channel channel = channelList.get(currentChannelNr);
		channelName.setText(channel.getName());

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
			return;

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

		programText.setText(t);

		long startMilliTime = currentProgram.getStart().getTime();
		long endMilliTime = startMilliTime + currentProgram.getDuration()*1000;
		long nowMilliTime = System.currentTimeMillis();

		programProgress.setMax((int)(endMilliTime-startMilliTime));
		programProgress.setProgress((int)(nowMilliTime-startMilliTime));

		programWrapper.setVisibility(View.VISIBLE);
		programWrapper.bringToFront();

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
						if(i != currentChannelNr && !isAnimationRunning){
							main.vibrate();
							setChannel(i);
						}
					}
				});

				currentChannelNr++;

			}

			EPGQuery query = new EPGQuery();
			Channel target = query.getCurrentChannel();
			currentChannelNr = 0;
			if(target != null){
				for(int i = 0; i< channelList.size(); i++){
					if(channelList.get(i) != null)
						if(channelList.get(i).getUrl() != null)
							if(target.getUrl().contains(channelList.get(i).getUrl())){
								currentChannelNr = i;
								break;
							}	
				}
			}

			buildForCurrentChannel();

		}

	}

}
