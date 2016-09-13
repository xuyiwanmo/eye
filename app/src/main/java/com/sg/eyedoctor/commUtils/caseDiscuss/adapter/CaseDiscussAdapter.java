package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscuss;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.main.bean.TeamRead;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class CaseDiscussAdapter extends CommAdapter<CaseDiscuss> {
    ArrayList<TeamRead> reads;
    public CaseDiscussAdapter(Context context, ArrayList<CaseDiscuss> list, int layoutId,ArrayList<TeamRead> reads) {
        super(context, list, layoutId);
        this.reads=reads;
    }

    @Override
    protected View convert(CaseDiscuss caseDiscuss, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_case_discuss, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mAgeTv,caseDiscuss.age+"岁");
        setTextView(holder.mTimeTv,caseDiscuss.latestDate);
        setTextView(holder.mNameTv,caseDiscuss.patientName);
        if(caseDiscuss.sex!=null){
            setTextView(holder.mSexTv,caseDiscuss.sex.equals("0")?"女":"男");
        }
        setTextView(holder.mIllnessTv,"症状描述:"+caseDiscuss.illness);
        holder.mReadTv.setVisibility(View.INVISIBLE);
        for (TeamRead read : reads) {
            if(caseDiscuss.teamId.equals(read.teamId)){
                LogUtils.i("success get count"+read.readCount+caseDiscuss.patientName+read.teamId);
                int count=read.readCount>99?99:read.readCount;
                holder.mReadTv.setText(read.readCount+"");
                holder.mReadTv.setVisibility(count>0?View.VISIBLE:View.INVISIBLE);
            }else{
                holder.mReadTv.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;

    }

    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mSexTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;
        @ViewInject(R.id.arraw_img)
        ImageView mArrawImg;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.illness_tv)
        TextView mIllnessTv;
        @ViewInject(R.id.read_count_tv)
        TextView mReadTv;
    }
}
