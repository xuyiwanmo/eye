package com.sg.eyedoctor.commUtils.academic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.bean.Academic;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class AcademicAdapter extends CommAdapter<Academic> {

    public AcademicAdapter(Context context, ArrayList<Academic> list, int layoutId) {
        super( context,list, layoutId);
    }

    @Override
    protected View convert(Academic doctor, View convertView, Context context, int position,int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_academic, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        CommonUtils.loadImgformat(doctor.picFileName,holder.mHeadImg);
        holder.mHeadImg.setImageResource(R.drawable.ad1);
        setTextView(holder.mTimeTv,doctor.newsTime);
        setTextView(holder.mTitleTv,doctor.newsTitle);

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.news_img)
        ImageView mHeadImg;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;

    }
}
