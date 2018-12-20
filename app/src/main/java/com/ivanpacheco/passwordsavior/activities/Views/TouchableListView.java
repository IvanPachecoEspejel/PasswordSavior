package com.ivanpacheco.passwordsavior.activities.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.ivanpacheco.LoggerLib.Logger;

/**
 * Created by ivanpacheco on 4/04/18.
 *
 */

public class TouchableListView extends ListView {

    private static final Logger LOG = new Logger(TouchableListView.class.getName());
    private Boolean isPerformClickCalled = false;

    public TouchableListView(Context context) {
        super(context);
    }

    public TouchableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean performClick(){
        boolean result = super.performClick();
        LOG.i("Perform click result: "+result);
        return true;
    }

    @Override
    public boolean performItemClick(View v, int position, long id) {
        boolean result = super.performItemClick(v, position, id);
        LOG.i("Perform item click result: "+result);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        LOG.i("On touch event result: "+result+" Action: "+ev.getActionMasked());
        return result;
    }
}
