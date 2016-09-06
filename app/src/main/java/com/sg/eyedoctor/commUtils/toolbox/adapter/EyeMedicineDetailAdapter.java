package com.sg.eyedoctor.commUtils.toolbox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeMedicineDetail;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/17.
 */
public class EyeMedicineDetailAdapter extends CommAdapter<EyeMedicineDetail> {
    public EyeMedicineDetailAdapter(Context context, ArrayList<EyeMedicineDetail> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(EyeMedicineDetail baseData, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_eye_check_data, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        setTextView(holder.mTitleTv,baseData.name);

        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.eye_data_tv)
        TextView mTitleTv;
    }
}
