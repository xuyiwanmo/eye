package com.sg.eyedoctor.common.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sg.eyedoctor.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/25.
 */
public class UiUtils {

    public static void setHeight(Context context,BaseAdapter comAdapter,ListView lsComment){
        int listViewHeight = 0;
        int adaptCount = comAdapter.getCount();
        for(int i=0;i<adaptCount;i++){
            View temp = comAdapter.getView(i,null,lsComment);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = lsComment.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = listViewHeight;
        lsComment.setLayoutParams(layoutParams);
    }

    public static void setImgGridviewHeight(Context context,int colums,GridView gv,ArrayList lsit) {
        if (lsit == null ||lsit.size() == 0) {
            return;
        }

        ViewGroup.LayoutParams layoutParams =  gv.getLayoutParams();
        int rows = lsit.size() / colums;
        if (lsit.size() % colums != 0) {//整除
            rows++;
        }

        layoutParams.height = rows * CommonUtils.dip2px(context, 90);
        layoutParams.width = 4 * CommonUtils.dip2px(context, 90);
        gv.setLayoutParams(layoutParams);
    }

    public static String getCurrentActivity(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

    //为listview设置空文本
    public static TextView setEmptyText(Context context,AbsListView list,String emptyText){
        LinearLayout.LayoutParams lytp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lytp.gravity = Gravity.CENTER;

        TextView emptyView=createEmptyView(context, emptyText);
        emptyView.setLayoutParams(lytp);

        ((ViewGroup)list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);
        emptyView.setVisibility(View.INVISIBLE);
        return emptyView;
    }

    //为pulltoRefreshLv设置空文本
    public static void setEmptyText(Context context,PullToRefreshListView list,String emptyText){
        LinearLayout.LayoutParams lytp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lytp.gravity = Gravity.CENTER;

        TextView emptyView=createEmptyView(context, emptyText);
        emptyView.setLayoutParams(lytp);
        ((ViewGroup) list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);
    }

    public static TextView createEmptyView(Context context,String emptyText){
        TextView emptyView = new TextView(context);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText(emptyText);
        emptyView.setTextSize(18);
        emptyView.setTextColor(context.getResources().getColor(R.color.text_body_weak));
        emptyView.setVisibility(View.GONE);
        return  emptyView;
    }

    /**
     * 通过listveiw的child view获取整个高度
     * @param gridView
     * @param paddingTop
     * @param paddingBottom
     */
    public static void setViewHeightBasedOnChildren(GridView gridView, int paddingTop, int paddingBottom) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = gridView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, null);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight + CommonUtils.dp2px(gridView.getContext(), paddingTop + paddingBottom);
        // 设置margin
        //   ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        gridView.setLayoutParams(params);
    }







}
