package com.sg.eyedoctor.consult.advice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.consult.advice.bean.AdviceDrug;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class DiagnosticDrugAdapter extends CommAdapter<AdviceDrug> {
    UpdateGrugCount mClickGrug;

    public DiagnosticDrugAdapter(Context context, ArrayList<AdviceDrug> list, int layoutId, UpdateGrugCount clickGrug) {
        super(context, list, layoutId);
        mClickGrug=clickGrug;
    }

    @Override
    protected View convert(AdviceDrug doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_dialognostic_drug, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mIncreaseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickGrug.increase(position);
            }
        });
        holder.mDecreaseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickGrug.decrease(position);
            }
        });
        setTextView(holder.mNumberTv,doctor.unit);
        setTextView(holder.mNameTv,doctor.name);
        setTextView(holder.mCountTv,doctor.count+"");


        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
        @ViewInject(R.id.unit_tv)
        TextView mNumberTv;
        @ViewInject(R.id.increase_img)
        ImageView mIncreaseImg;
        @ViewInject(R.id.decrease_img)
        ImageView mDecreaseImg;
    }

    public interface UpdateGrugCount {
        void increase(int postion);
        void decrease(int postion);
        void edit(int postion,String count);
    }
}
