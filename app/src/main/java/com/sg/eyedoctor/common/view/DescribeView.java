package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class DescribeView extends LinearLayout {


    TextView mTitleTv;

    TextView mDetailTv;

    ImageView mOpenImg;

    public DescribeView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_view_describe, this);

        mTitleTv = (TextView) findViewById(R.id.actionbar_title_tv);
        mDetailTv = (TextView) findViewById(R.id.describe_tv);
        mOpenImg = (ImageView) findViewById(R.id.open_img);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DescribeView);

        String title = a.getString(R.styleable.DescribeView_title_name);
        mTitleTv.setText(title);
        String describe = a.getString(R.styleable.DescribeView_describe);
        mDetailTv.setText(describe);
        boolean show=a.getBoolean(R.styleable.DescribeView_show_open,false);
        if(show){
            mOpenImg.setVisibility(VISIBLE);
        }else{
            mOpenImg.setVisibility(GONE);
        }

    }

    public void setTitleText(String title){
        mTitleTv.setText(title);
    }
    public void setDescribeText(String describe){
        mDetailTv.setHint(describe);
    }


    public void setOpenImgListener(OnClickListener onListener) {
        mOpenImg.setOnClickListener(onListener);
    }

    public ImageView getOpenImg(){
        return mOpenImg;
    }
}
