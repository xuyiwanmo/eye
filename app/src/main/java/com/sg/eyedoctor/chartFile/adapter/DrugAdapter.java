package com.sg.eyedoctor.chartFile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.Drug;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 病例详情->用药
 */
public class DrugAdapter extends CommAdapter<Drug> {


    public DrugAdapter(Context context, ArrayList<Drug> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Drug drug, View convertView, Context context, int position, int layoutId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_case_detail_medicine, null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mMedcineTv.setText(drug.drugName);


        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.tv_medicine_name)
        TextView mMedcineTv;
        @ViewInject(R.id.tv_medicine_weight)
        TextView mWeightTv;

    }
}
