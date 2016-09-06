package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class BlueTextView extends RelativeLayout {


    TextView mNameTv;

    public BlueTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_textview_blue, this);
        mNameTv = (TextView) findViewById(R.id.name_tv);


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BlueTextView);

        int color = a.getResourceId(R.styleable.BlueTextView_text_color, R.color.text_body_strong);
        mNameTv.setTextColor(getResources().getColor(color));

        int size = a.getDimensionPixelSize(R.styleable.BlueTextView_text_size, 16);
        mNameTv.setTextSize(size);

        String name = a.getString(R.styleable.BlueTextView_text_content);
        if (name != null) {
            mNameTv.setText(name);
        }
    }


}
