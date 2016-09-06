package com.sg.eyedoctor.settings.myWallet.fragment;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.settings.myWallet.adapter.IncomeDetailAdapter;
import com.sg.eyedoctor.settings.myWallet.bean.IncomeDetail;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.fragment_month_income_detail)
public class MonthIncomeDetailFragment extends BaseFragment {
    @ViewInject(R.id.lv_couslt)
    private ListView mCousltLv;
    @ViewInject(R.id.price_tv)
    private TextView mPriceTv;

    private ArrayList<IncomeDetail> mIncomeDetails = new ArrayList<>();

    private IncomeDetailAdapter mDetailAdapter;
    private int mType = ConstantValues.TYPE_TEXT;

    private NetCallback mCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<IncomeDetail>>(){}.getType();
                BaseArrayResp<IncomeDetail> res=new Gson().fromJson(result, objectType);
                mIncomeDetails=res.value;
                mDetailAdapter.setData(mIncomeDetails);
                double totalPrice=0;
                for (IncomeDetail incomeDetail : mIncomeDetails) {
                    totalPrice=totalPrice+incomeDetail.orderAmount;
                }
                mPriceTv.setText(String.format("%.2f", totalPrice));
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    public MonthIncomeDetailFragment(int i) {
        mType = i;
    }

    @Override
    protected void initView() {
        mDetailAdapter=new IncomeDetailAdapter(getActivity(),mIncomeDetails,0);
        mCousltLv.setAdapter(mDetailAdapter);
        UiUtils.setEmptyText(getActivity(),mCousltLv,"无记录");
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //判断该Fragment是否已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
        if (isVisibleToUser && isVisible() ) {
            queryData(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void queryData() {
        showLoginDlg();
        BaseManager.getOrderList(mType + "", mCallback);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint() ) {
            queryData(); //加载数据的方法
        }
        super.onActivityCreated(savedInstanceState);
    }
}
