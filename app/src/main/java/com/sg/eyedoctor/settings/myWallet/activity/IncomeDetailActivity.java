package com.sg.eyedoctor.settings.myWallet.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.settings.myWallet.adapter.IncomeDetailAdapter;
import com.sg.eyedoctor.settings.myWallet.bean.IncomeDetail;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 收入明细
 */
@ContentView(R.layout.activity_income_detail)
public class IncomeDetailActivity extends BaseActivity {
    @ViewInject(R.id.third_money_tv)
    private TextView mThirdMoneyTv;
    @ViewInject(R.id.third_ll)
    private LinearLayout mThirdLl;
    @ViewInject(R.id.third_lv)
    private NoScrollListView mThirdLv;
    @ViewInject(R.id.second_month_tv)
    private TextView mSecondMonthTv;
    @ViewInject(R.id.second_money_tv)
    private TextView mSecondMoneyTv;
    @ViewInject(R.id.second_ll)
    private LinearLayout mSecondLl;
    @ViewInject(R.id.second_lv)
    private NoScrollListView mSecondLv;
    @ViewInject(R.id.first_month_tv)
    private TextView mFirstMonthTv;
    @ViewInject(R.id.first_money_tv)
    private TextView mFirstMoneyTv;
    @ViewInject(R.id.first_ll)
    private LinearLayout mFirstLl;
    @ViewInject(R.id.first_lv)
    private NoScrollListView mFirstLv;

    private ArrayList<IncomeDetail> mFirstIncomes = new ArrayList<>();
    private ArrayList<IncomeDetail> mSecondIncomes = new ArrayList<>();
    private ArrayList<IncomeDetail> mThirdIncomes = new ArrayList<>();
    private ArrayList<IncomeDetail> mAllIncomes = new ArrayList<>();

    private IncomeDetailAdapter mFirstAdapter;
    private IncomeDetailAdapter mSecondAdapter;
    private IncomeDetailAdapter mThirdAdapter;

    private int mThirdMonth;
    private int mSecondMonth;
    private int mFirstMonth;

    private double mThirdMoney;
    private double mFirstMoney;
    private double mSecondMoney;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<IncomeDetail>>() {
                }.getType();
                BaseArrayResp<IncomeDetail> res = new Gson().fromJson(result, objectType);
                mAllIncomes = res.value;
                fillData();
                mFirstAdapter.setData(mFirstIncomes);
                mSecondAdapter.setData(mSecondIncomes);
                mThirdAdapter.setData(mThirdIncomes);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {
        mFirstAdapter = new IncomeDetailAdapter(this, mFirstIncomes, 0);
        mSecondAdapter = new IncomeDetailAdapter(this, mSecondIncomes, 0);
        mThirdAdapter = new IncomeDetailAdapter(this, mThirdIncomes, 0);

        Calendar cal = Calendar.getInstance();
        mThirdMonth = cal.get(Calendar.MONTH) + 1;
        mSecondMonth = mThirdMonth - 1;
        mFirstMonth = mThirdMonth - 2;

        mFirstAdapter = new IncomeDetailAdapter(mContext, mFirstIncomes, 0);
        mSecondAdapter = new IncomeDetailAdapter(mContext, mSecondIncomes, 0);
        mThirdAdapter = new IncomeDetailAdapter(mContext, mThirdIncomes, 0);

        mFirstLv.setAdapter(mFirstAdapter);
        mSecondLv.setAdapter(mSecondAdapter);
        mThirdLv.setAdapter(mThirdAdapter);
    }

    private void fillData() {
        for (IncomeDetail allIncome : mAllIncomes) {

            if (CommonUtils.getMonth(allIncome.createDate) == mFirstMonth) {
                mFirstIncomes.add(allIncome);
                mFirstMoney = mFirstMoney + allIncome.orderAmount;
                continue;
            }
            if (CommonUtils.getMonth(allIncome.createDate)  == mSecondMonth) {
                mSecondIncomes.add(allIncome);
                mSecondMoney = mSecondMoney + allIncome.orderAmount;
                continue;
            }
            if (CommonUtils.getMonth(allIncome.createDate) == mThirdMonth) {
                mThirdIncomes.add(allIncome);
                mThirdMoney = mThirdMoney + allIncome.orderAmount;
                continue;
            }
        }

        mFirstMoneyTv.setText("合计: " +String.format("%.2f", mFirstMoney)  + "元");
        mSecondMoneyTv.setText("合计: " + String.format("%.2f", mSecondMoney) + "元");
        mThirdMoneyTv.setText("合计: " + String.format("%.2f", mThirdMoney) + "元");

        mFirstMonthTv.setText(CommonUtils.getChineseMonth(mFirstMonth));
        mSecondMonthTv.setText(CommonUtils.getChineseMonth(mSecondMonth));
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
        showdialog();
        BaseManager.getOrderList("0", mCallback);
    }

    private void showNoData() {
        if (mFirstIncomes.size() == 0) {
            mFirstLv.setVisibility(View.GONE);
            mFirstLl.setVisibility(View.GONE);
        }
        if (mSecondIncomes.size() == 0) {
            mSecondLl.setVisibility(View.GONE);
            mSecondLv.setVisibility(View.GONE);
        }
        if (mThirdIncomes.size() == 0) {
            mThirdLl.setVisibility(View.GONE);
            mThirdLv.setVisibility(View.GONE);
        }
    }
}

