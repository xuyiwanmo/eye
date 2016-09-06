package com.sg.eyedoctor.consult.textConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 图文咨询-病人列表
 */
public class ConsultPatientAdapter extends CommAdapter<Patient> {


    public ConsultPatientAdapter(Context context, ArrayList<Patient> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Patient patient, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_consult_patient, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNameTv.setText(patient.patientName);

        if (patient.sex != null && patient.sex.equals("1")) {
            holder.mSexTv.setText("男");
        } else {
            holder.mSexTv.setText("女");
        }

        holder.mAgeTv.setText(patient.age + "岁");
        holder.mTimeTv.setText(patient.latestDate);
        holder.mIllnessTv.setText("症状描述: " + patient.description);

        if (!patient.newMessage.equals("0")) {
            holder.mCountTv.setText(patient.newMessage);
            holder.mCountTv.setVisibility(View.VISIBLE);
        } else {
            holder.mCountTv.setVisibility(View.INVISIBLE);
        }

        if (patient.state == 4) {
            holder.mCountTv.setVisibility(View.INVISIBLE);
        }
        if (patient.picFileName != null) {
            CommonUtils.loadImg(patient.picFileName, holder.mHeadImg, R.drawable.ic_patient_head);
        }

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.head_img)
        ImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.tv_consult_patient_sex)
        TextView mSexTv;
        @ViewInject(R.id.tv_consult_patient_age)
        TextView mAgeTv;
        @ViewInject(R.id.tv_consult_patient_time)
        TextView mTimeTv;
        @ViewInject(R.id.tv_consult_patient_illness)
        TextView mIllnessTv;
        @ViewInject(R.id.read_count_tv)
        TextView mCountTv;
    }
}
