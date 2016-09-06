package com.sg.eyedoctor.settings.myOnlineManager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.settings.myOnlineManager.bean.VideoTime;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class VideoTimeSettingAdapter extends CommAdapter<VideoTime> {

    public ViedoTimeCallback mCallback;
    ViewHolder holder = null;

    public VideoTimeSettingAdapter(Context context, ArrayList<VideoTime> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(VideoTime doctor, View convertView, Context context, final int position, int layoutId) {

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_video_set_time, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mStartTimeTv.setText(doctor.startTime);
        holder.mStartTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.clickStartTime(position);
            }
        });
        holder.mEndTimeTv.setText(doctor.endTime);
        holder.mEndTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.clickEndTime(position);
            }
        });

        holder.mCountTv.setText(doctor.serviceAmount + "");
        holder.mCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.clickCount(position);
            }
        });
        holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.decrease(position);
            }
        });
        holder.mTitleTv.setText("时间段" + (position+1));
        return convertView;
    }

    public interface ViedoTimeCallback {
        void decrease(int position);
        void clickEndTime(int position);
        void clickCount(int position);
        void clickStartTime(int position);
    }

    public void setCallback(ViedoTimeCallback callback) {
        mCallback = callback;
    }

    class ViewHolder {
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.delete_img)
        ImageView mDeleteImg;
        @ViewInject(R.id.ill_time_tv)
        TextView mIllTimeTv;
        @ViewInject(R.id.start_time_tv)
        TextView mStartTimeTv;
        @ViewInject(R.id.horizontal_line_tv)
        TextView mHorizontalLineTv;
        @ViewInject(R.id.end_time_tv)
        TextView mEndTimeTv;
        @ViewInject(R.id.ill_count_tv)
        TextView mIllCountTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;

    }
}
