package se.z_app.zmote.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.api.RCProxy;
import se.z_app.zmote.epg.EPGQuery;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class MainViewFragment extends Fragment{

	private MainTabActivity main;
	private View v;
	private ImageView left;
	private ImageView center;
	private ImageView right;
	private ImageView newLeft;
	private ImageView newRight;
	private RelativeLayout r;
    
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
    
	
	private ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	
	private int currentChannelNr;
	
	
	public MainViewFragment(MainTabActivity main){
		this.main = main;
	}
	
	boolean clicked = false;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		
		v = inflater.inflate(R.layout.fragment_main_view, null);
		
		
		
		
		r = (RelativeLayout)v.findViewById(R.id.rellativIconSpinner);
		
		
		EPGQuery query = new EPGQuery();
		EPG epg = query.getEPG();
		
		for (Channel channel : epg) {
			Drawable draw = new BitmapDrawable(channel.getIcon());
			ImageView i = new ImageView(v.getContext());
			i.setImageDrawable(draw);	
			
			imageList.add(i);
			
		}
		
		//TODO: Featch this one
		currentChannelNr = 4;
	
		
		LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    r.setLayoutParams(params);

	    buildForCurrentChannel();    
	
	    
		Button leftButton=(Button)v.findViewById(R.id.bLeft);
		Button rightButton=(Button)v.findViewById(R.id.bRight);
		
		leftButton.setOnClickListener(new OnClickListener() {
			
			int i = 0;
			@Override
			public void onClick(View v) {
					
					if(!posVar){
						setVariables();
						posVar = true;
					}					
					
					currentChannelNr = (currentChannelNr+1)%imageList.size();
					
					rotateLeft();
				
			}
		});
		
		
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!posVar){
					setVariables();
					posVar = true;
				}
				
				currentChannelNr = (currentChannelNr+imageList.size()-1)%imageList.size();
				
				rotateRight();
				
			}
		});
		
		
		return v;
	}
	
	
	private void rotateRight(){
		newLeft = imageList.get((currentChannelNr+imageList.size()-1)%imageList.size());
		newLeft.setY(-300);
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    newLeft.setLayoutParams(params1);
		newLeft.setVisibility(View.VISIBLE);
		r.addView(newLeft);
		
		
		
		right.animate().y(-300).x(rightX + 300).setListener(new AnimatorListener() {
			ImageView tmp = right;
			
			@Override
			public void onAnimationStart(Animator animation) {}
			
			@Override
			public void onAnimationRepeat(Animator animation) {}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);	
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);						
			}
		});
		
		center.animate().x(rightX).y(rightY).scaleX(rightScale).scaleY(rightScale).setListener(null);
		left.animate().x(centerX).y(centerY).scaleX(centerScale).scaleY(centerScale).setListener(null);
		newLeft.animate().x(leftX).y(leftY).scaleX(leftScale).scaleY(leftScale).setListener(null);
		
		
		
	    right = center;
	    center = left;
	    left = newLeft;
	}
	
	private void rotateLeft(){
		ImageView newRight = imageList.get((currentChannelNr+1)%imageList.size());
		newRight.setY(-300);
		LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    newRight.setLayoutParams(params1);
	    newRight.setVisibility(View.VISIBLE);
		r.addView(newRight);
		
		
		
		left.animate().y(-300).x(-300).setListener(new AnimatorListener() {
			ImageView tmp = left;
			
			@Override
			public void onAnimationStart(Animator animation) {}
			
			@Override
			public void onAnimationRepeat(Animator animation) {}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				r.removeView(tmp);	
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				r.removeView(tmp);						
			}
		});
		
		center.animate().x(leftX).y(leftY).scaleX(leftScale).scaleY(leftScale).setListener(null);
		right.animate().x(centerX).y(centerY).scaleX(centerScale).scaleY(centerScale).setListener(null);
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
	    left.setPadding(10, 10, 10, 10);
	    LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    left.setLayoutParams(params1);
	    

	    params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params1.addRule(RelativeLayout.CENTER_IN_PARENT);
	    center.setLayoutParams(params1);
	    center.setScaleX(4F);
	    center.setScaleY(4F);
	    
	    
	    right.setScaleX(2F);
	    right.setScaleY(2F);
	    right.setPadding(10, 10, 10, 10);
	    params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    right.setLayoutParams(params1);
	    
	    
	    r.addView(left);
	    r.addView(center);
	    r.addView(right);
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
	
	
}
