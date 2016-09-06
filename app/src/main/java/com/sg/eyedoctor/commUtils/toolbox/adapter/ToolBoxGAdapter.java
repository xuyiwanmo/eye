package com.sg.eyedoctor.commUtils.toolbox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.ImgText;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ToolBoxGAdapter extends CommAdapter<ImgText> {

    public ToolBoxGAdapter(Context context, ArrayList<ImgText> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(ImgText imgText, View convertView, Context context, int position, int layoutId) {


        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_tool_box, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mToolImg.setImageResource(imgText.mImgId);
        holder.mToolTv.setText(imgText.mTextId);

        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.tool_img)
        ImageView mToolImg;

        @ViewInject(R.id.tool_tv)
        TextView mToolTv;
    }

}



