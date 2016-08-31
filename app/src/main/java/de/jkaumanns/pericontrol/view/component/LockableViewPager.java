package de.jkaumanns.pericontrol.view.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Joerg on 31.08.2016.
 */
public class LockableViewPager extends ViewPager {

    private boolean swipeLocked;

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isSwipeLocked() {
        return swipeLocked;
    }

    public void setSwipeLocked(boolean swipeLocked) {
        this.swipeLocked = swipeLocked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !swipeLocked && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !swipeLocked && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return !swipeLocked && super.canScrollHorizontally(direction);
    }
}

