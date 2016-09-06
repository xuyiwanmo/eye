package com.sg.eyedoctor.commUtils.outPatientRecord.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhanghua on 2016/1/12.
 */
public class MyPatientDateAdapter extends CommAdapter<Date> {


    private int mSelectedPosition;

    public MyPatientDateAdapter(Context context, ArrayList<Date> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Date date, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.item_grid_my_patient_date,null);
            holder=new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(position==mSelectedPosition){
            holder.mDateLl.setSelected(true);
        }else{
            holder.mDateLl.setSelected(false);
        }
        holder.mDayTv.setText(CommonUtils.formatDay(date));
        holder.mDateTv.setText(CommonUtils.formatDate(date));
        return convertView;
    }


    public void setSelectPosition(int i) {
        mSelectedPosition=i;
        notifyDataSetChanged();
    }

    static class  ViewHolder{
        @ViewInject(R.id.tv_my_patient_day)
        TextView mDayTv;
        @ViewInject(R.id.tv_my_patient_date)
        TextView mDateTv;
        @ViewInject(R.id.ll_my_patient_date)
        LinearLayout mDateLl;
    }


}
