package com.sg.eyedoctor.chartFile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.Diagnosis;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2016/1/14.
 */
public class DiagnosisAdapter extends CommAdapter<Diagnosis> {


    public DiagnosisAdapter(Context context, ArrayList<Diagnosis> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Diagnosis diagnosis, View convertView, Context context, int position, int layoutId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_case_detail_check, null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCheckTv.setText(diagnosis.costName);

        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.tv_case_detail_check)
        TextView mCheckTv;
    }

}
