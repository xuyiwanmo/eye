package com.sg.eyedoctor.commUtils.internetConsult.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.CommentInternetAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.InternetPictureAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.SupportInternetAadpter;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.commUtils.internetConsult.bean.TopicCaseDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.common.view.NoScroolGridView;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;
import com.sg.eyedoctor.lookPicture.bean.Picture;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 互联网会诊详情界面
 */
@ContentView(R.layout.activity_internet_consult_detail)
public class InternetConsultDetailActivity extends BaseActivity {

    @ViewInject(R.id.center_comment_count_tv)
    private TextView mCenterCommentCountTv;
    @ViewInject(R.id.center_support_count_tv)
    private TextView mCenterSupportCountTv;
    @ViewInject(R.id.comment_count_v)
    private View mCommentCountV;

    @ViewInject(R.id.support_count_v)
    private View mSupportCountV;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.suport_all_ll)
    private LinearLayout mSupportAllLl;
    @ViewInject(R.id.ll_team_topic_store)
    private LinearLayout mStoreAllLl;

    @ViewInject(R.id.bottom_comment_ll)
    private LinearLayout mBottomCommentLl;
    @ViewInject(R.id.bottom_comment_count_tv)
    private TextView mBottomCommentCountTv;
    @ViewInject(R.id.rootRl)
    private RelativeLayout mRootRl;
    @ViewInject(R.id.refresh_ptrsv)
    private PullToRefreshScrollView mRefreshSv;
    //底部输入框
    @ViewInject(R.id.bottom_edit)
    private LinearLayout mEditLl;
    @ViewInject(R.id.comment_et)
    private EditText mCommentEt;
    @ViewInject(R.id.add_img)
    private ImageView mAddImg;
    @ViewInject(R.id.store_img)
    private ImageView mStoreImg;
    @ViewInject(R.id.tv_team_topic_store)
    private TextView mStoreCountTv;
    @ViewInject(R.id.bottom_support_tv)
    private TextView mBottomSupportCountTv;
    //底部点赞
    @ViewInject(R.id.support_ll)
    private LinearLayout mSupportLl;
    @ViewInject(R.id.support_img)
    private ImageView mBottomSupportImg;
    @ViewInject(R.id.job_tv)
    private TextView mJobTv;
    @ViewInject(R.id.comment_lv)
    private NoScrollListView mCommentLv;
    @ViewInject(R.id.support_lv)
    private NoScrollListView mSupportLv;
    @ViewInject(R.id.head_img)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.hospital_tv)
    private TextView mHospitalTv;
    @ViewInject(R.id.photo_gv)
    private NoScroolGridView mImgGv;
    @ViewInject(R.id.content_tv)
    private TextView mContentTv;
    @ViewInject(R.id.internet_title_tv)
    private TextView mTitleTv;
    @ViewInject(R.id.time_tv)
    private TextView mTimeTv;

    private CommentInternetAdapter mCommentAdapter;
    private SupportInternetAadpter mSupportImgAadpter;
    private InternetPictureAdapter mPictureGridAdapter;
    private ArrayList<Comment> mComments;
    private ArrayList<Comment> mSupports;//点赞集合
    private InternetConsult mTopicCase;
    private String mReviewerId = ""; //回复人
    private String mReviewerName = ""; //回复人
    private PullState mPullState = PullState.NORMAL;
    private String mCommentContent;
    private TopicCaseDetail mDetail;
    private String mSupportId;
    //收藏
    private NetCallback mStoreCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.store_ok);
                initStoreCount(true);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    //取消收藏
    private NetCallback mRemoveCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.remove_store_ok);
                initStoreCount(false);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    //取消点赞
    private NetCallback mCancelSupportCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.cancel_support_ok);
                BaseManager.netConsultationDetailFind(mTopicCase.id, mDetailCallback);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    //评论列表
    private NetCallback mCommentCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.comment_ok);

                mComments.add(new Comment(mTopicCase.id, mDoctor.id, 2, mCommentContent, getCurrentTime(), mReviewerId, mDoctor.picFileName, mDoctor.userName, mReviewerName));
                mCommentAdapter.setData(mComments);
                initCount();
                mRefreshSv.getRefreshableView().fullScroll(ScrollView.FOCUS_DOWN);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mSupportCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                BaseManager.netConsultationDetailFind(mTopicCase.id, mDetailCallback);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mDetailCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<TopicCaseDetail>>() {
                }.getType();
                BaseResp<TopicCaseDetail> res = new Gson().fromJson(result, objectType);
                mDetail = res.value;
                initSupportAndComment(mDetail.commentList);
                viewInit();
                refreshComplete(mRefreshSv);
            }else{
                showToast(R.string.operation_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mDeleteCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.delete_success);
                AppManager.getAppManager().finishActivity();
            } else {
                showToast(R.string.delete_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {

        Intent intent = getIntent();
        mTopicCase = (InternetConsult) intent.getParcelableExtra(ConstantValues.KEY_DATA);
        mComments = new ArrayList<>();
        mSupports = new ArrayList<>();

        mPictureGridAdapter = new InternetPictureAdapter(this, mTopicCase.picList, R.layout.item_grid_image);
        mSupportImgAadpter = new SupportInternetAadpter(this, mSupports, R.layout.item_support_internet);
        mSupportLv.setAdapter(mSupportImgAadpter);
        mImgGv.setAdapter(mPictureGridAdapter);

        mCommentAdapter = new CommentInternetAdapter(this, mComments, R.layout.item_comment);
        mCommentLv.setAdapter(mCommentAdapter);

    }

    @Override
    protected void initListener() {
        mRefreshSv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mRefreshSv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                showdialog();
                BaseManager.netConsultationDetailFind(mTopicCase.id, mDetailCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        mImgGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InternetConsultDetailActivity.this, LookBigPicActivity.class);
                intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                for (Picture s : mDetail.picList) {
                    picDataList.add(new PicLocalBean(s.picUrl,s.picUrl));
                }
                intent.putExtra(LookBigPicActivity.PICDATALIST, picDataList);
                startActivity(intent);
            }
        });

        mCommentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = mComments.get(position);
                mReviewerId = comment.doctorId;
                //马上添加评论
                mReviewerName = comment.userName;
                Intent intent = new Intent(mContext, EditCommentActivity.class);
                intent.putExtra(ConstantValues.KEY_REVIEWER_ID, mReviewerId);
                intent.putExtra(ConstantValues.KEY_ID, mTopicCase.id);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.comment_et)
    private void edit(View view) {
        KeyBoardUtils.showKeyboard(mCommentEt);
    }

    @Event(R.id.add_img)
    private void addComment(View view) {
        BaseManager.internetCommentAdd(mTopicCase.id, ConstantValues.COMMENT_TYPE_COMMENT, getText(mCommentEt), mReviewerId, mCommentCallback);
    }

    @Event(R.id.bottom_comment_ll)
    private void comment(View view) {
        Intent intent = new Intent(mContext, EditCommentActivity.class);
        intent.putExtra(ConstantValues.KEY_REVIEWER_ID, mReviewerId);
        intent.putExtra(ConstantValues.KEY_ID, mTopicCase.id);

        startActivity(intent);
    }

    @Event(R.id.support_ll)
    private void support(View view) {
        showdialog();
        if (isSupport()) {
            BaseManager.commentDelete(mSupportId, mCancelSupportCallback);

        } else {
            BaseManager.internetCommentAdd(mTopicCase.id, ConstantValues.COMMENT_TYPE_SUPPORT, "", "", mSupportCallback);
        }
    }

    @Event(R.id.center_comment_count_tv)
    private void clickCommentCount(View view) {
        checkToCommentTab(View.VISIBLE, View.GONE);
    }

    @Event(R.id.center_support_count_tv)
    private void clickSuppotCount(View view) {
        checkToCommentTab(View.INVISIBLE, View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            mCommentContent = data.getExtras().getString(ConstantValues.KEY_DATA);
            BaseManager.internetCommentAdd(mTopicCase.id, ConstantValues.COMMENT_TYPE_COMMENT, mCommentContent, mReviewerId, mCommentCallback);
        }
    }

    @Event(R.id.ll_team_topic_store)
    private void store(View view) {

        if (mDetail.isCollect.equals("true")) {//取消收藏
            showdialog();
            BaseManager.collectionCancel(mDetail.id, mRemoveCallback);
        } else {
            String picture = "";
            if (mDetail.picList != null && mDetail.picList.size() > 0) {
                picture = mDetail.picList.get(0).picUrl;
            }
            String title=mDetail.title;
            if(title.length()>50){
                title=title.substring(0,50);
            }
            showdialog();
            BaseManager.collectionAdd(mDetail.id, mDoctor.id, ConstantValues.STORE_INTERNET + "", title, picture, "", mStoreCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.netConsultationDetailFind(mTopicCase.id, mDetailCallback);
    }

    public String getCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    private void checkToCommentTab(int visible, int invisible) {
        mCommentLv.setVisibility(visible);
        mSupportLv.setVisibility(invisible);
        mCommentCountV.setVisibility(visible);
        mSupportCountV.setVisibility(invisible);
    }

    private void refreshComplete(
            final PullToRefreshScrollView refreshView) {
        refreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshView.onRefreshComplete();
                mPullState = PullState.NORMAL;
            }
        }, 200);
    }


    private void initSupportAndComment(ArrayList<Comment> commentList) {
        if (commentList == null) {
            return;
        }
        mSupports.clear();
        mComments.clear();

        for (Comment comment : commentList) {
            if (ConstantValues.COMMENT_TYPE_COMMENT.equals(comment.commentType + "")) {
                mComments.add(comment);
            } else if (ConstantValues.COMMENT_TYPE_SUPPORT.equals(comment.commentType + "")) {
                mSupports.add(comment);
            }
        }
        mCommentAdapter.setData(mComments);
        mSupportImgAadpter.setData(mSupports);

        mDetail.commentCount = mComments.size();
        mDetail.upvoteCount = mSupports.size();

        initCount();
    }

    private void initCount() {
        if (isSupport()) {
            mBottomSupportImg.setImageResource(R.drawable.ic_support_check);
        } else {
            mBottomSupportImg.setImageResource(R.drawable.ic_support);
        }
        setTextView(mCenterCommentCountTv, "评论：" + mComments.size());
        setTextView(mCenterSupportCountTv, "赞：" + mSupports.size());
        setTextView(mBottomSupportCountTv, mSupports.size() + "");
        setTextView(mBottomCommentCountTv, mComments.size() + "");
    }

    //是否已经支持
    private boolean isSupport() {
        for (Comment support : mSupports) {
            if (support.doctorId.equals(mDoctor.id)) {
                mSupportId = support.id;
                return true;
            }
        }
        return false;
    }

    private void viewInit() {
        setTextView(mNameTv, mDetail.userName);
        StringBuffer content = new StringBuffer();
        content.append(mDetail.sex.equals("0") ? "女" : "男").append("性患者，").append(mDetail.age + "岁，" + getText(mDetail.illDetail, ""));
        if (mDetail.illness != null && !mDetail.illness.equals("")) {
            content.append(" 诊断：" + mDetail.illness);
        }
        if (mDetail.detail != null && !mDetail.detail.equals("")) {
            content.append(" 备注：" + mDetail.detail);
        }
        setTextView(mContentTv, content.toString());
        setTextView(mTitleTv, "#" + mDetail.title + "#");
        setTextView(mHospitalTv, mDetail.licenseHospital);
        setTextView(mJobTv, mDetail.licenseTitle);
        setTextView(mTimeTv, mDetail.createDate);
        setTextView(mStoreCountTv, mDetail.collectCount + "");
        setTextView(mBottomSupportCountTv, mDetail.upvoteCount + "");

        mStoreImg.setSelected(mDetail.isCollect.equals("true"));
        CommonUtils.loadImg(mDetail.picFileName, mHeadImg);
        mPictureGridAdapter.setData(mDetail.picList);

        initBar();
    }

    private void initBar() {
        if (mDetail.doctorId.equals(BaseManager.getDoctor().id)) {
            mActionbar.setRightTv(R.string.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogManager.showConfimCancelDlg(mContext, R.string.delete_confirm, new DlgClick() {
                        @Override
                        public boolean click() {
                            showdialog();
                            BaseManager.netConsultationDelete(mDetail.id, mDeleteCallback);
                            return false;
                        }
                    }, new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });
                }
            });
        }
    }

    private void initStoreCount(boolean b) {
        mDetail.isCollect = b + "";
        mStoreImg.setSelected(b);
        if (b) {
            mDetail.collectCount = mDetail.collectCount + 1;
        } else {
            mDetail.collectCount = mDetail.collectCount - 1;
        }
        setTextView(mStoreCountTv, mDetail.collectCount + "");
        initCount();
    }



}
