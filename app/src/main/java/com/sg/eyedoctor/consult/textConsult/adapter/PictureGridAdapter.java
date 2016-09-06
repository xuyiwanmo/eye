package com.sg.eyedoctor.consult.textConsult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.lookPicture.bean.Picture;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.ConstantValues;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29.
 */
public class PictureGridAdapter extends CommAdapter<Picture> {

    public PictureGridAdapter(Context context, ArrayList<Picture> list, int layoutId) {
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

        String url = ConstantValues.IMG_HOST + picture.picUrl;
        String url1 = url.replaceAll("\\\\", "/");
        ImageLoader.getInstance().displayImage(url1,holder.mImg);

        return convertView;
    }


    static class ViewHolder{
        @ViewInject(R.id.iv_image)
        ImageView mImg;

    }
}
