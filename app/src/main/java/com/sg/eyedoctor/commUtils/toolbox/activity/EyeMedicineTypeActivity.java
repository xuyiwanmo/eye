package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeMedicineTypeAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeMedicineType;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_eye_medicine)
public class EyeMedicineTypeActivity extends BaseActivity {
    @ViewInject(R.id.search_tv)
    private TextView mSearchTv;
    @ViewInject(R.id.search_ll)
    private LinearLayout mSearchSl;
    @ViewInject(R.id.type_lv)
    private ListView mTypeLv;
    @ViewInject(R.id.empty_tv)
    private TextView mEmptyTv;
    private EyeMedicineTypeAdapter mTypeAdapter;

    private ArrayList<EyeMedicineType> mEyeMedicineTypes = new ArrayList<>();
    private ArrayList<EyeMedicineType> filterDateList = new ArrayList<>();
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseArrayResp<EyeMedicineType>>() {
                }.getType();
                BaseArrayResp<EyeMedicineType> res = new Gson().fromJson(result, objectType);
                mEyeMedicineTypes = res.value;
                filterDateList = res.value;
                mTypeAdapter.setData(filterDateList);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mTypeAdapter = new EyeMedicineTypeAdapter(this, mEyeMedicineTypes, R.layout.item_eye_check_data);
        mTypeLv.setAdapter(mTypeAdapter);

        showdialog();
        BaseManager.getEyeDrugChannel(mCallback);

    }

    @Override
    protected void initListener() {
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, EyeMedicineDetailActivity.class);
                intent.putExtra(ConstantValues.CHANNELID, filterDateList.get(position).channelId);
                startActivity(intent);
            }
        });

        mSearchSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchMedicineActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void initActionbar() {

    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mEyeMedicineTypes;
            mEmptyTv.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (EyeMedicineType sortModel : mEyeMedicineTypes) {
                String cname = sortModel.channelName;

                if (cname.indexOf(filterStr.toString()) != -1) {
                    filterDateList.add(sortModel);
                }
            }
        }

        mTypeAdapter.setData(filterDateList);
        if (filterDateList.size() == 0) {
            mEmptyTv.setVisibility(View.VISIBLE);
        }
    }

}
