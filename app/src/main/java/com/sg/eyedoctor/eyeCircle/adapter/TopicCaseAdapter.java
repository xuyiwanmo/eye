package com.sg.eyedoctor.eyeCircle.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.eyeCircle.bean.TopicCase;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 医生圈:话题列表
 */
public class TopicCaseAdapter extends BaseAdapter {
    private ArrayList<TopicCase> mTopicCases;
    private Context mContext;

    private DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.ic_loading)
            .showImageOnFail(R.drawable.ic_loading)
            .showImageOnLoading(R.drawable.ic_loading)
            .cacheInMemory(true)// 在内存中会缓存该图片
            .cacheOnDisk(true)// 在硬盘中会缓存该图片
            .considerExifParams(true)// 会识别图片的方向信息
            .resetViewBeforeLoading(true)// 重设图片
            .build();

    public TopicCaseAdapter(ArrayList<TopicCase> topicCases, Context context) {
        this.mTopicCases = topicCases;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mTopicCases == null) {
            return 0;
        } else {
            return this.mTopicCases.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mTopicCases == null) {
            return null;
        } else {
            return this.mTopicCases.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopicCase topicCase=this.mTopicCases.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_team_topic, null);
            x.view().inject(holder,convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(topicCase.picFileName), holder.mHeadImg, mConfig);

        holder.mNameTv.setText(topicCase.userName);
        holder.mHospitalTv.setText(topicCase.licenseHospital);
        holder.mDateTv.setText(topicCase.createDate);
        holder.mSupportTv.setText(topicCase.upvoteCount+"");
        holder.mBrowseTv.setText("223");
        holder.mCommentTv.setText(topicCase.commentCount+"");
        holder.mTitleTv.setText("#"+topicCase.title+"#");
        holder.mContentTv.setText(topicCase.detail);
        holder.mStoreTv.setText(topicCase.collectCount+"");


        if (topicCase.picList != null&&topicCase.picList.size()!=0) {
            ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(topicCase.picList.get(0).picUrl),holder.mDetailImage,mConfig);
            holder.mDetailImage.setVisibility(View.VISIBLE);
            holder.mCountLv.setVisibility(View.VISIBLE);
            holder.mCountTv.setText(topicCase.picList.size()+"");

        }else {
            holder.mDetailImage.setVisibility(View.GONE);
            holder.mCountLv.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setData(ArrayList<TopicCase> data) {
        mTopicCases = data;
        notifyDataSetChanged();
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
        @ViewInject(R.id.tv_team_topic_browse)
        TextView mBrowseTv;
        @ViewInject(R.id.tv_team_topic_comment)
        TextView mCommentTv;
        @ViewInject(R.id.tv_team_topic_store)
        TextView mStoreTv;
        @ViewInject(R.id.tv_team_topic_title)
        TextView mTitleTv;
        @ViewInject(R.id.tv_team_topic_content)
        TextView mContentTv;
        @ViewInject(R.id.detail_img)
        ImageView mDetailImage;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
        @ViewInject(R.id.count_ll)
        LinearLayout mCountLv;


    }
}

