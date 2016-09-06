package com.sg.eyedoctor.commUtils.plusManager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.plusManager.activity.PlusManagerActivity;
import com.sg.eyedoctor.commUtils.plusManager.bean.GetXtrAppointment;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/8.
 */
public class PlusManagerAdapter extends CommAdapter<GetXtrAppointment> {
    AddManagerCallback mCallback;

    public PlusManagerAdapter(Context context, ArrayList<GetXtrAppointment> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(final GetXtrAppointment addManager, View convertView, Context context, final int position, int layoutId) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_add_manager, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        String section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.mSortTv.setVisibility(View.VISIBLE);
            setTextView(viewHolder.mSortTv, CommonUtils.parseDate(addManager.appointmentDate));
        } else {
            viewHolder.mSortTv.setVisibility(View.GONE);
        }
        setTextView(viewHolder.mAgeTv, addManager.age + "");
        if (addManager.sex != null) {
            setTextView(viewHolder.mSexTv, addManager.sex.equals("1") ? "男" : "女");
        }
        setTextView(viewHolder.mPatientNameTv, addManager.patientName);
        setTextView(viewHolder.mTimeTv, addManager.appointmentDate.replaceAll("/","-") + " " + addManager.appointmentTime + "-" + addManager.appointmentTime2);
        setTextView(viewHolder.mIllnessTv, addManager.description);

        if (addManager.visState.equals("1")) {
            viewHolder.mAppointAlreadyTv.setBackgroundResource(R.drawable.bg_text_black);
            viewHolder.mAppointDelayTv.setBackgroundResource(R.drawable.bg_text_black);

            //已就诊
            viewHolder.mAppointAlreadyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //延时就诊
            viewHolder.mAppointDelayTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else if (addManager.visState.equals("2")) {
            viewHolder.mAppointDelayTv.setBackgroundResource(R.drawable.bg_text_black);
            viewHolder.mAppointAlreadyTv.setBackgroundResource(R.drawable.bg_text_blue);
            //已就诊
            viewHolder.mAppointAlreadyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.update(position, PlusManagerActivity.STATE_ALREADY);
                }
            });

            //延时就诊
            viewHolder.mAppointDelayTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else if (addManager.visState.equals("0")) {
            viewHolder.mAppointDelayTv.setBackgroundResource(R.drawable.bg_text_green);
            viewHolder.mAppointAlreadyTv.setBackgroundResource(R.drawable.bg_text_blue);
            //已就诊
            viewHolder.mAppointAlreadyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCallback.update(position, PlusManagerActivity.STATE_ALREADY);
                }
            });

            //延时就诊
            viewHolder.mAppointDelayTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCallback.update(position, PlusManagerActivity.STATE_DELAY);
                }
            });
        }


        return convertView;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public String getSectionForPosition(int position) {
        return CommonUtils.formatDate(mList.get(position).appointmentDate);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = CommonUtils.formatDate(mList.get(i).appointmentDate);
            String firstChar = sortStr;
            if (firstChar.equals(section)) {
                return i;
            }
        }

        return -1;
    }


    static class ViewHolder {
        @ViewInject(R.id.patient_name_tv)
        TextView mPatientNameTv;
        @ViewInject(R.id.sort_tv)
        TextView mSortTv;
        @ViewInject(R.id.actionbar_title_tv)
        TextView mSexTv;
        @ViewInject(R.id.age_tv)
        TextView mAgeTv;

        @ViewInject(R.id.time_tv)
        TextView mTimeTv;
        @ViewInject(R.id.illness_tv)
        TextView mIllnessTv;
        @ViewInject(R.id.appoint_yet_tv)
        TextView mAppointAlreadyTv;
        @ViewInject(R.id.appoint_delay_tv)
        TextView mAppointDelayTv;
    }

    public interface AddManagerCallback {
        void update(int position, int visState);
    }

    public void setCallback(AddManagerCallback callback) {
        mCallback = callback;
    }
}


