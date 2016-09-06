package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 好友列表adapter
 */
public class ChooseContactAdapter extends CommAdapter<FriendList> implements SectionIndexer {
    DoctorCheckedCallback mDoctorCheckedCallback;

    public void setDoctorCheckedCallback(DoctorCheckedCallback doctorCheckedCallback) {
        mDoctorCheckedCallback = doctorCheckedCallback;
    }

    public ChooseContactAdapter(Context context, ArrayList<FriendList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(final FriendList friendList, View convertView, Context context, final int position, int layoutId) {
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
                    mDoctorCheckedCallback.setDoctorChecked(false, friendList, position);

                } else {
                    ((RelativeLayout) v).getChildAt(0).setSelected(true);

                    friendList.isChecked = true;
                    mDoctorCheckedCallback.setDoctorChecked(true, friendList, position);

                }
            }
        });
        CommonUtils.loadImg(friendList.picFileName, viewHolder.mHeadImg);
        viewHolder.mNameTv.setText(friendList.userName);
        return convertView;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).userName.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).userName;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }


    private String spitName(String name) {
        if (name.length() == 1) {
            return name;
        } else if (name.length() == 2) {
            return name.charAt(1) + "";
        } else if (name.length() > 2) {
            return name.substring(1, 3);
        }
        return name;
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

    public interface DoctorCheckedCallback {

        void setDoctorChecked(boolean isChecked, FriendList checkFriend, int position);
    }
}