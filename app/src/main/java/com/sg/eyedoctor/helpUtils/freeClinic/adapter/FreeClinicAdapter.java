package com.sg.eyedoctor.helpUtils.freeClinic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.GetFreeClinicList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class FreeClinicAdapter extends CommAdapter<GetFreeClinicList> {


    ViewHolder holder = null;

    public FreeClinicAdapter(Context context, ArrayList<GetFreeClinicList> list, int layoutId) {

        super(context,list, layoutId);
    }

    @Override
    protected View convert(GetFreeClinicList doctor, View convertView, Context context, final int position, int layoutId) {

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_free_clinic, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mTimeTv.setText("义诊日期: "+doctor.openDate.split(" ")[0]);

        holder.mCountTv.setText(doctor.amount + "");

        String name="";
        switch (doctor.type){
            case 1:
                name=mContext.getString(R.string.text_consult);
                break;
            case 2:
                name=mContext.getString(R.string.phone_consult);
                break;

        }
        holder.mTitleTv.setText(name);

        return convertView;

    }

    static class ViewHolder {
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.once_tv)
        TextView mOnceTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
    }

}
