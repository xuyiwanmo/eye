package com.sg.eyedoctor.settings.myStore.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.settings.myStore.bean.StoreItem;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/17.
 */
public class StoreAdapter extends CommAdapter<StoreItem> {
    public StoreAdapter(Context context, ArrayList<StoreItem> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(StoreItem doctor, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_store, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        switch(doctor.sourceType){
            case ConstantValues.STORE_ACADEMIC:
                holder.mTypeTv.setText(R.string.academic);
                break;
            case ConstantValues.STORE_INTERNET:
                holder.mTypeTv.setText(R.string.internet_consult);
                break;
            case ConstantValues.STORE_CIRCLE:
                holder.mTypeTv.setText(R.string.eye_circle);
                break;
        }

        holder.mTimeTv.setText(doctor.createDate);
        holder.mTitleTv.setText(doctor.title);
        if(doctor.picUrl==null||doctor.picUrl.equals("")){
            holder.mPictureImg.setVisibility(View.GONE);
        }else{
            CommonUtils.loadImg(doctor.picUrl,holder.mPictureImg);
            holder.mPictureImg.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.type_tv)
        TextView mTypeTv;
        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.picture_img)
        ImageView mPictureImg;
        @ViewInject(R.id.store_title_tv)
        TextView mTitleTv;
    }
}
