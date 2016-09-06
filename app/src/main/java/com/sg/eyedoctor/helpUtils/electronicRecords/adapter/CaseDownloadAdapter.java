package com.sg.eyedoctor.helpUtils.electronicRecords.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.helpUtils.electronicRecords.bean.PubliceList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/7.
 */
public class CaseDownloadAdapter extends CommAdapter<PubliceList> {

    public CaseDownloadAdapter(Context context, ArrayList<PubliceList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(final PubliceList publiceList, View convertView, Context context, final int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_case_load, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }

        holder.mNameTv.setText(publiceList.title);

        return convertView;
    }

    public static class ViewHolder {

        @ViewInject(R.id.name_tv)
        TextView mNameTv;

    }


}
