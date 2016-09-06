package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeEnglishAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeEnglish;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CharacterParser;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PinyinComparator;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.SearchLayout;
import com.sg.eyedoctor.common.view.SideBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_eye_english)
public class EyeEnglishActivity extends BaseActivity implements SearchLayout.SearchCallback {

    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchEtLl;
    @ViewInject(R.id.no_friends_tv)
    private TextView mEmptyTv;

    @ViewInject(R.id.country_lv)
    private ListView mSortLv;
    @ViewInject(R.id.catalog_tv)
    private TextView mCatlogTv;
    @ViewInject(R.id.sort_sb)
    private SideBar mSortSb;
    @ViewInject(R.id.notice_tv)
    private TextView mNoticeTv;
    @ViewInject(R.id.title_lv)
    private LinearLayout mTitleLl;
    private EyeEnglishAdapter mEnglishAdapter;
    private ArrayList<EyeEnglish> mEyeEnglishs = new ArrayList<>();

    private int mLastFirstVisibleItem = -1;//上次第一个可见元素，用于滚动时记录标识。
    private CharacterParser mCharacterParser;//汉字转换成拼音的类
    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                Type objectType = new TypeToken<BaseArrayResp<EyeEnglish>>() {
                }.getType();
                BaseArrayResp<EyeEnglish> res = new Gson().fromJson(result, objectType);

                mEyeEnglishs = res.value;
                mEnglishAdapter.setData(mEyeEnglishs);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mCharacterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSortSb.setTextView(mNoticeTv);

        mEnglishAdapter = new EyeEnglishAdapter(this, mEyeEnglishs, R.layout.item_eye_english);
        mSortLv.setAdapter(mEnglishAdapter);
        mSearchEtLl.setCallback(this);
        UiUtils.setEmptyText(mContext,mSortLv,"无记录");
        showdialog();
        BaseManager.getEyeWordList(mCallback);

    }

    @Override
    protected void initListener() {

        // 设置右侧触摸监听
        mSortSb.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mEnglishAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mSortLv.setSelection(position);
                }

            }
        });

        mSortLv.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLl
                            .getLayoutParams();
                    params.topMargin = 0;
                    mTitleLl.setLayoutParams(params);
                    mCatlogTv.setText(mEyeEnglishs.get(getPositionForSection(section)).egroup);
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
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLl
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

    }

    @Override
    protected void initActionbar() {

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (mEyeEnglishs.size() <= position || mEyeEnglishs.size() == 0) {
            return -1;
        }
        return mEyeEnglishs.get(position).egroup.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < mEyeEnglishs.size(); i++) {
            String sortStr = mEyeEnglishs.get(i).egroup;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        ArrayList<EyeEnglish> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mEyeEnglishs;
            mEmptyTv.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (EyeEnglish sortModel : mEyeEnglishs) {
                String cname = sortModel.name;
                String ename = sortModel.word;

                if (cname.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSelling(cname).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                } else if (ename.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSelling(cname).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }

            }
        }

        mEnglishAdapter.setData(filterDateList);
        if (filterDateList.size() == 0) {
            mEmptyTv.setVisibility(View.VISIBLE);
        }else{
            mEmptyTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void fillData(String s) {
        filterData(s);
    }

    @Override
    public void cancel() {

        mEnglishAdapter.setData(mEyeEnglishs);
        mEmptyTv.setVisibility(View.GONE);
    }
}


