package com.pulltorefresh.chuwe1.swipemenu.library;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView.OnSwipeItemClickListener;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView.OnMenuItemClickListener;

public class SwipeMenuAdapter implements WrapperListAdapter,
		OnSwipeItemClickListener {

	private ListAdapter mAdapter;
	private Context mContext;
	private OnMenuItemClickListener onMenuItemClickListener;

	public SwipeMenuAdapter(Context context, ListAdapter adapter) {
		mAdapter = adapter;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mAdapter.getCount();
	}

	@Override
	public Object getItem(int position) {
		return mAdapter.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return mAdapter.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout layout = null;
		if (convertView == null) {
			View contentView = mAdapter.getView(position, convertView, parent);
			com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu menu = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu(mContext);
			menu.setViewType(mAdapter.getItemViewType(position));
			createMenu(menu);
			com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView menuView = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView(menu,
					(com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView) parent);
			menuView.setOnSwipeItemClickListener(this);
			com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView listView = (com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView) parent;
			layout = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout(contentView, menuView,
					listView.getCloseInterpolator(),
					listView.getOpenInterpolator());
			layout.setPosition(position);
		} else {
			layout = (com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout) convertView;
			layout.closeMenu();
			layout.setPosition(position);
			View view = mAdapter.getView(position, layout.getContentView(),
					parent);
		}
		return layout;
	}

	public void createMenu(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu menu) {
		// Test Code
		com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem item = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem(mContext);
		item.setTitle("Item 1");
		item.setBackground(new ColorDrawable(Color.GRAY));
		item.setWidth(300);
		menu.addMenuItem(item);

		item = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem(mContext);
		item.setTitle("Item 2");
		item.setBackground(new ColorDrawable(Color.RED));
		item.setWidth(300);
		menu.addMenuItem(item);
	}

	@Override
	public void onItemClick(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView view, com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu menu, int index) {
		if (onMenuItemClickListener != null) {
			onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
					index);
		}
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return mAdapter.isEnabled(position);
	}

	@Override
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	@Override
	public int getItemViewType(int position) {
		return mAdapter.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount();
	}

	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}

	@Override
	public ListAdapter getWrappedAdapter() {
		return mAdapter;
	}
}