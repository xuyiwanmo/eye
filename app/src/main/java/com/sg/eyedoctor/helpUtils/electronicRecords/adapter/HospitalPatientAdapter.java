package com.sg.eyedoctor.helpUtils.electronicRecords.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatient;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;



/**
 * Created by Administrator on 2016/2/22.
 */

public class HospitalPatientAdapter extends CommAdapter<HospitalPatient> {

    public HospitalPatientAdapter(Context context, ArrayList<HospitalPatient> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(HospitalPatient doctor, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_hospital_patient, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mBedTv,doctor.bedNumber);
        setTextView(holder.mHospitalTv,doctor.hospitalized);
        setTextView(holder.mQualityTv,doctor.chargeName);
        setTextView(holder.mAgeTv, CommonUtils.dateToAge(doctor.patientBirt)+"岁");
        setTextView(holder.mOweTv, doctor.moneyLeft<0?"是":"否");
        setTextView(holder.mNameTv, doctor.patientName);


        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.patient_no_tv)
        TextView mBedTv;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.hospital_tv)
        TextView mHospitalTv;
        @ViewInject(R.id.bed_tv)
        TextView mQualityTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;
        @ViewInject(R.id.office_tv)
        TextView mOweTv;
    }
}
