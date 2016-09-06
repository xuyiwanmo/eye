package com.sg.eyedoctor.contact.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SearchContactAdapter extends CommAdapter<FriendList> {


    public SearchContactAdapter(Context context, ArrayList<FriendList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(FriendList searchContact, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_search_contact, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommonUtils.loadImg(searchContact.picFileName, holder.mHeadImg);
        holder.mNameTv.setText(searchContact.userName);
        holder.mHospitalTv.setText(searchContact.speciality);

        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.hospital_tv)
        TextView mHospitalTv;

    }


}
