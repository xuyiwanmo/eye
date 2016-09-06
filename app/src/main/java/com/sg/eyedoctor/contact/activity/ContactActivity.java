package com.sg.eyedoctor.contact.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.TopIndicator;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.contact.fragment.MyFriendFragment;
import com.sg.eyedoctor.contact.fragment.MyHospitalFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 辅助工具-通讯录
 */
@ContentView(R.layout.activity_contact)
public class ContactActivity extends BaseActivity implements TopIndicator.OnTopIndicatorListener{
    @ViewInject(R.id.top_indicator)
    private TopIndicator mTopIndicator;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private MyHospitalFragment mMyHospitalFragment;
    private MyFriendFragment mMyFriendFragment;

    private FragmentManager mFragmentManager;

    private ArrayList<FriendList> mFriendLists;

    @Override
    protected void initView() {

        mTopIndicator.setLabels(getResources().getStringArray(R.array.contact));
        mTopIndicator.setOnTopIndicatorListener(this);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initListener() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mMyFriendFragment = new MyFriendFragment();
        transaction.add(R.id.fl_fragment, mMyFriendFragment);
        transaction.commitAllowingStateLoss();

        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SearchFriendActivity.class);
                intent.putParcelableArrayListExtra(ConstantValues.KEY_DATA, mFriendLists);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void onIndicatorSelected(int index) {

        mTopIndicator.setTabsDisplay(this, index);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);

        switch (index) {
            case 0:
                if (null == mMyFriendFragment) {
                    mMyFriendFragment = new MyFriendFragment();
                    transaction.add(R.id.fl_fragment, mMyFriendFragment);
                } else {
                    transaction.show(mMyFriendFragment);
                  //  mMyFriendFragment.initData();
                }
                break;
            case 1:
                if (null == mMyHospitalFragment) {
                    mMyHospitalFragment = new MyHospitalFragment();
                    transaction.add(R.id.fl_fragment, mMyHospitalFragment);
                } else {
                    transaction.show(mMyHospitalFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mMyFriendFragment) {
            transaction.hide(mMyFriendFragment);
        }
        if (null != mMyHospitalFragment) {
            transaction.hide(mMyHospitalFragment);
        }

    }

}
