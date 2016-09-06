package com.pulltorefresh.chuwe1.swipemenu.library;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

import java.util.List;

public class SwipeMenuView extends LinearLayout implements OnClickListener {

	private com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView mListView;

	private com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout mLayout;
	private SwipeMenu mMenu;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(SwipeMenu menu, com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private void addItem(SwipeMenuItem item, int id) {

		LayoutParams params = new LayoutParams(item.getWidth(),
				LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);

		if (!TextUtils.isEmpty(item.getTitle())&&item.getImageId() != 0) {
			parent.addView(createTitleIcon(item));
			return;
		}

		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		if (!TextUtils.isEmpty(item.getTitle())) {

			parent.addView(createTitle(item));
		}

	}

	private ImageView createIcon(SwipeMenuItem item) {
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	private TextView createTitle(SwipeMenuItem item) {
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	private View createTitleIcon(SwipeMenuItem item){
		View view = View.inflate(getContext(), R.layout.pull_item_swipmenu,null);
		TextView tv= (TextView) view.findViewById(R.id.tv);
		ImageView iv= (ImageView) view.findViewById(R.id.img);
		tv.setText(item.getTitle());
		tv.setTextSize(item.getTitleSize());
		//tv.setTextColor(getResources().getColor());
		iv.setImageResource(item.getImageId());
		return view;
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
		if (onExpandableSwipeItemClickListener != null && mLayout.isOpen()) {
			onExpandableSwipeItemClickListener.onExpandableItemClick(this,
					mMenu, v.getId());
		}
	}

	/*********** 实现ExpandableListView左划功能 start ***************/
	private com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView mExpandableListView;

	public SwipeMenuView(SwipeMenu menu,
			com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView expandableListView) {
		super(menu.getContext());
		mExpandableListView = expandableListView;
		mMenu = menu;
		List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private OnExpandableSwipeItemClickListener onExpandableSwipeItemClickListener;

	public OnExpandableSwipeItemClickListener getOnExpandableSwipeItemClickListener() {
		return onExpandableSwipeItemClickListener;
	}

	public void setOnExpandableSwipeItemClickListener(
			OnExpandableSwipeItemClickListener onExpandableSwipeItemClickListener) {
		this.onExpandableSwipeItemClickListener = onExpandableSwipeItemClickListener;
	}

	public static interface OnExpandableSwipeItemClickListener {
		void onExpandableItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}

	private int groupPosition;
	private int childPosition;

	public int getGroupPostion() {
		return groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setPositions(int groupPosition, int childPosition) {
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
	}

	/*************************** end *******************************/

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(
			OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}
}
