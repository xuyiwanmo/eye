package com.sg.eyedoctor.consult.videoConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.DescribeView;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 开启视频问诊 设置数量
 */
@ContentView(R.layout.activity_video_setting)
public class VideoSettingActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.count_et)
    private TextView mCountEt;
    @ViewInject(R.id.open_dv)
    private DescribeView mOpenDv;

    private OptionsPickerView<String> mPickerView;
    private ArrayList<String> mNums=new ArrayList<>();

    private boolean mIsOpenFree=false;

    /**
     * 问诊Id
     */
    private String mXtrId;
    //确认提交
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                try {
                    JSONObject object=new JSONObject(result);
                    mXtrId=object.getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(mXtrId!=null){
                    //跳转界面
                    Intent intent=new Intent(mContext,VideoConsultActivity.class);
                    intent.putExtra(ConstantValues.KEY_XTR_ID,mXtrId);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity();
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
        mXtrId=getIntent().getStringExtra(ConstantValues.KEY_XTR_ID);
        for (int i = 1; i <=10; i++) {
            mNums.add(i + "");
        }
        mPickerView=new OptionsPickerView<>(mContext);
        mPickerView.setPicker(mNums);
        mPickerView.setCyclic(false);
        mPickerView.setCancelable(true);

        mOpenDv.setOpenImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOpenFree = !mIsOpenFree;
                mOpenDv.setSelected(mIsOpenFree);
            }
        });

    }

    @Override
    protected void initListener() {
        mPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mCountEt.setText(mNums.get(options1));
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = mCountEt.getText().toString();

                if (count == null || count.equals("")) {
                    showToast("请填写就诊数量!");
                    return;
                }

                //提交
                showdialog();
                if(mIsOpenFree){
                   //义诊为1
                    LogUtils.i("义诊");
                    BaseManager.serviceRecordAdd(count,"1", mCallback);
                }else{
                    LogUtils.i("非义诊");
                    BaseManager.serviceRecordAdd(count,"0", mCallback);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showdialog();
//        BaseManager.getServiceRemind(mConcernCallback);

    }
    @Event(R.id.count_et)
    private void showNum(View view) {
        mPickerView.show();
    }
}
