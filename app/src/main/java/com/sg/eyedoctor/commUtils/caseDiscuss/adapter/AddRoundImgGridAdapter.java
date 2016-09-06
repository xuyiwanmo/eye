package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.contact.bean.FriendList;

import java.util.ArrayList;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 */
public class AddRoundImgGridAdapter extends CommAdapter<FriendList> {

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_ADD = 0;

    public final int mGridWidth;
    public final static int column=4;

    public AddRoundImgGridAdapter(Context context, ArrayList<FriendList> list, int layoutId) {
        super(context, list, layoutId);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / column;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        return position == mList.size() ? TYPE_ADD : TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return mList.size() + 1;
    }

    @Override
    public FriendList getItem(int i) {

        if (i == mList.size()) {
            return null;
        }
        return mList.get(i);
    }

    @Override
    protected View convert(FriendList friendList, View view, Context context, int position, int layoutId) {
        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (position == mList.size()) {
            ImageView imageView1= new ImageView(mContext);
            imageView1.setImageResource(R.drawable.ic_add_people);
            imageView1.setLayoutParams(new AbsListView.LayoutParams(CommonUtils.dp2px(mContext,50),CommonUtils.dp2px(mContext,50)));
            return imageView1;
        }

        ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext,R.layout.item_add_round_photo, null);
            holder = new ViewHolder();
            holder.image= (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CommonUtils.loadImg(mList.get(position).picFileName, holder.image);
        return view;
    }

    class ViewHolder {
        ImageView image;
    }

}
