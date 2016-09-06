package com.sg.eyedoctor.consult.videoConsult.adapter;

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
 * 视频列表
 */
public class VideoConsultAdapter extends CommAdapter<Patient> {

    private DailCallback mDailCallback;

    public VideoConsultAdapter(Context context, ArrayList<Patient> list, int layoutId, DailCallback dailCallback) {
        super(context, list, layoutId);
        mDailCallback = dailCallback;
    }

    @Override
    protected View convert(final Patient patient, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_video_consult_fragment, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        if (patient.state == 1) {
            holder.mDailTv.setVisibility(View.VISIBLE);
            holder.mAppointTimeNameTv.setText(R.string.place_order);
            holder.mTimeTv.setText(patient.createDate);
        } else {
            holder.mDailTv.setVisibility(View.INVISIBLE);
            holder.mAppointTimeNameTv.setText(R.string.complete_time);
            holder.mTimeTv.setText(patient.modifyDate);
        }

        holder.mDailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mDailCallback.dail(position);
            }
        });

        holder.mAgeTv.setText(patient.age + "岁");
        holder.mNameTv.setText(patient.patientName);
        if (patient.sex != null) {
            holder.mSexTv.setText(patient.sex.equals("1") ? "男" : "女");
        }

        if (patient.state == 6) {
            holder.mDiagnoseTv.setVisibility(View.VISIBLE);
            holder.mDailNotTv.setVisibility(View.VISIBLE);
            holder.mDiagnoseTv.setVisibility(View.GONE);
        } else if (patient.writeState.equals("0")) {
            holder.mDailNotTv.setVisibility(View.GONE);
            holder.mDiagnoseTv.setVisibility(View.VISIBLE);
            holder.mDiagnoseTv.setBackgroundResource(R.drawable.bg_blue_text_white);
        } else if (patient.writeState.equals("1")) {
            holder.mDailNotTv.setVisibility(View.GONE);
            holder.mDiagnoseTv.setVisibility(View.VISIBLE);
            holder.mDiagnoseTv.setBackgroundResource(R.drawable.bg_auth_green);
        }

        //诊断建议
        holder.mDiagnoseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailCallback.editDiagnotice(position);
            }
        });
        if (patient.picFileName != null) {
            CommonUtils.loadImg(patient.picFileName, holder.mHeadImg, R.drawable.ic_patient_head);
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
    }

    public interface DailCallback {
        void dail(int position);

        void editDiagnotice(int position);
    }
}
