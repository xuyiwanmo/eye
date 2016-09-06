package com.sg.eyedoctor.consult.advice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.consult.advice.bean.AdviceCheck;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class DiagnosticCheckAdapter extends CommAdapter<AdviceCheck> {

    public DiagnosticCheckAdapter(Context context, ArrayList<AdviceCheck> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(AdviceCheck doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_dialognostic_check, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mNameTv.setText(doctor.name);

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        TextView mNameTv;

    }

}
