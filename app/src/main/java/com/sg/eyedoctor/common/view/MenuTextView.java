package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MenuTextView extends FrameLayout {

    private TextView mShowTv;
    private TextView mOtherTv;
    private RelativeLayout mClickRl;
    private Context mContext;

    public MenuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.custom_view_menu_textview, this);

        mShowTv= (TextView) findViewById(R.id.show_tv);
        mOtherTv= (TextView) findViewById(R.id.other_tv);
        mClickRl= (RelativeLayout) findViewById(R.id.click_rl);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MenuTextView);

        String  rightText = a.getString(R.styleable.MenuTextView_menu_right_text);
        String  leftText= a.getString(R.styleable.MenuTextView_menu_text);


        if(leftText!=null){
            mShowTv.setText(leftText);
        }
        if(rightText!=null){
            mOtherTv.setVisibility(VISIBLE);
            mOtherTv.setText(rightText);
        }else{
            mOtherTv.setVisibility(INVISIBLE);
        }
    }

    public void setShowTv(int stringId){
        mShowTv.setText(stringId);
    }
    public void setShowTv(String string){
        mShowTv.setText(string);
    }
    public void setOthreTv(int stringId){
        mOtherTv.setVisibility(VISIBLE);
        mOtherTv.setText(stringId);
    }
    public void setOtherTv(String string){
        mOtherTv.setVisibility(VISIBLE);
        mOtherTv.setText(string);
    }

    public void setClick(OnClickListener listener){
        mClickRl.setOnClickListener(listener);
    }
}
