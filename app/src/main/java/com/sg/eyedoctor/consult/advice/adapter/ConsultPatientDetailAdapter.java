package com.sg.eyedoctor.consult.advice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.consult.textConsult.bean.ConsultationList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class ConsultPatientDetailAdapter extends CommAdapter<ConsultationList> {


    public ConsultPatientDetailAdapter(Context context, ArrayList<ConsultationList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(ConsultationList patientDetail, View convertView, Context context, int position, int layoutId) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_consult_patient_detail, null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder,convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mHourTv.setText(getHour(patientDetail.createDate));
        viewHolder.mDateTv.setText(getDate(patientDetail.createDate));
        String type="";
        switch (patientDetail.type){
            case ConstantValues.PATIENT_CENTER_NEW_ADD:
                type="新增";
                break;
            case ConstantValues.PATIENT_CENTER_HOSPITAL:
                type="医院";
                break;
            case ConstantValues.PATIENT_CENTER_PLATFORM:
                type="平台";
                break;
        }
        viewHolder.mTypeTv.setText(type);
        viewHolder.miagnosisNameTv.setText(patientDetail.description);

        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.tv_patient_center_hour)
        TextView mHourTv;
        @ViewInject(R.id.tv_patient_center_date)
        TextView mDateTv;
        @ViewInject(R.id.tv_patient_center_case_name)
        TextView miagnosisNameTv;
        @ViewInject(R.id.type_tv)
        TextView mTypeTv;

    }

    private String getHour(String str) {
        Date date = null;
        Calendar c = Calendar.getInstance();
        String amPm = "AM";

        try {
            //                         2016-01-13 09:05:00
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.AM_PM) != 0) {
            amPm = "PM";
        }
        return amPm + " " + new SimpleDateFormat("HH:mm").format(date);
    }

    private String getDate(String str) {
        Date date = null;
        try {
            //                          2016-01-13 09:05:00
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("yy/MM/dd").format(date);
    }

}
