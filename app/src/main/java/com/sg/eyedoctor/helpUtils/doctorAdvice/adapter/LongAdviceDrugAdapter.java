package com.sg.eyedoctor.helpUtils.doctorAdvice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.helpUtils.doctorAdvice.bean.LongAdviceDrug;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class LongAdviceDrugAdapter extends CommAdapter<LongAdviceDrug> {
    ClickCheck mClickCheck;

    public LongAdviceDrugAdapter(Context context, ArrayList<LongAdviceDrug> list, int layoutId,ClickCheck clickCheck) {
        super(context, list, layoutId);
       mClickCheck=clickCheck;
    }

    @Override
    protected View convert(LongAdviceDrug doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_long_advice_drug, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
     //   holder.mNameTv.setText(doctor.name);
        setTextView(holder.mNameTv,doctor.drugName);
        setTextView(holder.mUnitTv,doctor.drugStandard);
        setTextView(holder.mPriceTv,doctor.measureUnit+"");
//        holder.mAddImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mClickCheck.click(position);
//            }
//        });

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.unit_tv)
        TextView mUnitTv;
        @ViewInject(R.id.price_tv)
        TextView mPriceTv;
        @ViewInject(R.id.add_img)
        ImageView mAddImg;
    }

    public interface ClickCheck {
        void click(int postion);
    }
}
