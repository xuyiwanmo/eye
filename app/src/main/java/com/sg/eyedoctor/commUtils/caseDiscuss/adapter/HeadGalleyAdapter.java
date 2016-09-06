package com.sg.eyedoctor.commUtils.caseDiscuss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.common.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/5.
 */
public class HeadGalleyAdapter<T> extends RecyclerView.Adapter<HeadGalleyAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<FriendList> mDatas;

    public HeadGalleyAdapter(Context context, ArrayList<FriendList> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
        ImageView mImg;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_choose_head_galley,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.iv_image);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        CommonUtils.loadImg(mDatas.get(i).picFileName,viewHolder.mImg);
    }
}
