package com.sg.eyedoctor.commUtils.toolbox.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.toolbox.adapter.ToolBoxGAdapter;
import com.sg.eyedoctor.commUtils.toolbox.bean.ImgText;
import com.sg.eyedoctor.common.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 工具箱
 */
@ContentView(R.layout.activity_tool_box)
public class ToolBoxActivity extends BaseActivity {
    public static final int TOOL_EYE_CHECK=0;
    public static final int TOOL_EYE_DATA=1;
    public static final int TOOL_EYE_MEDCINE=2;
    public static final int TOOL_EYE_CALCULATE=3;
    public static final int TOOL_EYE_ENGLISH=4;
    public static final int TOOL_EYE_CRYSTAL=5;

    @ViewInject(R.id.box_gv)
    private GridView mToolGv;

    ToolBoxGAdapter mToolBoxGAdapter;
    ArrayList<ImgText> mImgTexts = new ArrayList<>();


    @Override
    protected void initView() {
        mImgTexts.add(new ImgText(R.drawable.tool1, R.string.eye_check));
        mImgTexts.add(new ImgText(R.drawable.tool2, R.string.eye_data));
        mImgTexts.add(new ImgText(R.drawable.tool3, R.string.eye_medicine));
        mImgTexts.add(new ImgText(R.drawable.tool4, R.string.eye_calculate));
        mImgTexts.add(new ImgText(R.drawable.tool5, R.string.eye_english));
        mImgTexts.add(new ImgText(R.drawable.tool6, R.string.eye_crystal));

        mToolBoxGAdapter = new ToolBoxGAdapter(this, mImgTexts, R.layout.item_tool_box);
        mToolGv.setAdapter(mToolBoxGAdapter);
    }

    @Override
    protected void initListener() {
        mToolGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case TOOL_EYE_CHECK:
                        startActivity(new Intent(mContext,EyeCheckActivity.class));
                        break;
                    case TOOL_EYE_DATA:
                        startActivity(new Intent(mContext,EyeDataActivity.class));
                        break;
                    case TOOL_EYE_MEDCINE:
                        startActivity(new Intent(mContext,EyeMedicineTypeActivity.class));
                        break;
                    case TOOL_EYE_CALCULATE:
                        startActivity(new Intent(mContext,EyeCalculateActivity.class));
                        break;
                    case TOOL_EYE_ENGLISH:
                        startActivity(new Intent(mContext,EyeEnglishActivity.class));
                        break;
                    case TOOL_EYE_CRYSTAL:
                        startActivity(new Intent(mContext,EyeCrystalActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

}
