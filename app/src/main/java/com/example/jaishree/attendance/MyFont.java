package com.example.jaishree.attendance;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jaishree on 14-06-2017.
 */

public class MyFont extends TextView {

    public MyFont(Context context, AttributeSet arrts, int defstyles){
        super(context,arrts,defstyles);
        init();
    }

    public MyFont(Context context,AttributeSet arrts){
        super(context,arrts);
        init();
    }


    public MyFont(Context context){
        super(context);
        init();
    }

    private void init() {
        if(!isInEditMode()){
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/Calling Angels Personal Use.ttf");
            setTypeface(tf);
        }
    }
}
