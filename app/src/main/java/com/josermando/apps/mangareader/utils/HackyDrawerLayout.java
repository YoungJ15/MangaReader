package com.josermando.apps.mangareader.utils;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Josermando Peralta on 5/4/2016.
 */
public class HackyDrawerLayout extends DrawerLayout {
    public HackyDrawerLayout(Context context) {
        super(context);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        /**catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }**/
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            //uncomment if you really want to see these errors
            e.printStackTrace();
            return false;
        }
    }
}
