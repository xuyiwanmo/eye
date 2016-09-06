package com.sg.eyedoctor.common.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.sg.eyedoctor.R;

import java.util.ArrayList;

/**
 * 图片Adapter
 */
public class AddImageGridAdapter extends CommAdapter<String> {

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_ADD = 0;
    public final int mGridWidth;
    public final static int column=4;

    public AddImageGridAdapter(Context context, ArrayList<String> list, int layoutId) {
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
    public String getItem(int i) {
        if (i == mList.size()) {
            return null;
        }
        return mList.get(i);
    }

    @Override
    protected View convert(String s, View convertView, Context context, int position, int layoutId) {
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == mList.size()) {
            convertView=View.inflate(mContext,R.layout.image_list_item_add,null);
            return convertView;
        }

        ViewHolder holder;
        if (convertView == null) {
            convertView=View.inflate(mContext,R.layout.item_add_photo,null);
            holder = new ViewHolder();
            holder.image= (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage("file://" + mList.get(position), holder.image, new ImageSize(mGridWidth, mGridWidth));
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }

}
