package com.sg.eyedoctor.chartFile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/20.
 */
public class ChartPatientAdapter extends CommAdapter<PatientByGroup> {
    public ChartPatientAdapter(Context context, ArrayList<PatientByGroup> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(PatientByGroup patient, View convertView, Context context, int position, int layoutId) {

        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_crm_child, null);
            viewHolder = new ChildViewHolder();

            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.mNameText.setText(patient.name);
        return convertView;
    }

    static class ChildViewHolder {
        @ViewInject(R.id.head_img)
        ImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameText;
        @ViewInject(R.id.tv_illness)
        TextView mIllnessText;
        @ViewInject(R.id.tv_unread_count)
        TextView mCountText;

    }
}
