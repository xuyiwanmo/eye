package com.sg.eyedoctor.commUtils.outPatientRecord.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.outPatientRecord.bean.AppointPatient;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 我的患者
 * Created by zhanghua on 2015/12/31.
 */
public class OutPatientAdapter extends CommAdapter<AppointPatient> {

    private boolean mIsvisable;

    public OutPatientAdapter(Context context, ArrayList<AppointPatient> list, int layoutId) {
        super(context, list, layoutId);
    }

    public OutPatientAdapter(Context context, ArrayList<AppointPatient> list, boolean b) {
        super(context,list,0);
        this.mIsvisable=b;
    }

    @Override
    protected View convert(AppointPatient patient, View convertView, Context context, int position, int layoutId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_my_patient, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if(mIsvisable){
            viewHolder.mAddImgTv.setVisibility(View.VISIBLE);
            viewHolder.mTimeTv.setText(mContext.getText(R.string.out_patient_time));
            viewHolder.mAppointmentTimeTv.setText(patient.apmDate);
        }else{
            viewHolder.mAddImgTv.setVisibility(View.INVISIBLE);
            viewHolder.mTimeTv.setText(mContext.getText(R.string.appointment_time));
            viewHolder.mAppointmentTimeTv.setText(patient.apmDate);
        }
        viewHolder.mNameTv.setText(patient.patientName);
        viewHolder.mIdcardTv.setText(patient.idCard);

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_my_patient_name)
        TextView mNameTv;
        @ViewInject(R.id.tv_patient_idcard)
        TextView mIdcardTv;
        @ViewInject(R.id.tv_patient_appointment_time)
        TextView mAppointmentTimeTv;
        @ViewInject(R.id.tv_my_patient_time)
        TextView mTimeTv;
        @ViewInject(R.id.img_my_patient_add)
        ImageView mAddImgTv;
    }
}
