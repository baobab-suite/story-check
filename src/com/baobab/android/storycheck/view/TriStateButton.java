package com.baobab.android.storycheck.view;

import com.baobab.android.storycheck.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/07
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriStateButton extends Button{

    private static final int[] DONE_STATE_SET = {  R.attr.state_item_done};
    private static final int[] NA_STATE_SET = {  R.attr.state_item_na};
    int state;

    public TriStateButton(Context context) {
        super(context);
    }

    public TriStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TriStateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
     protected int[] onCreateDrawableState(int extraSpace) {
         final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        switch (state){
            case 1:
                mergeDrawableStates(drawableState, DONE_STATE_SET);
            break;
            case 2:
                mergeDrawableStates(drawableState, NA_STATE_SET);
            break;
        }
         return drawableState;
     }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        invalidate();
    }
}
