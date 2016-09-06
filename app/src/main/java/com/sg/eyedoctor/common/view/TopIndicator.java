package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部indicator
 *
 * @author dewyze
 *
 */
public class TopIndicator extends LinearLayout {

	private static final String TAG = "TopIndicator";

	private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
	private List<View> mViewList = new ArrayList<View>();
	// 顶部菜单的文字数组
	private CharSequence[] mLabels = new CharSequence[] { };
	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	// 底部线条移动初始位置
	private int mUnderLineFromX = 0;
	private int mTextSize=18;
	Context mContext;

	public TopIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
	//	init(context);
	}

	public TopIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext=context;
	}

	public TopIndicator(Context context) {
		super(context);
		mContext=context;
	}

	private void init(final Context context) {
		setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(250, 250, 250));

		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		mUnderLineWidth = mScreenWidth / mLabels.length;

		mUnderLine = new View(context);
		mUnderLine.setBackgroundColor(getResources().getColor(R.color.text_color));
		LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(
				mUnderLineWidth, 4);

		// 标题layout
		LinearLayout topLayout = new LinearLayout(context);
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);

		LayoutInflater inflater = LayoutInflater.from(context);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {

			final int index = i;

			final View view = inflater.inflate(R.layout.custom_top_indicator_item,
					null);

			final CheckedTextView itemName = (CheckedTextView) view
					.findViewById(R.id.item_name);
			itemName.setTextSize(mTextSize);
//			if(i==size-1){
//				line.setVisibility(INVISIBLE);
//			}
//			itemName.setCompoundDrawablesWithIntrinsicBounds(context
//							.getResources().getDrawable(mDrawableIds[i]), null, null,
//					null);
			itemName.setCompoundDrawablePadding(10);
			itemName.setText(mLabels[i]);

			topLayout.addView(view, params);

			View line=new View(context);

			line.setBackgroundColor(context.getResources().getColor(R.color.text_body_weak));
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					1, CommonUtils.dp2px(context,48));
			p.setMargins(0,CommonUtils.dp2px(context,4),0,CommonUtils.dp2px(context,4));
			line.setLayoutParams(p);
			topLayout.addView(line,p);

			itemName.setTag(index);

			mCheckedList.add(itemName);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != mTabListener) {
						mTabListener.onIndicatorSelected(index);
					}
				}
			});

			// 初始化 底部菜单选中状态,默认第一个选中
			if (i == 0) {
				itemName.setChecked(true);
				itemName.setTextColor(getResources().getColor(R.color.text_color));
			} else {
				itemName.setChecked(false);
				itemName.setTextColor(getResources().getColor(R.color.black));
			}

		}
		this.addView(topLayout, topLayoutParams);
		this.addView(mUnderLine, underLineParams);
	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(Context context, int index) {
		int size = mCheckedList.size();
		for (int i = 0; i < size; i++) {
			CheckedTextView checkedTextView = mCheckedList.get(i);
			if ((Integer) (checkedTextView.getTag()) == index) {
				checkedTextView.setChecked(true);
				checkedTextView.setTextColor(getResources().getColor(R.color.text_color));
			} else {
				checkedTextView.setChecked(false);
				checkedTextView.setTextColor(getResources().getColor(R.color.black));
			}
		}
		// 下划线动画
		doUnderLineAnimation(index);
	}

	private void doUnderLineAnimation(int index) {
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
				index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		mUnderLineFromX = index * mUnderLineWidth;
	}

	// 回调接口
	private OnTopIndicatorListener mTabListener;

	public interface OnTopIndicatorListener {
		void onIndicatorSelected(int index);
	}

	public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
		this.mTabListener = listener;
	}

	public void setLabels(CharSequence[] lables){
		this.mLabels=lables;
		init(mContext);
	}
	public void setLabels(CharSequence[] lables,int textSize){
		this.mLabels=lables;
		this.mTextSize=textSize;
		init(mContext);
	}

}
