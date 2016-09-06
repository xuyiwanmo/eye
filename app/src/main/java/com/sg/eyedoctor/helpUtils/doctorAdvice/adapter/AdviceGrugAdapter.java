package com.sg.eyedoctor.helpUtils.doctorAdvice.adapter;

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

public class AdviceGrugAdapter extends CommAdapter<AdviceDrug> {
    ClickGrug mClickGrug;
    ViewHolder holder;

    public AdviceGrugAdapter(Context context, ArrayList<AdviceDrug> list, int layoutId,ClickGrug clickGrug) {
        super(context, list, layoutId);
        mClickGrug=clickGrug;
    }

    @Override
    protected View convert(AdviceDrug doctor, View convertView, Context context, final int position, int layoutId) {

        holder=null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_advice_drug, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mNameTv.setText(doctor.name);

        holder.mCountEt.setText(doctor.count+"");
        holder.mCountTv.setText(doctor.unit);
        holder.mAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickGrug.click(position);
            }
        });

//        holder.mCountEt.addTextChangedListener(new TextWatcher() {
//            String temp="";
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                temp=s.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                LogUtils.i("adapter" +
//                        temp);
//                 mClickGrug.edit(position,temp);
//            }
//        });
        holder.mCountEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickGrug.edit(position,"");
            }
        });

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
        @ViewInject(R.id.add_img)
        ImageView mAddImg;
        @ViewInject(R.id.count_et)
        TextView mCountEt;
        
    }

    public interface ClickGrug {
        void click(int postion);
        void edit(int postion,String count);
    }
}
