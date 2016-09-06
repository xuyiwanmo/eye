package com.sg.eyedoctor.helpUtils.doctorAdvice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
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

public class AdviceCheckAdapter extends CommAdapter<AdviceCheck> {
    ClickCheck mClickCheck;

    public AdviceCheckAdapter(Context context, ArrayList<AdviceCheck> list, int layoutId,ClickCheck clickCheck) {
        super(context, list, layoutId);
        mClickCheck=clickCheck;
    }

    @Override
    protected View convert(AdviceCheck doctor, View convertView, Context context, final int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_advice_check, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mNameTv.setText(doctor.name);
        holder.mAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCheck.click(position);
            }
        });

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.add_img)
        ImageView mAddImg;
    }

    public interface ClickCheck {
        void click(int postion);
    }
}
