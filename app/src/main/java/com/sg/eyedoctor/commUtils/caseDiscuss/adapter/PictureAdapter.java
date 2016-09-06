package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail.PicList;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/7.
 */
public class PictureAdapter extends CommAdapter<CaseDiscussDetail.PicList> {

    public PictureAdapter(Context context, ArrayList<CaseDiscussDetail.PicList> list, int layoutId) {
        super( context,list, layoutId);
    }

    @Override
    protected View convert(PicList doctor, View convertView, Context context, int position,int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_add_photo, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        CommonUtils.loadImg(doctor.picUrl, holder.mHeadImg);
        return convertView;
    }


    static class ViewHolder {
        @ViewInject(R.id.image)
        ImageView mHeadImg;

    }
}