package com.pulltorefresh.chuwe1.swipemenu.library;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView.OnExpandableSwipeItemClickListener;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView.OnExpandableMenuItemClickListener;

import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView;

/**
 * 可侧滑的ExpandListView适配器
 * 
 * @author chuwe1
 *
 */
public class SwipMenuExpandableAdapter extends BaseExpandableListAdapter
		implements OnExpandableSwipeItemClickListener {

	private ExpandableListAdapter mAdapter;
	private Context mContext;
	private OnExpandableMenuItemClickListener onExpandableMenuItemClickListener;

	public SwipMenuExpandableAdapter(ExpandableListAdapter mAdapter,
			Context mContext) {
		this.mAdapter = mAdapter;
		this.mContext = mContext;
	}

	@Override
	public int getGroupCount() {
		return mAdapter.getGroupCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mAdapter.getChildrenCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mAdapter.getGroup(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mAdapter.getChild(groupPosition, childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return mAdapter.getGroupId(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return mAdapter.getChildId(groupPosition, childPosition);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View contentView = mAdapter.getGroupView(groupPosition, isExpanded,
				convertView, parent);
		return contentView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout layout = null;
		if (convertView == null) {
			View contentView = mAdapter.getChildView(groupPosition,
					childPosition, isLastChild, convertView, parent);
			SwipeMenu menu = new SwipeMenu(mContext);
			menu.setViewType(getChildType(groupPosition, childPosition));
			createMenu(menu);
			SwipeMenuView menuView = new SwipeMenuView(menu,
					(SwipeMenuExpandableListView) parent);
			menuView.setOnExpandableSwipeItemClickListener(this);
			com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView expandablelistView = (com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView) parent;
			layout = new com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout(contentView, menuView,
					expandablelistView.getCloseInterpolator(),
					expandablelistView.getOpenInterpolator());
			layout.setPositions(groupPosition, childPosition);
		} else {
			layout = (com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout) convertView;
			layout.closeMenu();
			layout.setPositions(groupPosition, childPosition);
			View view = mAdapter.getChildView(groupPosition, childPosition,
					isLastChild, layout.getContentView(), parent);
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
	public void onExpandableItemClick(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuView view, com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu menu,
			int index) {
		if (onExpandableMenuItemClickListener != null) {
			onExpandableMenuItemClickListener.onExpandableMenuItemClick(
					view.getGroupPostion(), view.getChildPosition(), menu,
					index);
		}
	}

	public void setOnExpandableMenuItemClickListener(
			OnExpandableMenuItemClickListener onExpandableMenuItemClickListener) {
		this.onExpandableMenuItemClickListener = onExpandableMenuItemClickListener;
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
	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	@Override
	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return mAdapter.isChildSelectable(groupPosition, childPosition);
	}
}