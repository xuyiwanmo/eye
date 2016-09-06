package com.sg.eyedoctor.helpUtils.doctorAdvice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.HospitalPatientDetail;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class HospitalPatientDetailAdapter extends CommAdapter<HospitalPatientDetail> {
    EditClick mEditClick;

    public void setEditClick(EditClick editClick) {
        mEditClick = editClick;
    }

    public HospitalPatientDetailAdapter(Context context, ArrayList<HospitalPatientDetail> list, int layoutId) {
        super(context, list, layoutId);
    }


    @Override
    protected View convert(HospitalPatientDetail doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_hospital_patient_detail, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }


        setTextView(holder.mBeginTv, doctor.adviceTime);
        setTextView(holder.mAdviceTv, doctor.adviceName);
        if (doctor.yzqx != null) {
            if (doctor.yzqx.equals("1")) {
                setTextView(holder.mTypeTv, "长期");
            } else if (doctor.yzqx.equals("2")) {
                setTextView(holder.mTypeTv, "临时");
            } else if (doctor.yzqx.equals("3")) {
                setTextView(holder.mTypeTv, "出院");
            }

        }

        holder.mEditTv.setVisibility(View.GONE);
        //0新开、1正常、2疑问、3作废、4停嘱、5复核通过
        switch (doctor.state) {

            case "0":
                holder.mSortTv.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.mSortTv.setText("新增");
                holder.mEditTv.setVisibility(View.VISIBLE);
                holder.mEditTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditClick.editClick(position);
                    }
                });
                break;

            case "4":
                holder.mSortTv.setTextColor(mContext.getResources().getColor(R.color.text_body_strong));
                holder.mSortTv.setText("停嘱");
                break;

            case "1":
                holder.mSortTv.setTextColor(mContext.getResources().getColor(R.color.actionbar_color));
                holder.mSortTv.setText("正常");
                break;

            case "5":
                holder.mSortTv.setTextColor(mContext.getResources().getColor(R.color.actionbar_color));
                holder.mSortTv.setText("复核通过");
                break;
        }


        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.begin_tv)
        TextView mBeginTv;
        @ViewInject(R.id.advice_tv)
        TextView mAdviceTv;
        @ViewInject(R.id.type_tv)
        TextView mTypeTv;
        @ViewInject(R.id.sort_tv)
        TextView mSortTv;
        @ViewInject(R.id.edit_tv)
        TextView mEditTv;

    }

    public interface EditClick{
        void editClick(int position);
    }
}
