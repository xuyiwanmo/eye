package com.sg.eyedoctor.consult.advice.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgEdit;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseRowsResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.consult.advice.bean.AdviceDrug;
import com.sg.eyedoctor.helpUtils.doctorAdvice.adapter.AdviceGrugAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 建议用药
 */
@ContentView(R.layout.activity_drug_advice)
public class DrugAdviceActivity extends BaseActivity implements AdviceGrugAdapter.ClickGrug {

    @ViewInject(R.id.drug_lv)
    private PullToRefreshListView mRefreshLv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    private ListView mGrugLv;
    private AdviceGrugAdapter mAdapter;
    private ArrayList<AdviceDrug> mDrugs = new ArrayList<>();
    private ArrayList<AdviceDrug> mChooseGrugs = new ArrayList<>();

    private int mRows = 10;
    private int mPage = 1;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseRowsResp<AdviceDrug>>() {
                }.getType();
                BaseRowsResp<AdviceDrug> res = new Gson().fromJson(result, objectType);

                mDrugs = res.value.rows;
                for (AdviceDrug grug : mDrugs) {
                    grug.count = 0;
                }

                mAdapter.setData(mDrugs);
                LogUtils.i(mDrugs.size() + "");
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
        mGrugLv = mRefreshLv.getRefreshableView();
        mAdapter = new AdviceGrugAdapter(mContext, mDrugs, 0, this);
        mGrugLv.setAdapter(mAdapter);

        showdialog();
        BaseManager.getShareDrug(mPage + "", mRows + "", "", mCallback);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (AdviceDrug drug : mDrugs) {
                    if (drug.count != 0) {
                        mChooseGrugs.add(drug);
                    }
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("data", mChooseGrugs);
                setResult(RESULT_OK, intent);
                AppManager.getAppManager().finishActivity();

            }
        });
    }

    @Override
    public void click(int postion) {
        AdviceDrug grug = mDrugs.get(postion);
        grug.count++;
        LogUtils.i("click: " + grug.count);
        mAdapter.setData(mDrugs);
    }

    @Override
    public void edit(final int postion, String count) {

        DialogManager.showEditDlg(DrugAdviceActivity.this, R.string.enter_count, new DlgEdit() {
            @Override
            public boolean edit(String text) {
                if (text == null || text.equals("")) {
                    return true;
                }
                AdviceDrug grug = mDrugs.get(postion);
                grug.count = Integer.valueOf(text);
                mAdapter.setData(mDrugs);
                return false;
            }
        }, InputType.TYPE_CLASS_NUMBER);


    }
}
