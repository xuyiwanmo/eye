package com.sg.eyedoctor.commUtils.internetConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/3.
 */
public class SupportInternetAadpter extends CommAdapter<Comment> {
    public SupportInternetAadpter(Context context, ArrayList<Comment> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Comment comment, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_support_internet, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        if (comment.picFileName != null) {
            ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(comment.picFileName), holder.mSupportImg);
        }
        setTextView(holder.mNameTv, comment.userName);
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mSupportImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
    }
}
