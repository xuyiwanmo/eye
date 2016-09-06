package com.sg.eyedoctor.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/23.
 */
public abstract class CommAdapter<T> extends BaseAdapter {
    protected ArrayList<T> mList;
    protected Context mContext;
    protected int mLayoutId;

    public CommAdapter(Context context, ArrayList<T> list, int layoutId) {
        mList = list;
        mContext = context;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        if(mList==null){
            return 0;
        }
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convert(mList.get(position), convertView, mContext, position, mLayoutId);
    }

    protected abstract View convert(T t, View convertView, Context context, int position, int layoutId);



    public void setData(ArrayList<T> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public void setTextView(TextView view, String string) {
        if (string != null) {
            view.setText(string);
        }else{
            view.setText("未填写");
        }
    }
    public void setTextView(TextView view, String string,String emptyStr) {
        if (string != null) {
            view.setText(string);
        }else{
            view.setText(emptyStr);
        }
    }


    public String getText(String illDetail,String empty) {
        if(illDetail==null){
            return empty;
        }else{
            return illDetail;
        }
    }


}
