package com.sg.eyedoctor.helpUtils.freeClinic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.PostClinicList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class FreeClinicTimeAdapter extends CommAdapter<PostClinicList> {

    public SetTimeCallback mCallback;
    public boolean mShowDecrease;
    ViewHolder holder = null;

    public FreeClinicTimeAdapter(Context context, ArrayList<PostClinicList> list, int layoutId,boolean showDecrease) {
        super(context, list, layoutId);
        mShowDecrease=showDecrease;
    }

    @Override
    protected View convert(PostClinicList doctor, View convertView, Context context, final int position, int layoutId) {

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_free_clinic_time, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        if(mShowDecrease){
            holder.mDeleteImg.setVisibility(View.VISIBLE);
        }else{
            holder.mDeleteImg.setVisibility(View.GONE);
        }
        holder.mTimeTv.setText(doctor.openDate);
        holder.mTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.clickTime(position);
            }
        });

        holder.mCountTv.setText(doctor.amount + "");
        holder.mCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.request(position);
            }
        });
        holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.decrease(position);
            }
        });

        holder.mTitleTv.setText("时间段"+(position+1));


        return convertView;

    }

    static class ViewHolder {
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.delete_img)
        ImageView mDeleteImg;
        @ViewInject(R.id.line)
        View mLine;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.count_et)
        TextView mCountTv;
        @ViewInject(R.id.time_ll)
        LinearLayout mTimeLl;

    }

    public interface SetTimeCallback{
        void request(int position);

        void clickTime(int position);

        void setCount(int position, String num);

        void decrease(int postion);
    }

    public void setCallback(SetTimeCallback callback) {
        mCallback = callback;
    }
}
