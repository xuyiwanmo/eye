package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeMedicineDetailAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeMedicineDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_search_medicine)
public class SearchMedicineActivity extends BaseActivity implements SearchLayout.SearchCallback {

    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;
    @ViewInject(R.id.crystal_lv)
    private ListView mDetailLv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private EyeMedicineDetailAdapter mDetailAdapter;
    private ArrayList<EyeMedicineDetail> mMedicineDetails = new ArrayList<>();


    private String mChannelId;
    private String mChannelName;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseArrayResp<EyeMedicineDetail>>(){}.getType();
                BaseArrayResp<EyeMedicineDetail> res=new Gson().fromJson(result, objectType);
                mMedicineDetails=res.value;
                mDetailAdapter.setData(mMedicineDetails);

                if(mMedicineDetails.size()==0){
                    showToast("未搜索到该药品");
                }
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mSearchSl.setCallback(this);
        mChannelId = getIntent().getStringExtra(ConstantValues.CHANNELID);
        mChannelName = getIntent().getStringExtra(ConstantValues.CHANNELNAME);

        mDetailAdapter = new EyeMedicineDetailAdapter(mContext, mMedicineDetails, R.layout.item_eye_check_data);
        mDetailLv.setAdapter(mDetailAdapter);
    }

    @Override
    protected void initListener() {
        mDetailLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(ConstantValues.KEY_TITLE, mMedicineDetails.get(position).name);
                intent.putExtra(ConstantValues.KEY_URL, mMedicineDetails.get(position).webUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    public void fillData(String s) {
        if(TextUtils.isEmpty(s)){
            showToast("请输入药品关键字");
            return;
        }
        showdialog();
        BaseManager.searchEyeDrugList(s,mCallback);

    }

    @Override
    public void cancel() {
        mMedicineDetails.clear();
        mDetailAdapter.setData(mMedicineDetails);
    }
}
