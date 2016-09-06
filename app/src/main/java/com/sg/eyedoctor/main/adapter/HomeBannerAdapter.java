/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.sg.eyedoctor.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 首页 轮播图
 */ 
public class HomeBannerAdapter extends BaseAdapter {

	private Context mContext;
	private int mSize;
	private boolean mIsInfiniteLoop;
	private List<Integer> mImgArray;

	public HomeBannerAdapter(Context context, ArrayList<Integer> imgArray) {
		this.mContext = context;
		this.mIsInfiniteLoop = false;
		this.mImgArray=imgArray;
		this.mSize = imgArray.size();
	}

	@Override
	public int getCount() {
		return mIsInfiniteLoop ? Integer.MAX_VALUE : mImgArray.size();
	}

	int getPosition(int position) {
		return mIsInfiniteLoop ? position % mSize : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(mContext);
			holder.imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.imageView.setImageResource(mImgArray.get(getPosition(position)));
		return view;
	}

	static class ViewHolder {

		ImageView imageView;
	}

	public boolean isInfiniteLoop() {
		return mIsInfiniteLoop;
	}


	public HomeBannerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.mIsInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

}
