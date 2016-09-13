package com.sg.eyedoctor.contact.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.P2PMessageActivity;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuCreator;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CharacterParser;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PinyinComparator;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.SideBar;
import com.sg.eyedoctor.contact.activity.AddFriendActivity;
import com.sg.eyedoctor.contact.activity.NewFriendActivity;
import com.sg.eyedoctor.contact.adapter.FriendListAdapter;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 通讯录-我的好友
 */

@ContentView(R.layout.fragment_my_friend)
public class MyFriendFragment extends BaseFragment implements SectionIndexer {

    @ViewInject(R.id.country_lv)
    private SwipeMenuListView mSortLv;
    @ViewInject(R.id.sort_sb)
    private SideBar mSortSb;
    @ViewInject(R.id.notice_tv)
    private TextView mNoticeTv;
    @ViewInject(R.id.all_tv)
    private TextView mAllCountTv;

    private ArrayList<FriendList> mFriends = new ArrayList<>();

    private FriendListAdapter mFriendAdapter;
    private int mLastFirstVisibleItem = -1;//上次第一个可见元素，用于滚动时记录标识。
    private CharacterParser mCharacterParser;//汉字转换成拼音的类
    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类

    private boolean mIsChooseFriend = false;
    private int mDeletePosition;

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<FriendList>>() {
                }.getType();
                BaseArrayResp<FriendList> res = new Gson().fromJson(result, objectType);
                mFriends.clear();
                mFriends.addAll(res.value);
                filledData();
                mFriendAdapter.setData(mFriends);
                mAllCountTv.setText("所有联系人" + " " + mFriends.size());

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mDeleteCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast("删除好友成功!");
                mFriends.remove(mDeletePosition);
                filledData();
                mFriendAdapter.setData(mFriends);
            } else {
                showToast("删除失败,请稍后重试!");
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mReadCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {

            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    public MyFriendFragment() {
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
        mFriendAdapter = new FriendListAdapter(getActivity(), mFriends, R.layout.item_friend_list);
        mFriendAdapter.setShowLetter(false);
        mSortLv.setAdapter(mFriendAdapter);

        initSwipeMenu();
    }

    @Override
    protected void initListener() {
        mSortLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                DialogManager.showConfimCancelDlg(getActivity(), R.string.delete_confirm, new DlgClick() {
                    @Override
                    public boolean click() {
                        String friendId = mFriends.get(position).friendId;
                        mDeletePosition = position;
                        showLoginDlg();
                        BaseManager.circleFriendDelete(friendId, mDeleteCallback);

                        return false;
                    }
                }, new DlgClick() {
                    @Override
                    public boolean click() {
                        return false;
                    }
                });
            }
        });

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
        mSortLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    return;
                }

                FriendList friend = mFriends.get(position);
                friend.loginid = friend.loginId;
                if (!friend.newMessage.equals("0")) {
                    BaseManager.setQuestionMessageIsRead(friend.id, "d" + friend.loginId, "d"+BaseManager.getDoctor().id, mReadCallback);
                }
                P2PMessageActivity.start(getActivity(), "d" + friend.loginId, SessionHelper.getDoctorP2pCustomization(friend), friend);
            }
        });

        UiUtils.setEmptyText(getActivity(), mSortLv, getString(R.string.empty));

        //initData();

    }

    @Event(R.id.new_friend_tv)
    private void newFriend(View view) {
        startActivity(new Intent(getActivity(), NewFriendActivity.class));
    }

    @Event(R.id.search_ll)
    private void searchFriend(View view) {
        startActivity(new Intent(getActivity(), AddFriendActivity.class));
    }


    /**
     * 为ListView填充数据
     */
    private void filledData() {
        int size = mFriends.size();
        for (int i = 0; i < size; i++) {

            FriendList friend = mFriends.get(i);
            if (friend.userName == null||friend.friendId.equals(BaseManager.getDoctor().id)) {
                mFriends.remove(i);
                i--;
                size--;
            }
        }

        for (FriendList friend : mFriends) {
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

    @Override
    public Object[] getSections() {
        return new Object[0];
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


    public void queryData() {
        showLoginDlg();
        BaseManager.circleFriendListFind("", mCallback);
    }

    private void initSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 两种获取屏幕宽度的方式
                int width1 = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                int width2 = getResources().getDisplayMetrics().widthPixels;

                SwipeMenuItem item2 = new SwipeMenuItem(getActivity());

                item2.setBackground(R.color.pink);
                item2.setIcon(R.drawable.ic_delete_normal);
                item2.setWidth(width2 / 5);

                menu.addMenuItem(item2);
            }
        };
        mSortLv.setMenuCreator(creator);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible()) {

            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint()) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            queryData();
        }
    }
}
