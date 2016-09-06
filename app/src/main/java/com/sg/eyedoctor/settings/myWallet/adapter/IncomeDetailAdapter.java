package com.sg.eyedoctor.settings.myWallet.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.settings.myWallet.bean.IncomeDetail;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class IncomeDetailAdapter extends CommAdapter<IncomeDetail> {


    public IncomeDetailAdapter(Context context, ArrayList<IncomeDetail> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(IncomeDetail caseDiscuss, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_income_detail, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        setTextView(holder.mTimeTv, caseDiscuss.createDate);
        String type="";
        switch (caseDiscuss.orderType){
            case 1:
                type=mContext.getString(R.string.text_consult);
                break;
            case 2:
                type=mContext.getString(R.string.phone_consult);
                break;
            case 3:
                type=mContext.getString(R.string.video_consult);
                break;
            case 4:
                type=mContext.getString(R.string.add_appointment);
                break;
        }
        String name="";
        if(caseDiscuss.patientName!=null){
            name=caseDiscuss.patientName+"-";
        }
        setTextView(holder.mContentTv, name + type);
        setTextView(holder.mMoneyTv, "+" + caseDiscuss.orderAmount);

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.content_tv)
        TextView mContentTv;
        @ViewInject(R.id.money_tv)
        TextView mMoneyTv;

    }
}
