package com.sg.eyedoctor.commUtils.toolbox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeCheck;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 */

public class EyeCheckAdapter extends CommAdapter<EyeCheck> {

    public EyeCheckAdapter(Context context, ArrayList<EyeCheck> list, int layoutId) {
        super( context,list, layoutId);
    }

    @Override
    protected View convert(EyeCheck eyeCheck, View convertView, Context context, int position,int layoutId) {

        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_eye_check, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        CommonUtils.loadImg(eyeCheck.piclist.get(0).picUrl,holder.mCheckImg);
        setTextView(holder.mTitleTv,eyeCheck.checkName);
        setTextView(holder.mDescTv,eyeCheck.description);

        return convertView;

    }


    static class ViewHolder{
        @ViewInject(R.id.check_img)
        RoundImageView mCheckImg;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.desc_tv)
        TextView mDescTv;


    }
}
