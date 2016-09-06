package com.sg.eyedoctor.commUtils.toolbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.bean.EyeEnglish;
import com.sg.eyedoctor.common.adapter.CommAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 好友列表adapter
 */
public class EyeEnglishAdapter extends CommAdapter<EyeEnglish> implements SectionIndexer {

	public EyeEnglishAdapter(Context context, ArrayList<EyeEnglish> list, int layoutId) {
		super(context, list, layoutId);
	}

	@Override
	protected View convert(EyeEnglish friendList, View convertView, Context context,int position, int layoutId) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_eye_english, null);
			x.view().inject(viewHolder,convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.mLetterTv.setVisibility(View.VISIBLE);
			viewHolder.mLetterTv.setText(friendList.egroup);
		} else {
			viewHolder.mLetterTv.setVisibility(View.GONE);
		}

		viewHolder.mEnglishTv.setText(friendList.word);
		viewHolder.mChineseTv.setText(friendList.name);

		return convertView;
	}

	static class ViewHolder{
		@ViewInject(R.id.catalog)
		TextView mLetterTv;
		@ViewInject(R.id.english_tv)
		TextView mEnglishTv;
		@ViewInject(R.id.chinese_tv)
		TextView mChineseTv;

	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mList.get(position).egroup.charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).egroup;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}


}