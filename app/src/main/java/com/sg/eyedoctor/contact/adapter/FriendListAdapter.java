package com.sg.eyedoctor.contact.adapter;

import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 好友列表adapter
 */
public class FriendListAdapter extends CommAdapter<FriendList> implements SectionIndexer {
    private boolean mShowLetter = true;

    public FriendListAdapter(Context context, ArrayList<FriendList> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(FriendList friendList, View convertView, Context context, int position, int layoutId) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_friend_list, null);
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mShowLetter) {
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.mCatalog.setVisibility(View.VISIBLE);
                viewHolder.mCatalog.setText(friendList.sortLetters);
            } else {
                viewHolder.mCatalog.setVisibility(View.GONE);
            }
        } else {
            viewHolder.mCatalog.setVisibility(View.GONE);
        }

        setTextView(viewHolder.mNameTv,friendList.userName);
        setTextView(viewHolder.mHospitalTv, friendList.licenseHospital);
        setTextView(viewHolder.mDoctorTitleTv, friendList.licenseTitle);
        setTextView(viewHolder.mDeptTv,friendList.licenseDept);

        CommonUtils.loadImg(friendList.picFileName, viewHolder.mHeadImg);
        viewHolder.mReadTv.setText(friendList.newMessage+"");
        viewHolder.mReadTv.setVisibility(!friendList.newMessage.equals("0")?View.VISIBLE:View.INVISIBLE);
        return convertView;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {

        if (mList != null && mList.get(position) != null && mList.get(position).sortLetters != null) {
            return mList.get(position).sortLetters.charAt(0);
        }

        return 0;


    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).sortLetters;
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
        @ViewInject(R.id.catalog)
        TextView mCatalog;
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.doctor_title_tv)
        TextView mDoctorTitleTv;
        @ViewInject(R.id.dept_tv)
        TextView mDeptTv;
        @ViewInject(R.id.hospital_tv)
        TextView mHospitalTv;
        @ViewInject(R.id.read_count_tv)
        TextView mReadTv;
    }

    public void setShowLetter(boolean b){
        this.mShowLetter=b;
    }
}