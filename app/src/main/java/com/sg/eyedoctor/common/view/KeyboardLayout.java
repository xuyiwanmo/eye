package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.utils.L;

/**
 * Created by Administrator on 2016/4/11.
 */
public class KeyboardLayout extends RelativeLayout {
    private static final String TAG = KeyboardLayout.class.getSimpleName();
    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;
    private InputWindowListener listener;

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context) {
        super(context);
    }
    /**
     * set keyboard state listener
     */
//    public void setOnkbdStateListener(onKybdsChangeListener listener){
//        mListener = listener;
//    }
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//        if (!mHasInit) {
//            mHasInit = true;
//            mHeight = b;
//            if (mListener != null) {
//                mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
//            }
//        } else {
//            mHeight = mHeight < b ? b : mHeight;
//        }
//        if (mHasInit && mHeight > b) {
//            mHasKeybord = true;
//            if (mListener != null) {
//                mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
//            }
//            LogUtils.i("show keyboard.......");
//
//        }
//        if (mHasInit && mHasKeybord && mHeight == b) {
//            mHasKeybord = false;
//            if (mListener != null) {
//                mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
//            }
//            LogUtils.i("hide keyboard.......");
//        }
//    }
//
//    public interface onKybdsChangeListener{
//        public void onKeyBoardStateChange(int state);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h) {
            L.d("input window show");
            listener.show();
        } else{
            L.d("input window hidden");
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }

    public interface InputWindowListener {
        void show();

        void hidden();
    }
}
