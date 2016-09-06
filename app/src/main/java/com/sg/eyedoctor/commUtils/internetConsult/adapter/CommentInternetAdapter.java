package com.sg.eyedoctor.commUtils.internetConsult.adapter;


import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/26.
 */
public class CommentInternetAdapter extends CommAdapter<Comment> {
    public CommentInternetAdapter(Context context, ArrayList<Comment> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Comment comment, View convertView, Context context, int position, int layoutId) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context,R.layout.item_internet_comment,null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        String name = comment.userName;
        String reply = comment.reviewerName;
        String content = comment.commentDetail;
        viewHolder.mNameTv.setText(name);

        setTextView(viewHolder.mTimeTv,comment.createDate);
        CommonUtils.loadImg(comment.picFileName,viewHolder.mHeadImg);
        if (!TextUtils.isEmpty(reply)) {
            SpannableString styledText = new SpannableString("回复 " + reply + ":" + content);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_comment), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_comment_reply), 3, reply.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style_comment), reply.length() + 3, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mContentTv.setText(styledText, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.mContentTv.setText(content);
        }

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.content_tv)
        TextView mContentTv;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.line)
        View mLine;
    }
}
