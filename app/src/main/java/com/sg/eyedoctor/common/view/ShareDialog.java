package com.sg.eyedoctor.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sg.eyedoctor.R;


/**
 * Created by Administrator on 2016/5/24.
 */
public class ShareDialog extends Dialog {

    public ShareDialog(Context context) {
        super(context);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private OnClickListener buttonClickListener;
        public Builder(Context context) {
            this.context = context;
        }
        public Builder setButtonListener(OnClickListener listener) {
            this.buttonClickListener = listener;
            return this;
        }
        public ShareDialog create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ShareDialog dialog = new ShareDialog(context, R.style.share_loading_dialog);
            View view = inflater.inflate(R.layout.share_dialog_layout,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.umeng_share_loading_anim);
            imageView.startAnimation(hyperspaceJumpAnimation);
            dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (buttonClickListener != null) {

            }
            dialog.setContentView(view);
            return dialog;
        }
    }
}
