package com.sg.eyedoctor.chartFile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/6/8.
 */
public class AttentionPatientAdapter extends CommAdapter<PatientByGroup> {
    public AttentionPatientAdapter(Context context, ArrayList<PatientByGroup> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(PatientByGroup focusPatient, View convertView, Context context, int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_attention_patient, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        setTextView(holder.mNameTv, focusPatient.name);
        setTextView(holder.mTvSex, focusPatient.sex == 1 ? "男" : "女");
        if (focusPatient.birth==null) {
            setTextView(holder.mTvAge, "未填写");
        }else{
            setTextView(holder.mTvAge, CommonUtils.dateToAge(focusPatient.birth) + "岁");
        }

        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.tv_sex)
        TextView mTvSex;
        @ViewInject(R.id.line)
        View mLine;
        @ViewInject(R.id.tv_age)
        TextView mTvAge;

    }
}
