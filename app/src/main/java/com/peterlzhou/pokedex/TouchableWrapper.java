package com.peterlzhou.pokedex;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }
    public TouchableWrapper(Activity activity) {super(activity);}
    public static OnTouchListener myonTouchListener;

    public void setTouchListener(OnTouchListener onTouchListener) {
        myonTouchListener = onTouchListener;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //System.out.println("I get to the touch event");
        //System.out.println("Event is " + event.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myonTouchListener.onTouch();
                break;
            case MotionEvent.ACTION_UP:
                myonTouchListener.onRelease();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchListener {
        public void onTouch();
        public void onRelease();
    }
}
