package com.sg.eyedoctor.consult.videoConsult.adapter;

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
 * 加号
 */
public class VideoAddAdapter extends CommAdapter<Patient> {
    AddCallback mCallback;

    public VideoAddAdapter(Context context, ArrayList<Patient> list, int layoutId,AddCallback callback) {
        super(context, list, layoutId);
        mCallback=callback;
    }

    @Override
    protected View convert(Patient serviceXtrRecord, View convertView, Context context, final int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_video_add, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNameTv.setText(serviceXtrRecord.patientName);

        if (serviceXtrRecord.sex!=null&&serviceXtrRecord.sex.equals("1")) {
            holder.mSexTv.setText("男");
        } else {
            holder.mSexTv.setText("女");
        }

        holder.mAgeTv.setText(serviceXtrRecord.age + "岁");
        holder.mTimeTv.setText("申请时间 "+serviceXtrRecord.createDate);

       switch (serviceXtrRecord.state){

           case 0://未选择
               holder.mResultTv.setVisibility(View.INVISIBLE);
               holder.mAgreeTv.setVisibility(View.VISIBLE);
               holder.mRefuseTv.setVisibility(View.VISIBLE);
               holder.mAgreeTv.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mCallback.agreeOrRefuse(position,1);
                   }
               });
               holder.mRefuseTv.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mCallback.agreeOrRefuse(position,2);
                   }
               });
               break;

           case 1://同意
               holder.mResultTv.setVisibility(View.VISIBLE);
               holder.mResultTv.setText(R.string.agree_already);
               holder.mResultTv.setTextColor(mContext.getResources().getColor(R.color.actionbar_color));
               holder.mAgreeTv.setVisibility(View.INVISIBLE);
               holder.mRefuseTv.setVisibility(View.INVISIBLE);
               break;

           case 2: //不同意
               holder.mResultTv.setVisibility(View.VISIBLE);
               holder.mResultTv.setText(R.string.refuse_already);
               holder.mResultTv.setTextColor(mContext.getResources().getColor(R.color.actionsheet_red));
               holder.mAgreeTv.setVisibility(View.INVISIBLE);
               holder.mRefuseTv.setVisibility(View.INVISIBLE);
               break;
       }

        if (serviceXtrRecord.picFileName != null) {
            CommonUtils.loadImg(serviceXtrRecord.picFileName, holder.mHeadImg, R.drawable.ic_patient_head);
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
        @ViewInject(R.id.result_tv)
        TextView mResultTv;
        @ViewInject(R.id.agree_tv)
        TextView mAgreeTv;
        @ViewInject(R.id.refuse_tv)
        TextView mRefuseTv;
        @ViewInject(R.id.tv_consult_patient_illness)
        TextView mIllnessTv;
    }

    public interface  AddCallback{
        /**
         *同意  1
         *不同意  2
         */
        void agreeOrRefuse(int position,int type);
    }
}
