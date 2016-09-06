package com.sg.eyedoctor.contact.bean;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class AddFriendAdapter extends CommAdapter<Doctor> {

    public AddFriendAdapter(Context context,ArrayList<Doctor> list, int layoutId) {
        super( context,list, layoutId);
    }

    @Override
    protected View convert(Doctor doctor, View convertView, Context context, int position,int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, layoutId, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        holder.mHeadImg.setImageResource(R.drawable.ic_add_patient);

        return convertView;

    }


    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.illness_tv)
        TextView mOfficeTv;
        @ViewInject(R.id.hospital_tv)
        TextView mHospitalTv;
        @ViewInject(R.id.agree_tv)
        TextView mAgreeTv;
        @ViewInject(R.id.accept_tv)
        TextView mRefuseTv;
        @ViewInject(R.id.arraw)
        ImageView mArraw;

    }
}
