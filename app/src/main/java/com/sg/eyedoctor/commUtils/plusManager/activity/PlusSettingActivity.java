package com.sg.eyedoctor.commUtils.plusManager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.plusManager.bean.XtrAppointment;
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

/**
 * 加号设置
 */
@ContentView(R.layout.activity_add_setting)
public class PlusSettingActivity extends BaseActivity {

    @ViewInject(R.id.set_rl)
    private RelativeLayout mSetRl;
    @ViewInject(R.id.price_tv)
    private TextView mPriceTv;

    private ArrayList<XtrAppointment> mAppointments;

    private int mClickPosition = 0;
    private int mCurrent = 0;
    private XtrAppointment mCurrentAppoint ;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();

                Type objectType = new TypeToken<BaseArrayResp<XtrAppointment>>() {}.getType();
                BaseArrayResp<XtrAppointment> res = new Gson().fromJson(result, objectType);
                mAppointments = res.value;
                fillData();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mDeleteCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                closeDialog();
                showToast(R.string.close_ok);
                TextView childView = (TextView) mSetRl.getChildAt(mCurrent);
                childView.setTag(R.id.tag_value, false);
                childView.setText(R.string.click_set);
                childView.setTextColor(getcolor(R.color.actionbar_color));
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setTextView(mPriceTv,mDoctor.addPrice,"0.01");
        showdialog();
        BaseManager.getXtrAppointment(mCallback);
    }

    @Override
    protected void initView() {

        for (mClickPosition = 0; mClickPosition < mSetRl.getChildCount(); mClickPosition++) {

            TextView childView = (TextView) mSetRl.getChildAt(mClickPosition);
            childView.setTag(R.id.tag_position, mClickPosition);
            childView.setTag(R.id.tag_value, false);
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrent = (int) v.getTag(R.id.tag_position);

                    if ((boolean) v.getTag(R.id.tag_value)) {
                        showCloseOrUpdateDlg();
                    } else {
                        //弹出对话框
                        Intent intent = new Intent(mContext, EditPlusInfoActivity.class);
                        intent.putExtra(ConstantValues.KEY_POSITON, mCurrent);
                        startActivity(intent);

                    }
                }
            });

        }
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    private void deleteAppoint() {
        for (XtrAppointment appointment : mAppointments) {
            if(appointment.orderby==mCurrent){
                showdialog();
                BaseManager.xtrAppointmentRemove(appointment.id,mDeleteCallback);
            }
        }
    }
    private void fillData() {
        for (XtrAppointment appointment : mAppointments) {

            TextView childView = (TextView) mSetRl.getChildAt(appointment.orderby);
            childView.setTag(R.id.tag_value, true);
            String type = "";

            switch (appointment.xtrType) {
                case EditPlusInfoActivity.NORMAL:
                    type = getString(R.string.normal);
                    break;
                case EditPlusInfoActivity.PROFESSOR:
                    type = getString(R.string.professor);

                    break;
                case EditPlusInfoActivity.SPECIAL_PROCUREMENT:
                    type = getString(R.string.special_procurement);

                    break;
                case EditPlusInfoActivity.COLLEGE:
                    type = getString(R.string.college);

                    break;
                case EditPlusInfoActivity.INTERNATION:
                    type = getString(R.string.internation);

                    break;
            }
            childView.setText(type + "号 " + appointment.xtrAccount + "个");
            childView.setTextColor(getcolor(R.color.actionsheet_red));
        }
    }


    private void showCloseOrUpdateDlg() {
        new AlertDialog.Builder(mContext)//设置对话框标题
                .setMessage(R.string.close_or_update)//设置显示的内容
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//修改update
                        for (XtrAppointment appointment : mAppointments) {
                            if(appointment.orderby==mCurrent){
                                mCurrentAppoint=appointment;
                            }
                        }
                        Intent intent = new Intent(mContext, EditPlusInfoActivity.class);
                        intent.putExtra(ConstantValues.KEY_UPDATE,mCurrentAppoint);
                        intent.putExtra(ConstantValues.KEY_POSITON, mCurrent);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {//关闭.删除
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAppoint();

            }
        }).show();

    }

}
