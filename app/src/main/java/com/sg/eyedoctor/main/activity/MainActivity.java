package com.sg.eyedoctor.main.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.main.fragment.ChartFileFragment;
import com.sg.eyedoctor.main.fragment.EyeCircleFragment;
import com.sg.eyedoctor.main.fragment.HomeFragment;
import com.sg.eyedoctor.main.fragment.MyFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.ll_bottom_home)
    private LinearLayout mHomeLl;
    @ViewInject(R.id.ll_bottom_chart_file)
    private LinearLayout mCrmLl;
    @ViewInject(R.id.ll_bottom_circle)
    private LinearLayout mTeamLl;
    @ViewInject(R.id.ll_bottom_my)
    private LinearLayout mMyLl;

    private HomeFragment mHomeFragment;
    private MyFragment mMyFragment;
    private ChartFileFragment mCrmFragment;
    private EyeCircleFragment mTeamworkFragment;
    private int mIndex = 0;

    private FragmentManager mFragmentManager;


    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDoctor.state == 2) {
            mIsAuth = true;
        } else {
            mIsAuth = false;
        }

        switch (mIndex) {
            case 0:

                onClickBottom(mHomeLl);
                break;
            case 1:
                onClickBottom(mCrmLl);
                break;
            case 2:
                onClickBottom(mTeamLl);
                break;
            case 3:
                onClickBottom(mMyLl);
                break;
        }
    }

    @Event({R.id.ll_bottom_home, R.id.ll_bottom_chart_file, R.id.ll_bottom_my, R.id.ll_bottom_circle})
    private void onClickBottom(View view) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        mHomeLl.setSelected(false);
        mCrmLl.setSelected(false);
        mMyLl.setSelected(false);
        mTeamLl.setSelected(false);
        switch (view.getId()) {
            case R.id.ll_bottom_home:
                mIndex = 0;
                mHomeLl.setSelected(true);
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.fl_fragment, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                    mHomeFragment.initBaseView();
                }
                break;
            case R.id.ll_bottom_chart_file:

                mIndex = 1;
                mCrmLl.setSelected(true);
                if (null == mCrmFragment) {
                    mCrmFragment = new ChartFileFragment();
                    transaction.add(R.id.fl_fragment, mCrmFragment);
                } else {
                    transaction.show(mCrmFragment);
                    mCrmFragment.initData();
                }
                break;
            case R.id.ll_bottom_circle:

                mIndex = 2;
                mTeamLl.setSelected(true);
                if (null == mTeamworkFragment) {
                    mTeamworkFragment = new EyeCircleFragment();
                    transaction.add(R.id.fl_fragment, mTeamworkFragment);
                } else {
                    transaction.show(mTeamworkFragment);
                }
                break;
            case R.id.ll_bottom_my:
                mIndex = 3;
                mMyLl.setSelected(true);
                if (null == mMyFragment) {
                    mMyFragment = new MyFragment();
                    transaction.add(R.id.fl_fragment, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                }
                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题
        super.onSaveInstanceState(outState);
        outState.putInt("index", mIndex);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIndex = savedInstanceState.getInt("index");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            showExitDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mCrmFragment) {
            transaction.hide(mCrmFragment);
        }
        if (null != mTeamworkFragment) {
            transaction.hide(mTeamworkFragment);
        }
        if (null != mMyFragment) {
            transaction.hide(mMyFragment);
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle(R.string.exit_app);
        ad.setMessage(R.string.whether_exit_app);
        ad.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        logout();
                        AppManager.getAppManager().finishActivity();
                    }
                }
        );
        ad.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create().show();
    }


}
