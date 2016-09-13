package com.sg.eyedoctor.helpUtils.freeConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.helpUtils.freeConsult.bean.FreePatient;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 图文咨询-病人列表
 */
public class FreeConsultAdapter extends CommAdapter<FreePatient> {

    private int type;

    public FreeConsultAdapter(Context context, ArrayList<FreePatient> list, int layoutId,int type) {
        super(context, list, layoutId);
        this.type=type;
    }

    @Override
    protected View convert(FreePatient patient, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_free_consult, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(patient.sex.equals("1")||patient.sex.equals("男")){
            setTextView(holder.mSexTv,"男");
        }else{
            setTextView(holder.mSexTv,"女");
        }
        holder.mNameTv.setText(patient.name);

        holder.mAgeTv.setText(patient.age + "岁");
        if(type==3){
            holder.mTimeTv.setText(patient.modifyDate);
        }else{
            holder.mTimeTv.setText(patient.modifyDate);
        }
        holder.mIllnessTv.setText(patient.questionDetail);
        if (patient.picFileName != null) {
            CommonUtils.loadImg(patient.picFileName, holder.mHeadImg, R.drawable.ic_patient_head);
        }

        holder.mCountTv.setText(patient.newMessage+"");
        holder.mCountTv.setVisibility(patient.newMessage!=0?View.VISIBLE:View.INVISIBLE);
        return convertView;
    }

    private String add48Hours(String modifyDate) {
        //1986-06-06
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        try {
            date = format.parse(modifyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,2);
        return format.format(cal.getTime());
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
        @ViewInject(R.id.img_arraw)
        TextView mCountTv;
    }


    private String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        return format.format(new Date(time));
    }
}
