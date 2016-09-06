package com.sg.eyedoctor.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sg.eyedoctor.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class EditView extends LinearLayout {


    TextView mNameTv;

    EditText mEdit;

    public EditView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_view_title_edit, this);

        mNameTv = (TextView) findViewById(R.id.name_tv);
        mEdit = (EditText) findViewById(R.id.name_et);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.EditView);

        String name = a.getString(R.styleable.EditView_left_name);
        mNameTv.setText(name);
        String hint = a.getString(R.styleable.EditView_right_hint);
        mEdit.setHint(hint);
        boolean type = a.getBoolean(R.styleable.EditView_is_password,false);
        if(type){
            mEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }


    }

    public String getEditText(){
        return mEdit.getText().toString();
    }



}
