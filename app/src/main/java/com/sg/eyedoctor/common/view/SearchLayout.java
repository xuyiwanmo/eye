package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class SearchLayout extends FrameLayout {

    private LinearLayout mSearchLl;
    private LinearLayout mSearchEtLl;
    private TextView mSearchTv;
    private TextView mConfirmTv;
    private TextView mCancelTv;
    private EditText mEditEt;
    private Context mContext;
    private SearchCallback mCallback;
    private TextView mEmptyTv;

    public SearchLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.custom_view_search, this);
        mSearchLl = (LinearLayout) findViewById(R.id.search_ll);
        mSearchEtLl = (LinearLayout) findViewById(R.id.search_et_ll);
        mSearchTv = (TextView) findViewById(R.id.search_tv);
        mCancelTv = (TextView) findViewById(R.id.cancle_tv);
        mConfirmTv = (TextView) findViewById(R.id.confirm_tv);
        mEditEt = (EditText) findViewById(R.id.search_et);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SearchLayout);
        String title = a.getString(R.styleable.SearchLayout_search_title);
        boolean confirmVisible = a.getBoolean(R.styleable.SearchLayout_confirm_visible, false);

        mSearchLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditEt.getText().clear();
                mSearchLl.setVisibility(GONE);
                mSearchEtLl.setVisibility(VISIBLE);
                showKeyboard(mEditEt);
            }
        });

        mCancelTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchLl.setVisibility(VISIBLE);
                mSearchEtLl.setVisibility(GONE);
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditEt.getWindowToken(), 0);
                if (mCallback != null) {

                    mCallback.cancel();
                    if (mEmptyTv != null) {
                        mEmptyTv.setVisibility(INVISIBLE);
                    }
                }
            }
        });

        if (confirmVisible) {
            mConfirmTv.setVisibility(VISIBLE);
            mConfirmTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    if (mCallback != null) {

                        mCallback.fillData(mEditEt.getText().toString());
                        if (mEmptyTv != null) {
                            mEmptyTv.setVisibility(VISIBLE);
                        }
                    }
                }
            });
        } else {
            mConfirmTv.setVisibility(GONE);
            mEditEt.addTextChangedListener(new TextWatcher() {
                String temp = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    temp = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mCallback != null) {

                        mCallback.fillData(s.toString());
                        if (mEmptyTv != null) {
                            mEmptyTv.setVisibility(VISIBLE);
                        }
                    }
                }
            });
        }

        if (title != null) {
            mSearchTv.setText(title);
        }
    }

    public interface SearchCallback {

        void fillData(String s);

        void cancel();

    }

    public void setCallback(SearchCallback callback) {
        mCallback = callback;
    }

    public void setCallback(SearchCallback callback, TextView emptyTv) {
        mCallback = callback;
        mEmptyTv = emptyTv;
    }


    protected void showKeyboard(EditText editText) {

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    protected void hideKeyboard() {

        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((BaseActivity) mContext).getCurrentFocus().getWindowToken(), 0);

//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        if (imm.isActive())  //一直是true
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void setEditClickListener(OnClickListener onClickListener) {
        mSearchLl.setOnClickListener(onClickListener);
    }


}
