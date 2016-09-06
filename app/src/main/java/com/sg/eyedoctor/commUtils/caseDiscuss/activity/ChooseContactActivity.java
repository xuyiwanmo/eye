package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.ChooseContactAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.HeadGalleyAdapter;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CharacterParser;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PinyinComparator;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.SideBar;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 我的医生好友
 */
@ContentView(R.layout.activity_choose_contact)
public class ChooseContactActivity extends BaseActivity implements SearchLayout.SearchCallback, ChooseContactAdapter.DoctorCheckedCallback {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchLayout;
    @ViewInject(R.id.sort_sb)
    private SideBar mSortSb;
    @ViewInject(R.id.empty_tv)
    private TextView mEmptyTv;
    @ViewInject(R.id.notice_tv)
    private TextView mNoticeTv;
    @ViewInject(R.id.country_lv)
    private ListView mSortLv;
    @ViewInject(R.id.head_rv)
    private RecyclerView mHeadRv;
    @ViewInject(R.id.choose_all_ll)
    private LinearLayout mAllLl;
    @ViewInject(R.id.all_check_img)
    private ImageView mCheckAllImg;
    @ViewInject(R.id.all_choose_tv)
    private TextView mCheckAllTv;

    private CharacterParser mCharacterParser;//汉字转换成拼音的类
    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类
    private ArrayList<FriendList> mTempFriends = new ArrayList<>();

    private ChooseContactAdapter mContactAdapter;
    private HeadGalleyAdapter mHeadAdapter;
    private ArrayList<FriendList> mAllFriends = new ArrayList<>();
    private ArrayList<FriendList> mUpdateFriends = new ArrayList<>();

    private boolean mIsChooseAll = false;
    private String mDisId;

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>() {
                }.getType();
                BaseArrayResp<FriendList> res = new Gson().fromJson(result, objectType);

                mAllFriends = res.value;
                setFriendsChecked();
                filledData();
                if (mDisId != null) {
                    updateData();
                }

                mContactAdapter.setData(mAllFriends);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        //  mActionbar.setTitle(R.string.);
        mDisId = getIntent().getStringExtra(ConstantValues.DIS_ID);

