package com.sg.eyedoctor.consult.phoneConsult.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.consult.phoneConsult.bean.PhoneConsultTime;

import java.util.ArrayList;

/**
 */
public class PhoneConsultTimeAdapter extends CommAdapter<PhoneConsultTime> {

    public final int mGridWidth;
    public final int column=4;

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_ADD = 0;

    public PhoneConsultTimeAdapter(Context context, ArrayList<PhoneConsultTime> list, int layoutId) {
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
        PhoneConsultTime time=mList.get(position);
        return time.type==1 ? TYPE_ADD : TYPE_NORMAL;
    }


    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    protected View convert(PhoneConsultTime time, View view, Context context, int position, int layoutId) {
     return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PhoneConsultTime time=mList.get(position);
        if (time.type==1) {
            view = View.inflate(mContext,R.layout.itme_phone_consult_img, null);
            return view;
        }

        ViewHolder holder=null;
        if (view == null) {
            view = View.inflate(mContext,R.layout.item_add_time, null);
            holder = new ViewHolder();
            holder.timeTv= (TextView) view.findViewById(R.id.time_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(time.dutyTime!=null){
            holder.timeTv.setText(time.dutyTime+"");
        }else{
            holder.timeTv.setText("");
        }
        return view;
    }

    static class ViewHolder {
        TextView timeTv;
    }

}
