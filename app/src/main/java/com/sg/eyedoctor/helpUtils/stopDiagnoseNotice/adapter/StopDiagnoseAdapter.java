package com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.bean.StopDiagnose;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/4.
 */
public class StopDiagnoseAdapter extends CommAdapter<StopDiagnose> {
    public StopDiagnoseAdapter(Context context, ArrayList<StopDiagnose> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(StopDiagnose stopDiagnose, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_stop_diagnose, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mReasonTv,stopDiagnose.stopReason);
        setTextView(holder.mTimeTv,stopDiagnose.createDate);
        String title="";
        String[] types=stopDiagnose.typeList.split(",");
        for (String type : types) {
            switch (type){
                case "1":
                    title=title+"在线问诊,";
                    break;
                case "2":
                    title=title+"电话问诊,";
                    break;
                case "3":
                    title=title+"视频问诊,";
                    break;
                case "4":
                    title=title+"加号问诊,";
                    break;
                case "0":
                    title=title+"未填写,";
                    break;
            }
        }

        title=title.substring(0,title.length()-1);
        setTextView(holder.mTypeTv, title);
        setTextView(holder.mStopSpanTv, stopDiagnose.startSpan+"-"+stopDiagnose.endSpan);
        setTextView(holder.mStopDateTv, CommonUtils.changeDateFormat(stopDiagnose.startDate)+"-"+CommonUtils.changeDateFormat(stopDiagnose.endDate));

        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.stop_date_tv)
        TextView mStopDateTv;
        @ViewInject(R.id.stop_span_tv)
        TextView mStopSpanTv;
        @ViewInject(R.id.type_tv)
        TextView mTypeTv;
        @ViewInject(R.id.reason_tv)
        TextView mReasonTv;

    }
}
