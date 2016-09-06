package com.sg.eyedoctor.contact.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CharacterParser;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PinyinComparator;
import com.sg.eyedoctor.common.view.SideBar;
import com.sg.eyedoctor.contact.adapter.FriendListAdapter;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 好友列表
 */
@ContentView(R.layout.activity_friend_list)
public class FriendListActivity extends BaseActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA";

    @ViewInject(R.id.add_friend_ll)
    private LinearLayout mAddLl;
    @ViewInject(R.id.search_ll)
    private LinearLayout mSearchLl;

    @ViewInject(R.id.country_lv)
    private ListView mSortLv;
    @ViewInject(R.id.sort_sb)
    private SideBar mSortSb;
    @ViewInject(R.id.notice_tv)
    private TextView mNoticeTv;
    @ViewInject(R.id.title_lv)
    private LinearLayout mTitleLl;
    @ViewInject(R.id.catalog_tv)
    private TextView mCatlogTv;
    @ViewInject(R.id.no_friends_tv)
    private TextView mNameTv;
    @ViewInject(R.id.imgBack)
    private ImageView mBackImg;

    private ArrayList<FriendList> mFriends=new ArrayList<>();

    private FriendListAdapter mFriendAdapter;
    private int mLastFirstVisibleItem = -1;//上次第一个可见元素，用于滚动时记录标识。
    private CharacterParser mCharacterParser;//汉字转换成拼音的类
    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类

    private boolean mIsChooseFriend = false;

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>(){}.getType();
                BaseArrayResp<FriendList> res=new Gson().fromJson(result, objectType);
                mFriends.clear();
                mFriends.addAll(res.value);
                filledData();
                mFriendAdapter.setData(mFriends);
            }

        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.circleFriendListFind("", mCallback);
    }

    @Override
    protected void initView() {
        mFriends = new ArrayList<>();
        // 实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSortSb.setTextView(mNoticeTv);
        filledData();
        // 根据a-z进行排序源数据
        Collections.sort(mFriends, pinyinComparator);
        mFriendAdapter = new FriendListAdapter(this, mFriends, R.layout.item_friend_list);
        mSortLv.setAdapter(mFriendAdapter);
    }

    @Override
    protected void initListener() {
        // 设置右侧触摸监听
        mSortSb.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mFriendAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mSortLv.setSelection(position);
                }

            }
        });

        mSortLv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = getSectionForPosition(firstVisibleItem);
                if (section == -1) {
                    return;
                }
                if (firstVisibleItem != mLastFirstVisibleItem) {
                    MarginLayoutParams params = (MarginLayoutParams) mTitleLl
                            .getLayoutParams();
                    params.topMargin = 0;
                    mTitleLl.setLayoutParams(params);
                    mCatlogTv.setText(mFriends.get(getPositionForSection(section)).sortLetters);
                }

                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (nextSection == -1) {
                    return;
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = mTitleLl.getHeight();
                        int bottom = childView.getBottom();
                        MarginLayoutParams params = (MarginLayoutParams) mTitleLl
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            mTitleLl.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                mTitleLl.setLayoutParams(params);
                            }
                        }
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });

        mSortLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mIsChooseFriend) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA,mFriends.get(position));
                    setResult(RESULT_OK, intent);
                    AppManager.getAppManager().finishActivity();
                }
            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.add_friend_ll)
    private void addFriend(View view) {
        startActivity(new Intent(this, NewFriendActivity.class));
    }

    @Event(R.id.search_ll)
    private void searchFriend(View view) {
        startActivity(new Intent(this, AddFriendActivity.class));
    }


    /**
     * 为ListView填充数据
     */
    private void filledData() {

        for (FriendList friend : mFriends) {
            if(friend.userName==null){
                mFriends.remove(friend);
                continue;
            }
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

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (mFriends.size() <= position || mFriends.size() == 0) {
            return -1;
        }
        return mFriends.get(position).sortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < mFriends.size(); i++) {
            String sortStr = mFriends.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }



}
