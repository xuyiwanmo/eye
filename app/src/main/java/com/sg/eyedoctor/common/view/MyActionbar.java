package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyActionbar extends FrameLayout {

    private ImageView mLeftBtn;
    private TextView mActionbarTitle;
    private TextView mRightTv;
    private TextView mLeftTv;
    private String mTitle;
    private ImageView mRightBtn;
    private ImageView mSecondBtn;
    private Context mContext;


    public MyActionbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.custom_view_actionbar, this);
        mLeftBtn = (ImageView) findViewById(R.id.back_img);
        mActionbarTitle = (TextView) findViewById(R.id.actionbar_title_tv);
        mRightTv = (TextView) findViewById(R.id.right_tv);
        mLeftTv = (TextView) findViewById(R.id.left_tv);
        mRightBtn = (ImageView) findViewById(R.id.action_img);
        mSecondBtn = (ImageView) findViewById(R.id.second_img);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyActionbar);

        String title = a.getString(R.styleable.MyActionbar_actionbar_title);
        if (title != null) {
            mActionbarTitle.setText(title);
        }

        boolean rightBtnVisible = a.getBoolean(R.styleable.MyActionbar_rightBtn_visible, true);
        if (rightBtnVisible) {
            mRightBtn.setVisibility(VISIBLE);
        }else{
            mRightBtn.setVisibility(INVISIBLE);
        }


        int rightImgId=a.getResourceId(R.styleable.MyActionbar_rightBtn_img,0);
        if(rightImgId!=0){
            mRightBtn.setVisibility(VISIBLE);
            mRightBtn.setImageResource(rightImgId);
        }
        int secondImgId=a.getResourceId(R.styleable.MyActionbar_secondBtn_img,0);
        if(rightImgId!=0){
            mSecondBtn.setVisibility(VISIBLE);
            mSecondBtn.setImageResource(secondImgId);
        }
        boolean backBtnVisible = a.getBoolean(R.styleable.MyActionbar_backBtn_visible, true);
        if (backBtnVisible) {
            mLeftBtn.setVisibility(VISIBLE);
        }else{
            mLeftBtn.setVisibility(INVISIBLE);
        }
        mLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) context).finish();
            }
        });

    }

    public void setRightClick(OnClickListener onClickListener) {
        mRightBtn.setOnClickListener(onClickListener);
    }

    public void setRightBtnVisible(int visible) {
        mRightBtn.setVisibility(visible);
    }

    public void setLeftbtnVisible(int visible) {
        mLeftBtn.setVisibility(visible);
    }

    public void setSecondbtnVisible(int visible) {
        mSecondBtn.setVisibility(visible);
    }

    public void setTitle(String text) {
        mActionbarTitle.setText(text);
    }

    public void setTitle(int textId) {
        mActionbarTitle.setText(textId);
    }
    public void setTitleVisible(int visible) {
        mActionbarTitle.setVisibility(visible);
    }

    public void setRightBtnImg(int imageId, OnClickListener onClickListener) {
        mRightBtn.setVisibility(VISIBLE);
        mRightBtn.setImageResource(imageId);
        mRightBtn.setOnClickListener(onClickListener);
    }

    public ImageView getRightBtnImg() {
        return mRightBtn;
    }
    public ImageView getSecondBtnImg() {
        return mSecondBtn;
    }

    public void setSecondBtnImg(int imageId, OnClickListener onClickListener) {
        mSecondBtn.setImageResource(imageId);
        mSecondBtn.setOnClickListener(onClickListener);
    }

    public void setRightTv(int stringId, OnClickListener onClickListener) {
        this.setRightTv(mContext.getResources().getString(stringId),onClickListener);
    }
    public void setRightTv(String string, OnClickListener onClickListener) {
        mRightTv.setVisibility(VISIBLE);
        mRightTv.setText(string);
        mRightTv.setOnClickListener(onClickListener);
    }

    public void setLeftTv(int stringId, OnClickListener onClickListener) {
        mLeftTv.setVisibility(VISIBLE);
        mLeftBtn.setVisibility(INVISIBLE);
        mLeftTv.setText(stringId);
        mLeftTv.setOnClickListener(onClickListener);
    }
    public void setLeftBtnListener(OnClickListener onClickListener) {

        mLeftBtn.setVisibility(VISIBLE);

        mLeftBtn.setOnClickListener(onClickListener);
    }

    public void setRightTvVisible(int rightTvVisible) {
        mRightTv.setVisibility(rightTvVisible);
    }
}
