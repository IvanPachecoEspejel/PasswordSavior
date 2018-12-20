package com.ivanpacheco.passwordsavior.activities.Listeners;


import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.greenrobot.greendao.annotation.OrderBy;


/**
 * Created by ivanpacheco on 2/04/18.
 *
 */

public abstract class OnSwipeTouchListener implements View.OnTouchListener {

    private ListView list;
    private GestureDetector gestureDetector;

    protected OnSwipeTouchListener(Context ctx, ListView list) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.list = list;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        return gestureDetector.onTouchEvent(event);
    }

    public abstract void onSwipeRight(int pos);

    public abstract void onSwipeLeft(int pos);

    public abstract void onSwipeDown(int pos);

    public abstract void onSwipeTop(int pos);

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private  int SWIPE_THRESHOLD_X = 100;
        private int SWIPE_VELOCITY_THRESHOLD_X = 100;
        private  int SWIPE_THRESHOLD_Y = 300;
        private int SWIPE_VELOCITY_THRESHOLD_Y = 300;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPostion(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if((Math.abs(distanceX) > SWIPE_THRESHOLD_X && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD_X)){
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if (distanceX > 0)
                        onSwipeRight(getPostion(e1));
                    else
                        onSwipeLeft(getPostion(e1));
                }
                return true;
            }
            if(Math.abs(distanceY) > SWIPE_THRESHOLD_Y && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD_Y){
                if(Math.abs(distanceX) < Math.abs(distanceY)){
                    if(distanceY > 0)
                        onSwipeDown(getPostion(e1));
                    else
                        onSwipeTop(getPostion(e1));
                }
            }
            return false;
        }

    }
}