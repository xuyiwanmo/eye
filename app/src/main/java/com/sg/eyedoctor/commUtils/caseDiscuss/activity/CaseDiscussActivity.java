package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.TeamMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuCreator;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.CaseDiscussAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscuss;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * 病历讨论
 */
@ContentView(R.layout.activity_case_discuss)
public class CaseDiscussActivity extends BaseActivity {
    public static final int ROWS = 20;
    public static final int TYPE = 1;

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.case_ptrasmlv)
    private SwipeMenuListView mCaseLv;
    private int mCurDeletePostiion = 0;
    private CaseDiscuss caseDiscuss;
    private int mCurrPage = 1;
    private ArrayList<CaseDiscuss> mCaseDiscusses = new ArrayList<>();
    private CaseDiscussAdapter mDiscussAdapter;
    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<CaseDiscuss>>() {
                }.getType();
                BaseArrayResp<CaseDiscuss> res = new Gson().fromJson(result, objectType);
                mCaseDiscusses = res.value;
                Collections.sort(mCaseDiscusses, new CaseDiscussSort());
                mDiscussAdapter.setData(mCaseDiscusses);

//                switch (mPullState) {
//                    case NORMAL:
//
//                        mCaseDiscusses = res.value;
//                        mDiscussAdapter.setData(mCaseDiscusses);
//                        break;
//                    case TOP_REFRESH:
//                        mCaseDiscusses = res.value;
//                        mDiscussAdapter.setData(mCaseDiscusses);
//                     //   refreshComplete(mSwipeMenuListView);
//                        break;
//                    case END_REFRESH:
//                        mCaseDiscusses.addAll(res.value);
//                        mDiscussAdapter.setData(mCaseDiscusses);
//
//                      //  refreshComplete(mSwipeMenuListView);
////                        if (res.value.size() < ROWS) {
////                            showToast(R.string.end_yet);
////                        }

                 //       break;
              //  }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mDeteleCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                mCaseDiscusses.remove(mCurDeletePostiion);
                mDiscussAdapter.setData(mCaseDiscusses);
            } else {
                showToast(R.string.delete_error);
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
        BaseManager.discussionListFind(mCurrPage + "", ROWS + "","", mCallback);
    }

    @Override
    protected void initView() {
        mActionbar.setTitle(R.string.case_discussing);
        mActionbar.setRightBtnImg(R.drawable.ic_add_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddCaseActivity.class));

            }
        });
    //    mCaseLv = mSwipeMenuListView.getRefreshableView();
        mDiscussAdapter = new CaseDiscussAdapter(this, mCaseDiscusses, 0);
        mCaseLv.setAdapter(mDiscussAdapter);
        initSwipeMenu();

        UiUtils.setEmptyText(mContext, mCaseLv, getString(R.string.empty));
    }

    @Override
    protected void initListener() {
//        mSwipeMenuListView.setMode(PullToRefreshBase.Mode.BOTH);
//        mSwipeMenuListView.setPullLabel("上拉加载", PullToRefreshBase.Mode.PULL_FROM_END);
//        mSwipeMenuListView.setPullLabel("下拉刷新", PullToRefreshBase.Mode.PULL_FROM_START);
//        mSwipeMenuListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
//                mCurrPage = 1;
//                mPullState = PullState.TOP_REFRESH;
//                BaseManager.discussionListFind(mCurrPage + "", ROWS + "", mCallback);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
//                mCurrPage++;
//                mPullState = PullState.END_REFRESH;
//                BaseManager.discussionListFind(mCurrPage + "", ROWS + "", mCallback);
//            }
//        });

        mCaseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CaseDiscuss discuss=mCaseDiscusses.get(position);

//                //查询所有成员
//                NIMClient.getService(TeamService.class).queryMemberList(discuss.teamId)
//                        .setCallback(new RequestCallback<List<TeamMember>>() {
//                            @Override
//                            public void onSuccess(List<TeamMember> members) {
//                                for (TeamMember member : members) {
//                                    LogUtils.i(member.getAccount()+"  是否在组里:  "+member.isInTeam());
//                                }
//                            }
//
//                            @Override
//                            public void onFailed(int i) {
//
//                            }
//
//                            @Override
//                            public void onException(Throwable throwable) {
//
//                            }
//                        });
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    return;
                }
                TeamMessageActivity.start(mContext, discuss.teamId,discuss.patientName,SessionHelper.getTeamCustomization(discuss.disId), null);
//                Intent intent = new Intent(mContext, CaseChatActivity.class);
//                intent.putExtra(AddCaseActivity.DIS_ID, mCaseDiscusses.get(position).disId);
//                intent.putExtra(AddCaseActivity.DIS_NAME, mCaseDiscusses.get(position).patientName);
//                startActivity(intent);
            }
        });

        mCaseLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        caseDiscuss = mCaseDiscusses.get(position);
                        mCurDeletePostiion = position;

                        DialogManager.showConfimCancelDlg(mContext, R.string.delete_confirm, new DlgClick() {
                            @Override
                            public boolean click() {
                                //删除讨论组中 自己
                                showdialog();

                                NIMClient.getService(TeamService.class).quitTeam(caseDiscuss.teamId).setCallback(new RequestCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        LogUtils.i("-----退出讨论组成功");
                                        BaseManager.discussionMemberDelete(caseDiscuss.disId, mDeteleCallback);
                                    }

                                    @Override
                                    public void onFailed(int i) {

                                    }

                                    @Override
                                    public void onException(Throwable throwable) {

                                    }
                                });

                                return false;
                            }
                        }, new DlgClick() {
                            @Override
                            public boolean click() {
                                return false;
                            }
                        });

//                        mGroupPosition = groupPosition;
//                        mChildPosition = childPosition;
//                        PatientByGroup deletePatient = mPatients.get(groupPosition).get(childPosition);

                        break;

                }
                //   mAdapter.notifyDataSetChanged();
            }
        });

        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,SearchDiscussCaseActivity.class));
            }
        });


    }

    @Override
    protected void initActionbar() {

    }

    private void initSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 两种获取屏幕宽度的方式
                int width1 = getWindowManager().getDefaultDisplay().getWidth();
                int width2 = getResources().getDisplayMetrics().widthPixels;

                SwipeMenuItem item2 = new SwipeMenuItem(mContext);

                item2.setBackground(R.color.pink);
                item2.setImageId(R.drawable.ic_delete_normal);
                item2.setTitle(R.string.exit_case_discuss);
                item2.setTitleColor(R.color.white);
                item2.setTitleSize(10);
                item2.setWidth(width2 / 5);

                menu.addMenuItem(item2);
            }
        };
        mCaseLv.setMenuCreator(creator);
    }

    public void refreshComplete(
            final PullToRefreshBase<?> refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 1000);
    }

    class CaseDiscussSort implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/M/d H:mm:ss");
            Date date1 = null;
            Date date2 = null;
            if(((CaseDiscuss) time1).latestDate==null){
                return 1;
            }
            if(((CaseDiscuss) time2).latestDate==null){
                return 1;
            }
            try {
                date1=format.parse(((CaseDiscuss) time1).latestDate.replace("-","/"));
                date2=format.parse(((CaseDiscuss) time2).latestDate.replace("-","/"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long flag =date1.compareTo(date2);
            if (flag > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }


}
