package com.sg.eyedoctor.contact.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.adapter.CommAdapter;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.contact.activity.NewFriendActivity;
import com.sg.eyedoctor.contact.bean.AddFriend;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/1.
 */
public class ContactAdapter extends CommAdapter<AddFriend> {
    ContactCallback mContactCallback;
    public ContactAdapter(Context context, ArrayList<AddFriend> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected View convert(AddFriend addFriend, View convertView, final Context context, final int position, int layoutId) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_contact, null);
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        }
        holder.mCatalogTv.setVisibility(View.GONE);

        CommonUtils.loadImg(addFriend.picFileName,holder.mHeadImg);

        holder.mNameTv.setText(addFriend.userName);
        if(addFriend.validateState==0){//验证状态(0:待验证|1:已通过|2:已拒绝)
            if(!addFriend.applyId.equals(addFriend.loginId)){
                holder.mAgreeLl.setVisibility(View.GONE);
                holder.mRefuseTv.setVisibility(View.VISIBLE);
                holder.mRefuseTv.setText("等待同意");
            }else{

                holder.mRefuseTv.setVisibility(View.GONE);
                holder.mAgreeLl.setVisibility(View.VISIBLE);
            }

        }else if(addFriend.validateState==1){
            holder.mRefuseTv.setVisibility(View.VISIBLE);
            holder.mAgreeLl.setVisibility(View.GONE);
            holder.mRefuseTv.setText(R.string.agree);
        }else{
            holder.mRefuseTv.setVisibility(View.VISIBLE);
            holder.mAgreeLl.setVisibility(View.GONE);
            holder.mRefuseTv.setText(R.string.refuse_already);
        }
        holder.mAgreeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactCallback.agree(NewFriendActivity.TYPE_AGREE,position);
            }
        });
        holder.mRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactCallback.agree(NewFriendActivity.TYPE_REFUSE,position);
            }
        });

        setTextView(holder.mTitleTv, addFriend.licenseTitle, "");
        setTextView(holder.mHospitalTv, addFriend.licenseHospital,"");
        setTextView(holder.mOfficeTv, addFriend.licenseDept,"");

        return convertView;
    }

    static class ViewHolder{
        @ViewInject(R.id.head_img)
        RoundImageView mHeadImg;
        @ViewInject(R.id.name_tv)
        TextView mNameTv;
        @ViewInject(R.id.contact_title_tv)
        TextView mTitleTv;
        @ViewInject(R.id.illness_tv)
        TextView mOfficeTv;
        @ViewInject(R.id.hospital_tv)
        TextView mHospitalTv;
        @ViewInject(R.id.agree_tv)
        TextView mAgreeTv;
        @ViewInject(R.id.refuse_btn)
        TextView mRefuseBtn;
        @ViewInject(R.id.accept_tv)
        TextView mRefuseTv;
        @ViewInject(R.id.arraw)
        ImageView mArraw;
        @ViewInject(R.id.catalog)
        TextView mCatalogTv;
        @ViewInject(R.id.agree_ll)
        LinearLayout mAgreeLl;

    }

    public interface ContactCallback{
        void agree(int type,int postition);

    }

    public void setContactCallback(ContactCallback contactCallback){
        mContactCallback=contactCallback;
    }
}
