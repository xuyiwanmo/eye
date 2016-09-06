package com.pulltorefresh.chuwe1.swipemenu.library;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenu {

	private Context mContext;
	private List<com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem> mItems;
	private int mViewType;

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}
}