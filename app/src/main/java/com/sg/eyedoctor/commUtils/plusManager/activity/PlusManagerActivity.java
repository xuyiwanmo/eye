package com.sg.eyedoctor.commUtils.plusManager.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.plusManager.adapter.PlusManagerAdapter;
import com.sg.eyedoctor.commUtils.plusManager.bean.GetXtrAppointment;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 加号管理
 */
@ContentView(R.layout.activity_add_manager)
public class PlusManagerActivity extends BaseActivity implements PlusManagerAdapter.AddManagerCallback {
    public static final int ROWS = 7;
    public static int STATE_DELAY = 2;   //延期就诊
    public static int STATE_ALREADY = 1; //已经就诊
    @ViewInject(R.id.add_manager_lv)
    private PullToRefreshListView mPullLv;
    private ListView mAddLv;
    @ViewInject(R.id.set_img)
    private ImageView mSetImg;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private PullState mPullState = PullState.NORMAL;//normal   1下拉  2上拉
    private PlusManagerAdapter mAddManagerAdapter;
    private ArrayList<GetXtrAppointment> mAppointments;
    private int mVisState = STATE_DELAY;
    private int mPosition = 0;
    private int mPage = 1;
    private boolean mIsEnd = false;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                Type objectType = new TypeToken<BaseRowsResp<GetXtrAppointment>>() {
                }.getType();
                BaseRowsResp<GetXtrAppointment> res = new Gson().fromJson(result, objectType);

                switch (mPullState) {
                    case NORMAL:
                        mAppointments.clear();
                        mAppointments = res.value.rows;

                        mAddManagerAdapter.setData(mAppointments);
                        mIsEnd = false;
                        break;
                    case TOP_REFRESH:
                        mAppointments.clear();
                        mAppointments = res.value.rows;

                        mAddManagerAdapter.setData(mAppointments);
                        refreshComplete(mPullLv);
                        mIsEnd = false;
                        break;
                    case END_REFRESH:
                        mAppointments.addAll(res.value.rows);

                        mAddManagerAdapter.setData(mAppointments);
                        refreshComplete(mPullLv);
                        mIsEnd = false;
                        if (mAppointments.size() < ROWS) {
                            showToast(R.string.end_yet);
                            mIsEnd = true;
                        }

                        break;

                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    private NetCallback mAddCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                mPage=0;
                BaseManager.getXtrAppointmentList(mPage + "", ROWS + "", mCallback);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mAppointments = new ArrayList<>();
        mAddLv = mPullLv.getRefreshableView();
        mPullLv.setMode(PullToRefreshBase.Mode.BOTH);
        mAddManagerAdapter = new PlusManagerAdapter(this, mAppointments, R.layout.item_add_manager);
        mAddManagerAdapter.setCallback(this);
        mAddLv.setAdapter(mAddManagerAdapter);
        UiUtils.setEmptyText(mContext, mAddLv, getString(R.string.empty));

        showdialog();
        BaseManager.getXtrAppointmentList(mPage + "", ROWS + "", mCallback);
    }


    @Override
    protected void initListener() {
        mPullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
                mPullState = PullState.TOP_REFRESH;
                BaseManager.getXtrAppointmentList(mPage + "", ROWS + "", mCallback);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mIsEnd) {
                    refreshComplete(refreshView);
                    showToast(R.string.end_yet);
                    return;
                }
                mPage++;
                mPullState = PullState.END_REFRESH;
                BaseManager.getXtrAppointmentList(mPage + "", ROWS + "", mCallback);
            }
        });

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PlusSettingActivity.class));
            }
        });
    }

    @Override
    public void update(int position, int visState) {
        String id = mAppointments.get(position).id;
        mVisState = visState;
        mPosition = position;
        showdialog();
        BaseManager.xtrAppointmentListUpdate(id, visState, mAddCallback);
    }

    public void refreshComplete(
            final PullToRefreshBase<ListView> refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 200);
    }

}
