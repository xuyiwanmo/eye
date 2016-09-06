package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.utils.CommonUtils;

/**
 * Created by Administrator on 2016/3/28.
 */
public class WalletLayout extends RelativeLayout {

    ImageView mDotImg;

    TextView mNameTv;

    TextView mCountTv;

    TextView mMoneyTv;

    public WalletLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_view_wallet, this);
        mDotImg = (ImageView) findViewById(R.id.dot_img);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mCountTv = (TextView) findViewById(R.id.count_tv);
        mMoneyTv = (TextView) findViewById(R.id.money_tv);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WalletLayout);

        int imgId = a.getResourceId(R.styleable.WalletLayout_wallet_img, R.drawable.dot_blue);
        mDotImg.setImageResource(imgId);

        String name=a.getString(R.styleable.WalletLayout_wallet_tv);
        if(name!=null){
            mNameTv.setText(name);
        }

    }

    public void setMoneyTv(double money){
        mMoneyTv.setText(CommonUtils.getDecimal("" + money));
    }
    public void setCountTv(int count){
        mCountTv.setText(count + "次");
    }


}
