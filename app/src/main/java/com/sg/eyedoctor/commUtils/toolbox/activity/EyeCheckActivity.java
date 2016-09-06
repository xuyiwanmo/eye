package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.EyeCheckAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeCheck;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

@ContentView(R.layout.activity_eye_check)
public class EyeCheckActivity extends BaseActivity {
    @ViewInject(R.id.imgBack)
    private ImageView mBackImg;

    @ViewInject(R.id.eye_check_lv)
    private ListView mCheckLv;

    private EyeCheckAdapter mEyeCheckDataAdapter;
    private ArrayList<EyeCheck> mEyeChecks = new ArrayList<>();
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                Type objectType = new TypeToken<BaseArrayResp<EyeCheck>>(){}.getType();
                BaseArrayResp<EyeCheck> res=new Gson().fromJson(result, objectType);
                mEyeChecks=res.value;
                mEyeCheckDataAdapter.setData(mEyeChecks);

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        mEyeCheckDataAdapter = new EyeCheckAdapter(this, mEyeChecks, R.layout.item_eye_check);
        mCheckLv.setAdapter(mEyeCheckDataAdapter);

        showdialog();
        BaseManager.getEyeCheckList(mCallback);

    }

    @Override
    protected void initListener() {
        mCheckLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, LookBigPicActivity.class);
                ArrayList<PicLocalBean> pictures = new ArrayList<>();
                EyeCheck eyeCheck = mEyeChecks.get(position);

                for (EyeCheck.PiclistEntity url : eyeCheck.piclist) {
                    pictures.add(new PicLocalBean(url.picUrl,url.picUrl));
                }
              //  intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                intent.putExtra(LookBigPicActivity.PICDATALIST, pictures);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

}
