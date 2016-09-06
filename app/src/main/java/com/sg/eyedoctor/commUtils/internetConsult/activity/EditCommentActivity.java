package com.sg.eyedoctor.commUtils.internetConsult.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 填写评论
 */
@ContentView(R.layout.activity_edit_comment)
public class EditCommentActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.comment_et)
    private EditText mCommentEt;

    private String reviewerId;
    private String id;
    private int type;  //0为互联网会诊  1为眼科圈

    private NetCallback mCommentCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.comment_ok);
                KeyBoardUtils.hideKeyboard(mCommentEt);
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

        reviewerId = getIntent().getStringExtra(ConstantValues.KEY_REVIEWER_ID);
        id = getIntent().getStringExtra(ConstantValues.KEY_ID);
        type=getIntent().getIntExtra(ConstantValues.KEY_TYPE,ConstantValues.COMMENT_INTERNET);
        if (reviewerId == null || TextUtils.isEmpty(reviewerId)) {
            mActionbar.setTitle(R.string.edit_comment);
        } else {
            mActionbar.setTitle(R.string.reply_comment);
        }
        mActionbar.setRightBtnVisible(View.INVISIBLE);
        mActionbar.setRightTv(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = getText(mCommentEt);
                if (TextUtils.isEmpty(content)) {
                    showToast(R.string.empty_comment);
                    return;
                }
                showdialog();
                if(type==ConstantValues.COMMENT_INTERNET){
                    //互联网会诊
                    BaseManager.internetCommentAdd(id, ConstantValues.COMMENT_TYPE_COMMENT, content, reviewerId, mCommentCallback);
                }else{
                    //眼科圈
                    BaseManager.commentAdd(id, ConstantValues.COMMENT_TYPE_COMMENT, content, reviewerId, mCommentCallback);
                }
            }
        });
        mActionbar.setLeftTv(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                KeyBoardUtils.hideKeyboard(mCommentEt);
                AppManager.getAppManager().finishActivity();
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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        AppManager.getAppManager().finishActivity();
    }
}
