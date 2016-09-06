package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class OnlineManagerLayout extends RelativeLayout {

    ImageView mLeftImg;

    TextView mNameTv;
    TextView mPriceTv;
    TextView mDescribeTv;

    public OnlineManagerLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_view_online_manager, this);
        mLeftImg = (ImageView) findViewById(R.id.img);
        mNameTv = (TextView) findViewById(R.id.actionbar_title_tv);
        mPriceTv = (TextView) findViewById(R.id.price_tv);
        mDescribeTv = (TextView) findViewById(R.id.service_tv);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.OnlineManagerLayout);

        int imgId = a.getResourceId(R.styleable.OnlineManagerLayout_left_img, R.drawable.home_text_consult);
        mLeftImg.setImageResource(imgId);

        String price=a.getString(R.styleable.OnlineManagerLayout_price);
        if(price!=null){
            mPriceTv.setText(price);
        }
        String name=a.getString(R.styleable.OnlineManagerLayout_online_name);
        if(price!=null){
            mNameTv.setText(name);
        }
        String service=a.getString(R.styleable.OnlineManagerLayout_service);
        if(service!=null){
            mDescribeTv.setText(service);
        }

    }

    public void setPriceTv(String price){
        mPriceTv.setText(price);
    }
    public void setServiceTv(String service){
        mDescribeTv.setText(service);
    }
    public void setTitleTv(String name){
        mNameTv.setText(name);
    }



}
