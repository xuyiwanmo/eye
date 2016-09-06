package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ContentView(R.layout.activity_eye_calculate)
public class EyeCalculateActivity extends BaseActivity {

    @ViewInject(R.id.old_img)
    private ImageView mOldImg;
    @ViewInject(R.id.old_rl)
    private RelativeLayout mOldRl;
    @ViewInject(R.id.age_twl)
    private WheelView mAgeTwl;
    @ViewInject(R.id.degree_twl)
    private WheelView mDegreeTwl;
    @ViewInject(R.id.old_ll)
    private LinearLayout mOldLl;

    @ViewInject(R.id.adjust_img)
    private ImageView mAdjustImg;
    @ViewInject(R.id.adjust_rl)
    private RelativeLayout mAdjustRl;
    @ViewInject(R.id.age_adjust_twl)
    private WheelView mAgeAdjustTwl;
    @ViewInject(R.id.adjust_range_twl)
    private WheelView mAdjustRangeTwl;
    @ViewInject(R.id.adjust_ll)
    private LinearLayout mAdjustLl;

    @ViewInject(R.id.flatten_img)
    private ImageView mFlattenImg;
    @ViewInject(R.id.flatten_rl)
    private RelativeLayout mFlattenRl;
    @ViewInject(R.id.flatten_twl)
    private WheelView mFlattenTwl;
    @ViewInject(R.id.center_flatten_twl)
    private WheelView mCenterFlattenTwl;
    @ViewInject(R.id.flatten_ll)
    private LinearLayout mFlattenLl;
    @ViewInject(R.id.calculate_tv)
    private TextView mCalculateTv;
    @ViewInject(R.id.calculate_et)
    private TextView mCalculateEt;

    @ViewInject(R.id.matrixing_img)
    private ImageView mMatrixingImg;
    @ViewInject(R.id.matrixing_rl)
    private RelativeLayout mMatrixingRl;
    @ViewInject(R.id.degree_one_twl)
    private WheelView mDegreeOneTwl;
    @ViewInject(R.id.degree_two_twl)
    private WheelView mDegreeTwoTwl;
    @ViewInject(R.id.degree_three_twl)
    private WheelView mDegreeThreeTwl;
    @ViewInject(R.id.degree_four_twl)
    private WheelView mDegreeFourTwl;
    @ViewInject(R.id.matrixing_ll)
    private LinearLayout mMatrixingLl;

    @ViewInject(R.id.presure_img)
    private ImageView mPresureImg;
    @ViewInject(R.id.pressure_rl)
    private RelativeLayout mPressureRl;
    @ViewInject(R.id.scale_twl)
    private WheelView mScaleTwl;
    @ViewInject(R.id.ruler_twl)
    private WheelView mRulerTwl;
    @ViewInject(R.id.mmhg_twl)
    private WheelView mMmhgTwl;
    @ViewInject(R.id.kpa_twl)
    private WheelView mKpaTwl;
    @ViewInject(R.id.pressure_ll)
    private LinearLayout mPressureLl;

    @ViewInject(R.id.imgBack)
    private ImageView mBackImg;

