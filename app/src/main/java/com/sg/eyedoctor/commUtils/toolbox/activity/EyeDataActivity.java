package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeDataAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeNV;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_eye_data)
public class EyeDataActivity extends BaseActivity {

    @ViewInject(R.id.eye_data_lv)
    private ListView mDataLv;

    private ArrayList<EyeNV> mEyeDatas=new ArrayList<>();
    private EyeDataAdapter mDataAdapter;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                Type objectType = new TypeToken<BaseArrayResp<EyeNV>>(){}.getType();
                BaseArrayResp<EyeNV> res=new Gson().fromJson(result, objectType);
                mEyeDatas=res.value;
                mDataAdapter.setData(mEyeDatas);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {

        mDataAdapter=new EyeDataAdapter(this,mEyeDatas,R.layout.item_eye_check_data);
        mDataLv.setAdapter(mDataAdapter);

        showdialog();
        BaseManager.getEyeNVAList(mCallback);
    }

    @Override
    protected void initListener() {

        mDataLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EyeNV data=mEyeDatas.get(position);
                Intent intent=new Intent(mContext,WebViewActivity.class);
                intent.putExtra(ConstantValues.KEY_TITLE,data.name);
                intent.putExtra(ConstantValues.KEY_URL,data.webUrl);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }
}
