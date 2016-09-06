package com.sg.eyedoctor.helpUtils.freeClinic.activity;

import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.DescribeView;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_free_video)
public class FreeVideoActivity extends BaseActivity {

    @ViewInject(R.id.open_dv)
    private DescribeView mOpenDv;
    @ViewInject(R.id.price_et)
    private TextView mPriceEt;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private OptionsPickerView<Integer> mNumPickerView;
    private boolean mIsOpen=false;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast("设置成功!");
                mDoctor.videoIsOpen="True";
                //提交了保存
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                AppManager.getAppManager().finishActivity();
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {
        mOpenDv.setOpenImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIsOpen=!mIsOpen;
                mOpenDv.getOpenImg().setSelected(mIsOpen);
                if(mIsOpen){
                    mActionbar.setRightTvVisible(View.VISIBLE);
                }else{
                    mActionbar.setRightTvVisible(View.GONE);
                }

            }
        });



        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            integers.add(i);
        }

        mNumPickerView = new OptionsPickerView<>(mContext);
        mNumPickerView.setPicker(integers);
        mNumPickerView.setCyclic(false);
        mNumPickerView.setCancelable(true);

    }

    @Override
    protected void initListener() {
        mNumPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                mPriceEt.setText((options1+1)+"");
            }
        });
    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNumeric(mPriceEt.getText().toString())) {
                    showdialog();
                    BaseManager.serviceRecordAdd(mPriceEt.getText().toString(),"1",mCallback);

                }else{
                    showToast("请填写就诊数量");
                }
            }
        });

        if(mIsOpen){
            mActionbar.setRightTvVisible(View.VISIBLE);
        }else{
            mActionbar.setRightTvVisible(View.GONE);
        }

    }

    @Event(R.id.price_et)
    private void showNum(View view) {
        mNumPickerView.show();
    }
}
