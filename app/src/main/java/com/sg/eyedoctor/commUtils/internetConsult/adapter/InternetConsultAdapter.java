package com.sg.eyedoctor.commUtils.internetConsult.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class InternetConsultAdapter extends CommAdapter<InternetConsult> {

    private DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.ic_loading)
            .showImageOnFail(R.drawable.ic_loading)
            .showImageOnLoading(R.drawable.ic_loading)
            .cacheInMemory(true)// 在内存中会缓存该图片
            .cacheOnDisk(true)// 在硬盘中会缓存该图片
            .considerExifParams(true)// 会识别图片的方向信息
            .resetViewBeforeLoading(true)// 重设图片
            .build();

    public InternetConsultAdapter(Context context, ArrayList<InternetConsult> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    protected View convert(InternetConsult topicCase, View convertView, Context context, int position, int layoutId) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_internet_consult, null);
            x.view().inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(topicCase.picFileName), holder.mHeadImg, mConfig);

        holder.mNameTv.setText(topicCase.userName);
        holder.mHospitalTv.setText(topicCase.licenseHospital);
        holder.mDateTv.setText(topicCase.createDate);
        holder.mSupportTv.setText(topicCase.upvoteCount + "");
        holder.mStoreTv.setText(topicCase.collectCount + "");
        holder.mCommentTv.setText(topicCase.commentCount + "");
        holder.mTitleTv.setText("#" + topicCase.title + "#");
        StringBuffer content = new StringBuffer();
        content.append(topicCase.sex.equals("0") ? "女" : "男").append("性患者，").append(topicCase.age + "岁，" + getText(topicCase.illDetail, ""));
        if (topicCase.illness != null && !topicCase.illness.equals("")) {
            content.append(" 诊断：" + topicCase.illness);
        }
        if (topicCase.detail != null && !topicCase.detail.equals("")) {
            content.append(" 备注：" + topicCase.detail);
        }

        holder.mContentTv.setText(content);
        if (topicCase.licenseDept != null) {
            holder.mDoctorTitleTv.setText(topicCase.licenseDept);
        }

        if (topicCase.picList != null && topicCase.picList.size() != 0) {
            ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(topicCase.picList.get(0).picUrl), holder.mDetailImage, mConfig);
            holder.mDetailImage.setVisibility(View.VISIBLE);
            holder.mCountLv.setVisibility(View.VISIBLE);
            holder.mCountTv.setText(topicCase.picList.size() + "");

        } else {
            holder.mDetailImage.setVisibility(View.GONE);
            holder.mCountLv.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.img_team_topic_head)
        RoundImageView mHeadImg;
        @ViewInject(R.id.tv_team_topic_name)
        TextView mNameTv;
        @ViewInject(R.id.tv_team_topic_date)
        TextView mDateTv;
        @ViewInject(R.id.tv_team_topic_support)
        TextView mSupportTv;
        @ViewInject(R.id.tv_team_topic_hospital)
        TextView mHospitalTv;
        @ViewInject(R.id.tv_team_topic_store)
        TextView mStoreTv;
        @ViewInject(R.id.tv_team_topic_comment)
        TextView mCommentTv;
        @ViewInject(R.id.tv_team_topic_title)
        TextView mTitleTv;
        @ViewInject(R.id.tv_team_topic_content)
        TextView mContentTv;
        @ViewInject(R.id.detail_img)
        ImageView mDetailImage;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mDoctorTitleTv;
        @ViewInject(R.id.count_ll)
        LinearLayout mCountLv;
    }

}

