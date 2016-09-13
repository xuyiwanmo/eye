package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscuss;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/2/22.
 */

public class CaseDiscussAdapter extends CommAdapter<CaseDiscuss> {
    List<RecentContact> mMessages;
    public CaseDiscussAdapter(Context context, ArrayList<CaseDiscuss> list, int layoutId,List<RecentContact> mMessages) {
        super(context, list, layoutId);
        this.mMessages=mMessages;
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

        for (RecentContact read : mMessages) {
            if(caseDiscuss.teamId.equals(read.getContactId())){

                int count=read.getUnreadCount()>99?99:read.getUnreadCount();
                holder.mReadTv.setText(read.getUnreadCount()+"");
                holder.mReadTv.setVisibility(count>0?View.VISIBLE:View.INVISIBLE);
            }
//            else{
//                holder.mReadTv.setVisibility(View.INVISIBLE);
//            }
        }

        return convertView;

    }

    public void setUnreadCount(List<RecentContact> messages) {
        this.mMessages=messages;
        notifyDataSetChanged();
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
