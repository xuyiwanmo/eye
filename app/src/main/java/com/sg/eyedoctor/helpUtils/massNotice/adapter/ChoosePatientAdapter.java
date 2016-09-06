package com.sg.eyedoctor.helpUtils.massNotice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.patientReport.bean.PDValidate;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 群发通知-选择联系人-患者adapter
 */
public class ChoosePatientAdapter extends CommAdapter<PDValidate> {
    PatientCheckedCallback mCheckedCallback;

    public void setCheckedCallback(PatientCheckedCallback checkedCallback) {
        mCheckedCallback = checkedCallback;
    }

    public ChoosePatientAdapter(Context context, ArrayList<PDValidate> list, int layoutId) {
        super(context, list, layoutId);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(ArrayList<PDValidate> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    protected View convert(final PDValidate friendList, View convertView, Context context, final int position, int layoutId) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_contact, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.mCheckImg.setSelected(friendList.isChecked);

        viewHolder.mCheckRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendList.isChecked) {
                    ((RelativeLayout) v).getChildAt(0).setSelected(false);

                    friendList.isChecked = false;
                    mCheckedCallback.setPatientChecked(false, friendList, position);

                } else {
                    ((RelativeLayout) v).getChildAt(0).setSelected(true);

                    friendList.isChecked = true;
                    mCheckedCallback.setPatientChecked(true, friendList, position);

                }
            }
        });
     //   CommonUtils.loadImg(friendList.picFileName, viewHolder.mHeadImg);
        viewHolder.mNameTv.setText(friendList.name);
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.check_img)
        ImageView mCheckImg;
        @ViewInject(R.id.check_rl)
        RelativeLayout mCheckRl;
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
    }

    public interface PatientCheckedCallback {

        void setPatientChecked(boolean isChecked, PDValidate checkFriend, int position);
    }
}