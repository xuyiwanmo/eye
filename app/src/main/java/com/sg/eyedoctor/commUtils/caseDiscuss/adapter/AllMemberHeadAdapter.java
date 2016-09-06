package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail.MemberList;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class AllMemberHeadAdapter extends CommAdapter<MemberList> {

    public AllMemberHeadAdapter(Context context, ArrayList<MemberList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(MemberList doctor, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_all_member, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        if (position == mList.size() - 1) {
            holder.mHeadImg.setImageResource(R.drawable.ic_add_people);
            holder.mNameTv.setText(R.string.add_new_doctor);
        } else {
            CommonUtils.loadImg(doctor.picFileName, holder.mHeadImg);

            setTextView(holder.mNameTv, doctor.userName,doctor.memberId+"");
        }
        return convertView;

    }


    class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;

    }
}
