package com.sg.eyedoctor.commUtils.internetConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29.
 */
public class InternetPictureAdapter extends CommAdapter<Picture> {

    public InternetPictureAdapter(Context context, ArrayList<Picture> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(Picture picture, View convertView, Context context, int positiont,int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_image, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        CommonUtils.loadImg(picture.picUrl,holder.mImg);
        return convertView;
    }


    static class ViewHolder{
        @ViewInject(R.id.iv_image)
        ImageView mImg;

    }
}