        mSearchLayout.setCallback(this);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(ConstantValues.EXTRA_DATA, mTempFriends);
                setResult(RESULT_OK, intent);
                AppManager.getAppManager().finishActivity();
            }
        });
        mTempFriends = getIntent().getParcelableArrayListExtra(ConstantValues.CHOOSE_HEAD);

        mSortSb.setTextView(mNoticeTv);
        mCharacterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mContactAdapter = new ChooseContactAdapter(mContext, mAllFriends, 0);
        mContactAdapter.setDoctorCheckedCallback(this);
        mSortLv.setAdapter(mContactAdapter);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeadRv.setLayoutManager(linearLayoutManager);
        //设置适配器
        if (mDisId != null) {
            mUpdateFriends.addAll(mTempFriends);
            mTempFriends.clear();
        }
        mHeadAdapter = new HeadGalleyAdapter(this, mTempFriends);
        mHeadRv.setAdapter(mHeadAdapter);

        showdialog();
        BaseManager.circleFriendListFind("", mCallback);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void fillData(String filterStr) {
        if (filterStr == null) {
            mAllLl.setVisibility(View.VISIBLE);
            return;
        }else{
            mAllLl.setVisibility(View.GONE);
        }
        ArrayList<FriendList> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mAllFriends;
            mAllLl.setVisibility(View.VISIBLE);
        } else {
            filterDateList.clear();
            for (FriendList sortModel : mAllFriends) {
                String cname = sortModel.userName;
                if (cname == null) {
                    continue;
                }
                if (cname.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSelling(cname).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        mContactAdapter.setData(filterDateList);
        if (filterDateList.size() == 0) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void cancel() {
        mContactAdapter.setData(mAllFriends);
        mEmptyTv.setVisibility(View.INVISIBLE);
        mAllLl.setVisibility(View.VISIBLE);
    }

    @Event(R.id.choose_all_ll)
    private void choose(View view) {
        String text = mCheckAllTv.getText().toString().trim();

        if (text.equals(getString(R.string.all_choose_false))) {
            mCheckAllTv.setText(R.string.all_choose);
            mTempFriends.clear();
            setAllFriendschoose(false);
            mCheckAllImg.setSelected(false);
        } else if (text.equals(getString(R.string.all_choose))) {
            mCheckAllTv.setText(R.string.all_choose_false);
            setAllFriendschoose(true);
            mTempFriends.clear();
            mTempFriends.addAll(mAllFriends);
            mCheckAllImg.setSelected(true);
        }
        mContactAdapter.notifyDataSetChanged();
        mHeadAdapter.notifyDataSetChanged();
    }


    @Override
    public void setDoctorChecked(boolean isChecked, FriendList checkFriend, int position) {
        for (int i = 0; i < mTempFriends.size(); i++) {
            FriendList friendList = mTempFriends.get(i);
            if (friendList.friendId.equals(checkFriend.friendId)) {  //存在
                if (!isChecked) {//不存在
                    mTempFriends.remove(i);
                }
            }
        }
        for (int i = 0; i < mAllFriends.size(); i++) {
            FriendList friendList = mAllFriends.get(i);
            if (friendList.friendId.equals(checkFriend.friendId)) {  //存在
                friendList.isChecked = isChecked;
            }
        }
        if (isChecked) {
            mTempFriends.add(checkFriend);
        }
        mHeadAdapter.notifyDataSetChanged();
        judgeAllChecked();
    }

    /**
     * 为ListView填充数据
     */
    private void filledData() {
        for (FriendList friend : mTempFriends) {
            // 汉字转换成拼音
            String pinyin = mCharacterParser.getSelling(friend.userName);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                friend.sortLetters = sortString.toUpperCase();
            } else {
                friend.sortLetters = "#";
            }
        }
    }

    private void setFriendsChecked() {
        //清除无用的账户
        for (int i = 0; i < mAllFriends.size(); i++) {
            FriendList friendList = mAllFriends.get(i);
            if (friendList.userName == null || TextUtils.isEmpty(friendList.userName)) {
                mAllFriends.remove(i);
            }
        }

        for (FriendList friendList : mTempFriends) {
            for (FriendList allFriend : mAllFriends) {

                if (allFriend.friendId.equals(friendList.friendId)) {
                    allFriend.isChecked = true;
                }
            }
        }
    }

    //从详情界面跳转
    private void updateData() {
        for (FriendList friendList : mUpdateFriends) {

            for (int i = 0; i < mAllFriends.size(); i++) {
                FriendList allFriend = mAllFriends.get(i);
                if (allFriend.friendId.equals(friendList.friendId)) {
                    mAllFriends.remove(i);
                }
            }
        }
    }

    private void judgeAllChecked() {
        int trueCount = 0;
        int falseCount = 0;

        for (FriendList allFriend : mAllFriends) {
            if (allFriend.isChecked) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        if (trueCount == mAllFriends.size()) {
            //全选
            mCheckAllImg.setSelected(true);
            mCheckAllTv.setText(R.string.all_choose_false);
            mIsChooseAll = !mIsChooseAll;
        } else if (falseCount == mAllFriends.size() - 1) {
            //反选
            mCheckAllImg.setSelected(false);
            mCheckAllTv.setText(R.string.all_choose);
            mIsChooseAll = !mIsChooseAll;
        } else {
            mCheckAllImg.setSelected(false);
            mCheckAllTv.setText(R.string.all_choose);
        }
    }

    public void setAllFriendschoose(boolean checked) {
        for (FriendList allFriend : mAllFriends) {
            allFriend.isChecked = checked;
        }
    }
}
