package com.sg.eyedoctor.lookPicture.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.photoview.HackyViewPager;
import com.sg.eyedoctor.lookPicture.adapter.ImageScaleAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 浏览大图
 */
@ContentView(R.layout.activity_look_big_pic)
public class LookBigPicActivity extends BaseActivity implements ViewTreeObserver.OnPreDrawListener
        , HackyViewPager.HackyViewPagerDispatchListener {

    public static String PICDATALIST = "PICDATALIST";
    public static String CURRENTITEM = "CURRENTITEM";
    public static String EDIT = "edit";

    @ViewInject(R.id.viewpager)
    private HackyViewPager viewPager;
    @ViewInject(R.id.ll_dots)
    private LinearLayout ll_dots;
    @ViewInject(R.id.ll_root)
    private LinearLayout ll_root;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.ll_bottom_all)
    private LinearLayout ll_bottom_all;

    private ArrayList<PicLocalBean> picDataList;
    private ArrayList<String> deletePath = new ArrayList<>();
    private List<View> dotList = new ArrayList<>();
    private ImageScaleAdapter imageScaleAdapter;
    private int currentItem;
    public int mPositon;
    private boolean edit;

    @Override
    protected void initView() {
        getData();
        setUpEvent();
        initDot(currentItem);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        edit = getIntent().getBooleanExtra(LookBigPicActivity.EDIT, false);
        if (edit) {
            mActionbar.setRightTv(R.string.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int size = picDataList.size();
                    PicLocalBean bean = picDataList.get(mPositon);
                    if (bean.picUrl != null) {
                        deletePath.add(bean.picUrl);
                    } else if (bean.localUrl != null) {
                        deletePath.add(bean.localUrl);
                    }

                    if (size == 1) {//直接返回  删除最后一张
                        finishCurrentActivity();
                    }
                    picDataList.remove(mPositon);

                    mPositon = mPositon - 1;
                    if (mPositon < 0) {
                        mPositon = 0;
                    }
                    currentItem = mPositon;
                    imageScaleAdapter.setData(picDataList);
                    setUpEvent();
                }
            });
        }

        mActionbar.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCurrentActivity();
            }
        });
    }

    private void finishCurrentActivity() {
        Intent data = new Intent();
        data.putStringArrayListExtra(ConstantValues.KEY_DELETE, deletePath);
        setResult(RESULT_OK, data);
        finish();
    }

    private void setUpEvent() {
        viewPager.setmHackyViewPagerDispatchListener(this);
        viewPager.setAdapter(imageScaleAdapter);
        viewPager.setCurrentItem(currentItem);
        setTitleNum(currentItem);
        setPagerChangeListener(viewPager);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        picDataList = (ArrayList<PicLocalBean>) intent.getSerializableExtra(PICDATALIST);
        currentItem = intent.getIntExtra(CURRENTITEM, 0);
        mPositon = currentItem;
        imageScaleAdapter = new ImageScaleAdapter(this, picDataList);
    }

    /**
     * 绘制前开始动画
     */
    @Override
    public boolean onPreDraw() {
        return true;
    }

    /**
     * 页面改动监听
     *
     * @param viewPager
     */
    private void setPagerChangeListener(HackyViewPager viewPager) {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPositon = position;
                setTitleNum(position);
                initDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setTitleNum(int position) {
        mActionbar.setTitle((position + 1) + "/" + picDataList.size());
    }

    /**
     * 初始化轮播图点
     */
    private void initDot(int index) {
        // 清空点所在集合
        dotList.clear();
        ll_dots.removeAllViews();
        for (int i = 0; i < picDataList.size(); i++) {
            ImageView view = new ImageView(this);
            if (i == index || picDataList.size() == 1) {
                view.setBackgroundResource(R.mipmap.type_selected);
            } else {
                view.setBackgroundResource(R.mipmap.type_normal);
            }
            // 指定点的大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    CommonUtils.dip2px(this, 5), CommonUtils.dip2px(this, 5));
            // 指定点的间距
            layoutParams.setMargins(CommonUtils.dip2px(this, 2), 0, CommonUtils.dip2px(this, 2), 0);
            // 添加到线性布局中
            ll_dots.addView(view, layoutParams);
            // 添加到集合中去
            dotList.add(view);
        }
    }
    @Override
    public void isDown() {

    }

    @Override
    public void isUp() {

    }

    @Override
    public void isCancel() {

    }


    @Override
    public void onBackPressed() {
        if (edit) {//可编辑
            finishCurrentActivity();
        }
        super.onBackPressed();
    }

}
