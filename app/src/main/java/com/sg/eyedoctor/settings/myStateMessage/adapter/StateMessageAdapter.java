package com.sg.eyedoctor.settings.myStateMessage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.settings.myStateMessage.bean.StateMessage;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class StateMessageAdapter extends CommAdapter<StateMessage> {

    private int mState;
    public StateMessageAdapter(Context context, ArrayList<StateMessage> list, int layoutId,int state) {
        super(context, list, layoutId);
        mState=state;
    }

    @Override
    protected View convert(StateMessage doctor, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_state_message, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mContentTv, doctor.messageDetail);
        setTextView(holder.mTimeTv, doctor.createDate);
        String result="";
        if(mState== ConstantValues.STATE_ALREADY_SEND){

            for (int i = 0; i < doctor.receiverList.size(); i++) {
                result = result + doctor.receiverList.get(i).receiverName + "，";
            }
            if (!TextUtils.isEmpty(result)) {
                result ="收件人: "+ result.substring(0, result.length() - 1);
            }
        }else if(mState==ConstantValues.STATE_RECEIVER){

            switch (doctor.type){
                case 1:
                    result="来自: 系统消息";
                    break;
                case 2:
                    if(doctor.publisherName!=null){
                        result="来自: "+doctor.publisherName+"的群发通知";
                    }else{
                        result="来自: 群发通知";
                    }

                    break;
                case 3:

                    if(doctor.publisherName!=null){
                        result="来自: "+doctor.publisherName+"的停诊通知";
                    }else{
                        result="来自: 停诊通知";
                    }
                    break;
            }

        }
        setTextView(holder.mFromTv, result);
        setTextView(holder.mTitleTv, doctor.messageTitle);

        if(doctor.isRead.equals("1")){
            holder.mTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_body_weak));
            holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.text_body_weak));
        }else{
            holder.mTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_body_strong));
            holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.text_body_strong));
        }

        return convertView;

    }

    class ViewHolder {
        @ViewInject(R.id.message_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.arraw_img)
        ImageView mArrawImg;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.from_tv)
        TextView mFromTv;
        @ViewInject(R.id.content_tv)
        TextView mContentTv;

    }
}