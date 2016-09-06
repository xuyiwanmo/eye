package com.sg.eyedoctor.settings.personalInfo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.settings.personalInfo.bean.Level;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/27.
 */
public class LevelAdapter extends CommAdapter<Level> {

    public LevelAdapter(Context context, ArrayList<Level> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Level level, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView=View.inflate(mContext, R.layout.item_approve,null);
            holder=new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        }

        holder.mText.setText(level.name);
        holder.mChooseImg.setSelected(level.isChoose);
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.img_approve_check)
        ImageView mChooseImg;
        @ViewInject(R.id.tv_approve)
        TextView mText;
    }
}
