package com.sg.eyedoctor.consult.phoneConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class PhoneConsultFragmentAdapter extends CommAdapter<Patient> {
    DailCallback mDailCallback;

    public PhoneConsultFragmentAdapter(Context context, ArrayList<Patient> list, int layoutId,DailCallback dailCallback) {
        super(context, list, layoutId);
        mDailCallback = dailCallback;
    }

    @Override
    protected View convert(final Patient doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_phone_consult_fragment, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        holder.mDailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (doctor.clickable) {
                    mDailCallback.dail(position);
                }
            }
        });
        if (doctor.authTime == 60) {
            holder.mDailTv.setText(R.string.dail_now);
            holder.mDailTv.setBackgroundResource(R.drawable.bg_blue_text_white);
        } else {
            holder.mDailTv.setText(doctor.authTime + mContext.getString(R.string.after_seconds_resend));
            holder.mDailTv.setBackgroundResource(R.drawable.bg_auth_grey);
        }

        setTextView(holder.mAgeTv, doctor.age + "岁");
        setTextView(holder.mNameTv, doctor.patientName);
        if (doctor.sex != null) {
            setTextView(holder.mSexTv, doctor.sex.equals("1") ? "男" : "女");
        } else {
            setTextView(holder.mSexTv, "未填写");
        }

        switch (doctor.writeState){
            case "0": //蓝色背景  未填写
                holder.mDiagnoseTv.setVisibility(View.VISIBLE);
                holder.mDiagnoseTv.setBackgroundResource(R.drawable.bg_blue_text_white);
                break;
            case "1": //灰色背景   已填写
                holder.mDiagnoseTv.setVisibility(View.VISIBLE);
                holder.mDiagnoseTv.setBackgroundResource(R.drawable.bg_auth_green);
                break;
            default:  //灰色
                holder.mDiagnoseTv.setVisibility(View.VISIBLE);
                holder.mDiagnoseTv.setBackgroundResource(R.drawable.bg_auth_grey);
                break;
        }

        holder.mDiagnoseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailCallback.editDiagnotice(position);
            }
        });

        if (doctor.state == 1) {
            holder.mDiagnoseTv.setVisibility(View.INVISIBLE);
            holder.mDailTv.setVisibility(View.VISIBLE);
            holder.mAppointTimeNameTv.setText(R.string.appointment_time);
            holder.mTimeTv.setText(doctor.dealTime);

            long currentTime=System.currentTimeMillis();
            long endTime= CommonUtils.getLongTime(doctor.dealTime);
            if(currentTime>endTime){  //超过预约时间  则显示已超时
                holder.mOutTimeTv.setVisibility(View.VISIBLE);
            }else{
                holder.mOutTimeTv.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.mOutTimeTv.setVisibility(View.INVISIBLE);
            holder.mDiagnoseTv.setVisibility(View.VISIBLE);
            holder.mDailTv.setVisibility(View.INVISIBLE);
            holder.mAppointTimeNameTv.setText(R.string.complete_time);
            holder.mTimeTv.setText(doctor.dealTime);

            if (doctor.state == 6) {//未拨通  退款状态
                holder.mDiagnoseTv.setVisibility(View.GONE);
                holder.mDailNotTv.setVisibility(View.VISIBLE);
            }else{
                holder.mDailNotTv.setVisibility(View.INVISIBLE);
            }
        }

        if (doctor.picFileName != null) {
            CommonUtils.loadImg(doctor.picFileName, holder.mHeadImg, R.drawable.ic_patient_head);
        }

        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.arraw_img)
        ImageView mArrawImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.sex_tv)
        TextView mSexTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;
        @ViewInject(R.id.dail_no_tv)
        TextView mDailNotTv;
        @ViewInject(R.id.dail_tv)
        TextView mDailTv;
        @ViewInject(R.id.diagnose_tv)
        TextView mDiagnoseTv;
        @ViewInject(R.id.appoint_time_name_tv)
        TextView mAppointTimeNameTv;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.out_time_tv)
        TextView mOutTimeTv;
    }

    public interface DailCallback {
        void dail(int position);

        void editDiagnotice(int position);
    }

}
