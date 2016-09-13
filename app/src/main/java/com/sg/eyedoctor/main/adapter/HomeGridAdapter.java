package com.sg.eyedoctor.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.main.bean.ImgDescription;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by zhanghua on 2015/12/29.
 */
public class HomeGridAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<ImgDescription> mImgDescriptions;

    public HomeGridAdapter(Context context, ArrayList<ImgDescription> imgDescriptions) {
        this.mContext = context;
        this.mImgDescriptions = imgDescriptions;
    }

    @Override
    public int getCount() {
        return mImgDescriptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgDescriptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        ImgDescription imgDescription=mImgDescriptions.get(position);
        ViewHolder viewHolder=null;
        if (convertView!=null){
            viewHolder=(ViewHolder)convertView.getTag();
        }else{
            convertView=View.inflate(mContext, R.layout.item_home_grid_img_text,null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }

        if(imgDescription.mDrawableId!=0){
            viewHolder.mImg.setImageResource(imgDescription.mDrawableId);
            viewHolder.mText.setText(imgDescription.mDescription);
        }else{
            viewHolder.mImg.setVisibility(View.INVISIBLE);
            viewHolder.mText.setVisibility(View.INVISIBLE);
        }

        if(imgDescription.count==0){
            viewHolder.mCountText.setVisibility(View.GONE);
        }else{
            viewHolder.mCountText.setVisibility(View.VISIBLE);
            viewHolder.mCountText.setText(imgDescription.count>99?99+"":imgDescription.count+"");
        }

        return convertView;
    }

    public void setData(ArrayList<ImgDescription> list) {
        this.mImgDescriptions=list;
        notifyDataSetChanged();
    }

    static class ViewHolder{
        @ViewInject(R.id.img)
        ImageView mImg;
        @ViewInject(R.id.text)
        TextView mText;
        @ViewInject(R.id.text_unread_tv)
        TextView mCountText;
    }
}