    private int mScaleIndex = 0;
    private int mRuleIndex = 0;
    private String mFlattenValue = "1";
    private String mCenterFlattenValue = "445";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //老视度数估算
        initWheelView(mAgeTwl, R.array.old_age, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDegreeTwl.setCurrentItem(index);
            }
        });
        initWheelView(mAgeTwl, R.array.old_age, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDegreeTwl.setCurrentItem(index);
            }

        });
        initWheelView(mDegreeTwl, R.array.old_degree, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mAgeTwl.setCurrentItem(index);
            }

        });

        //年龄与调节幅度

        initWheelView(mAgeAdjustTwl, R.array.old_range, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                mAdjustRangeTwl.setCurrentItem(index);
            }

        });
        initWheelView(mAdjustRangeTwl, R.array.range, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                mAgeAdjustTwl.setCurrentItem(index);
            }

        });


        //角膜厚度

        initWheelView(mFlattenTwl, R.array.eye_presure, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {

                mFlattenValue = getArray(R.array.eye_presure).get(index);
            }


        });

        initWheelView(mCenterFlattenTwl, R.array.center_cornea, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                mCenterFlattenValue = getArray(R.array.center_cornea).get(index);
            }


        });

        //视力换算
        initWheelView(mDegreeOneTwl, R.array.matrixing_presure, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex) {
                mDegreeTwoTwl.setCurrentItem(selectedIndex );
                mDegreeThreeTwl.setCurrentItem(selectedIndex );
                mDegreeFourTwl.setCurrentItem(selectedIndex );
            }

        });

        initWheelView(mDegreeTwoTwl, R.array.eye_age_one, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex) {
                mDegreeOneTwl.setCurrentItem(selectedIndex );

                mDegreeThreeTwl.setCurrentItem(selectedIndex );
                mDegreeFourTwl.setCurrentItem(selectedIndex );
            }

        });

        initWheelView(mDegreeThreeTwl, R.array.eye_age_two, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex) {
                mDegreeOneTwl.setCurrentItem(selectedIndex );
                mDegreeTwoTwl.setCurrentItem(selectedIndex );

                mDegreeFourTwl.setCurrentItem(selectedIndex );
            }
        });

        initWheelView(mDegreeFourTwl, R.array.eye_age_three, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex) {
                mDegreeOneTwl.setCurrentItem(selectedIndex );
                mDegreeTwoTwl.setCurrentItem(selectedIndex );
                mDegreeThreeTwl.setCurrentItem(selectedIndex );
            }
        });


        //眼压计算器

        initWheelView(mScaleTwl, R.array.scale, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mScaleIndex = index;
                mMmhgTwl.setCurrentItem(mScaleIndex + mRuleIndex);
                mKpaTwl.setCurrentItem(mScaleIndex + mRuleIndex);
            }

        });
        initWheelView(mRulerTwl, R.array.ruler, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mRuleIndex = index;
                mMmhgTwl.setCurrentItem(mScaleIndex + mRuleIndex );
                mKpaTwl.setCurrentItem(mScaleIndex + mRuleIndex );
            }

        });

        initWheelView(mMmhgTwl, R.array.scale, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

            }


        });
        initWheelView(mKpaTwl, R.array.ruler, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {

            }
        });

    }

    private void initWheelView(WheelView WheelView, int arrayId, OnItemSelectedListener onToolScrollListener) {

        WheelView.setAdapter(new ArrayWheelAdapter<String>(getStringArray(arrayId)));
        WheelView.setCyclic(false);
        WheelView.setCurrentItem(0);
        WheelView.setOnItemSelectedListener(onToolScrollListener);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Event({R.id.old_rl, R.id.adjust_rl, R.id.flatten_rl, R.id.matrixing_rl, R.id.pressure_rl})
    private void unfold(View view) {

        switch (view.getId()) {

            case R.id.old_rl:
                setImgSecectedVisible(mOldImg, mOldLl);
                break;
            case R.id.adjust_rl:
                setImgSecectedVisible(mAdjustImg, mAdjustLl);
                break;
            case R.id.flatten_rl:
                setImgSecectedVisible(mFlattenImg, mFlattenLl);
                break;
            case R.id.matrixing_rl:
                setImgSecectedVisible(mMatrixingImg, mMatrixingLl);
                break;
            case R.id.pressure_rl:
                setImgSecectedVisible(mPresureImg, mPressureLl);
                break;
        }
    }

    @Event(R.id.calculate_tv)
    private void calculate(View view) {

        float f = (float) (Float.parseFloat(mFlattenValue) - 0.05D * (Float.parseFloat(mCenterFlattenValue) - 545.0F));
        mCalculateEt.setText(f + "");
    }

    private void setImgSecectedVisible(ImageView imageView, LinearLayout linearLayout) {
        boolean isSecected = false;
        if (imageView.getTag() != null) {
            isSecected = (boolean) imageView.getTag();
        }
        imageView.setSelected(!isSecected);
        imageView.setTag(!isSecected);

        if (linearLayout.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private List<String> getArray(int arrayId) {
        return Arrays.asList(getResources().getStringArray(arrayId));
    }

    private ArrayList<String> getStringArray(int arrayId) {
        String[] strings = getResources().getStringArray(arrayId);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (String string : strings) {
            stringArrayList.add(string);
        }
        return stringArrayList;
    }


}
