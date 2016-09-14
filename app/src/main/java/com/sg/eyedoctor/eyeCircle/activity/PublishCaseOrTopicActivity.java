package com.sg.eyedoctor.eyeCircle.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.adapter.AddImageGridAdapter;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.eyeCircle.fragment.CircleTopicCaseFragment;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 发布眼科圈病例
 */
@ContentView(R.layout.activity_publish_case_topic)
public class PublishCaseOrTopicActivity extends BaseActivity {

    private int mType = CircleTopicCaseFragment.TYPE_CASE;
    @ViewInject(R.id.cancel_tv)
    private TextView mCancelTv;
    @ViewInject(R.id.headline_tv)
    private TextView mHeadlineTv;
    @ViewInject(R.id.title_et)
    private EditText mTitleEt;
    @ViewInject(R.id.content_et)
    private EditText mContentEt;
    @ViewInject(R.id.arraw)
    private ImageView mArraw;
    @ViewInject(R.id.import_rl)
    private RelativeLayout mImportRl;
    @ViewInject(R.id.see_arraw)
    private ImageView mSeeArraw;
    @ViewInject(R.id.see_doctor_tv)
    private TextView mSeeDoctorTv;
    @ViewInject(R.id.notice_rl)
    private RelativeLayout mNoticeRl;
    @ViewInject(R.id.publish_btn)
    private Button mPublishBtn;
    @ViewInject(R.id.line)
    private View mLine;
    @ViewInject(R.id.image_gv)
    private GridView mImageGv;

    private ArrayList<String> mSelectPath;
    private AddImageGridAdapter mGridAdapter;
    private String title;
    private String detail;
    private String type;
    private ArrayList<PicBean> mPicBeans=new ArrayList<>();

    private boolean mIsAuth = false;
    //上传图片
    private NetCallback mPicCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PicBean>>() {
                }.getType();
                BaseArrayResp<PicBean> res = new Gson().fromJson(result, objectType);
                mPicBeans.addAll(res.value);
                BaseManager.topicRelease(title, detail, type, mPicBeans, mCallback);

            } else {
                showToast(R.string.get_data_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.publish_ok);
                AppManager.getAppManager().finishActivity();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };


    @Override
    protected void initView() {
        mType = getIntent().getIntExtra(ConstantValues.KEY_TYPE, CircleTopicCaseFragment.TYPE_TOPIC);
        if (mType == CircleTopicCaseFragment.TYPE_CASE) {
            mHeadlineTv.setText(R.string.publish_case);
            mLine.setVisibility(View.GONE);
            mImportRl.setVisibility(View.GONE);
            mTitleEt.setHint(R.string.enter_case_example_title);

        } else {
            mHeadlineTv.setText(R.string.publish_topic);
            mLine.setVisibility(View.GONE);
            mImportRl.setVisibility(View.GONE);
            mTitleEt.setHint(R.string.enter_case_topic);
        }
        mSelectPath = new ArrayList<>();
        mGridAdapter = new AddImageGridAdapter(this, mSelectPath, 4);
        mImageGv.setAdapter(mGridAdapter);
        mImageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mSelectPath.size()) {
                    chooseImg();
                } else {
                    //查看大图
                    Intent intent = new Intent(mContext, LookBigPicActivity.class);
                    ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                    for (String s : mSelectPath) {
                        picDataList.add(new PicLocalBean(s, s));
                    }
                    intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                    intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantValues.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mGridAdapter.setData(mSelectPath);
                setViewHeightBasedOnChildren(mImageGv, 16, 16);
            }
        }
    }

    @Event(R.id.publish_btn)
    private void publish(View view) {
        title = getText(mTitleEt);
        detail = getText(mContentEt);
        type = mType + "";
        if (TextUtils.isEmpty(title)) {
            showToast("标题" + getResources().getString(R.string.can_not_empty));
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            showToast("内容" + getResources().getString(R.string.can_not_empty));
            return;
        }
        showdialog();
        if (mSelectPath.size() == 0) {
            BaseManager.topicRelease(title, detail, type, new ArrayList<PicBean>(), mCallback);
        } else {
            BaseManager.pictureUpload(mSelectPath, mPicCallback);//上传图片
        }

    }

    @Event(R.id.cancel_tv)
    private void cancel(View view) {
        AppManager.getAppManager().finishActivity();
    }

    private void chooseImg() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, ConstantValues.REQUEST_IMAGE);
    }


}
