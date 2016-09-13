package com.sg.eyedoctor.commUtils.patientReport.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/10.
 */
public class AlreadyReportAdapter extends CommAdapter<PDValidate> {

    public AlreadyReportAdapter(Context context, ArrayList<PDValidate> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(PDValidate patientReport, View convertView, Context context, final int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.item_patient_already_report, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mNameTv,patientReport.name);
        if(patientReport.sex!=null){
            setTextView(holder.mSexTv,patientReport.sex.equals("1")?"男":"女");
        }
        setTextView(holder.mAgeTv,patientReport.age);
        setTextView(holder.mIllnessTv,"初步预诊:"+patientReport.diagnosisResult);
        if(patientReport.latestDate!=null){
           // String time=patientReport.latestDate.split(" ")[0];
            String time=patientReport.latestDate;
            setTextView(holder.mRecentTimeTv,time.replaceAll("/","-"));
        }
        holder.mCountTv.setText(patientReport.newMessage);
        holder.mCountTv.setVisibility(!patientReport.newMessage.equals("0")?View.VISIBLE:View.INVISIBLE);
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mSexTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;
        @ViewInject(R.id.illness_tv)
        TextView mIllnessTv;
        @ViewInject(R.id.recent_time_tv)
        TextView mRecentTimeTv;
        @ViewInject(R.id.accept_tv)
        TextView mAcceptTv;
        @ViewInject(R.id.already_tv)
        TextView mAlreadyTv;
        @ViewInject(R.id.read_count_tv)
        TextView mCountTv;
    }

}
