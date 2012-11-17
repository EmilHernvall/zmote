package se.z_app.zmote.gui;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SnapHorizontalScrollView extends HorizontalScrollView {
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
 
    private ArrayList mItems = null;
    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;
 
    public SnapHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public SnapHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public SnapHorizontalScrollView(Context context) {
        super(context);
    }
 
    public void setFeatureItems(ArrayList items){
    	int width_screen = getResources().getDisplayMetrics().widthPixels;
        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        addView(internalWrapper);
        this.mItems = items;
        for(int i = 0; i< items.size();i++){
            //LinearLayout featureLayout = (LinearLayout) View.inflate(this.getContext(),R.layout.homefeature,null);
            LinearLayout featureLayout = new LinearLayout(this.getContext(), null);
            featureLayout.setBackgroundColor(0xFFFFFFFF);
            LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(width_screen,200);
            par.setMargins(5, 0, 5, 0);
            
            LinearLayout smallBox = new LinearLayout(this.getContext());
            smallBox.setBackgroundColor(0xFF000000);
            LinearLayout.LayoutParams par2 = new LinearLayout.LayoutParams(190,190);
            featureLayout.addView(smallBox);
            
            //...
          //Create the view for each screen in the scroll view
            //...
            //internalWrapper.addView(featureLayout,par);
            internalWrapper.addView((View) mItems.get(i));
        }System.out.println("Vector size:"+items.size());
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                    int scrollX = getScrollX();
                    int featureWidth = v.getMeasuredWidth();
                    mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
                    int scrollTo = mActiveFeature*featureWidth;
                    smoothScrollTo(scrollTo, 0);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        mGestureDetector = new GestureDetector(new MyGestureDetector());
    }
        class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //right to left
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature < (mItems.size() - 1))? mActiveFeature + 1:mItems.size() -1;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
                //left to right
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth();
                    mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
            } catch (Exception e) {
                    Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
            }
            return false;
        }
    }
}
