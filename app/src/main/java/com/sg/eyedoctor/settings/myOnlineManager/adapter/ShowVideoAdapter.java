package com.sg.eyedoctor.settings.myOnlineManager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.settings.myOnlineManager.bean.VideoTime;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 视频在线设置
 */
public class ShowVideoAdapter extends CommAdapter<VideoTime> {
    public ShowVideoAdapter(Context context, ArrayList<VideoTime> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(VideoTime videoTime, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_show_video_time, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mTimeTv, changeTime(videoTime.serviceDate)+" "+videoTime.serviceTime);
        setTextView(holder.mCountTv,videoTime.serviceAmount+"");

        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.count_tv)
        TextView mCountTv;
    }

    public static String changeTime(String serviceDate) {
        return serviceDate.replace("-","/");
    }

}
