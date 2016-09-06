package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeCrystalAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.CrystalParam;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_eye_crystal)
public class EyeCrystalActivity extends BaseActivity {

    @ViewInject(R.id.crystal_lv)
    private ListView mCrystalLv;

    private EyeCrystalAdapter mCrystalAdapter;
    private ArrayList<CrystalParam> mCrystalParams=new ArrayList<>();

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                Type objectType = new TypeToken<BaseArrayResp<CrystalParam>>(){}.getType();
                BaseArrayResp<CrystalParam> res=new Gson().fromJson(result, objectType);
                mCrystalParams=res.value;
                mCrystalAdapter.setData(mCrystalParams);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mCrystalAdapter=new EyeCrystalAdapter(this,mCrystalParams,R.layout.item_eye_check_data);
        mCrystalLv.setAdapter(mCrystalAdapter);

        showdialog();
        BaseManager.getCrystalParamList(mCallback);

    }

    @Override
    protected void initListener() {
        mCrystalLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(ConstantValues.KEY_TITLE, mCrystalParams.get(position).name);
                intent.putExtra(ConstantValues.KEY_URL, mCrystalParams.get(position).webUrl);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initActionbar() {

    }
}
