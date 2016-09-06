package com.sg.eyedoctor.eyeCircle.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
import com.sg.eyedoctor.commUtils.internetConsult.activity.EditCommentActivity;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.CommentInternetAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.InternetPictureAdapter;
import com.sg.eyedoctor.commUtils.internetConsult.adapter.SupportInternetAadpter;
import com.sg.eyedoctor.commUtils.internetConsult.bean.TopicCaseDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.PullState;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.NoScrollListView;
import com.sg.eyedoctor.common.view.NoScroolGridView;
import com.sg.eyedoctor.common.view.RoundImageView;
import com.sg.eyedoctor.eyeCircle.bean.Comment;
import com.sg.eyedoctor.eyeCircle.bean.TopicCase;
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
 * 眼科圈详情
 */
@ContentView(R.layout.activity_case_detail)
public class CaseDetailActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.store_img)
    private ImageView mStoreImg;
    @ViewInject(R.id.tv_team_topic_store)
    private TextView mBottomStoreTv;
    @ViewInject(R.id.topic_title_tv)
    private TextView mTopicTitleTv;
    @ViewInject(R.id.ll_team_topic_store)
    private LinearLayout mLlTeamTopicStore;
    @ViewInject(R.id.bottom_comment_tv)
    private TextView mBottomCommentTv;
    @ViewInject(R.id.bottom_comment_ll)
    private LinearLayout mBottomCommentLl;
    @ViewInject(R.id.bottom_support_img)
    private ImageView mBottomSupportImg;
    @ViewInject(R.id.bottom_support_tv)
    private TextView mBottomSupportTv;
    @ViewInject(R.id.bottom_support_ll)
    private LinearLayout mBottomSupportLl;
    @ViewInject(R.id.suport_all_ll)
    private LinearLayout mSuportAllLl;
    @ViewInject(R.id.head_img)
    private RoundImageView mHeadImg;
    @ViewInject(R.id.name_tv)
    private TextView mNameTv;
    @ViewInject(R.id.hospital_tv)
    private TextView mHospitalTv;

    @ViewInject(R.id.content_tv)
    private TextView mContentTv;
    @ViewInject(R.id.photo_gv)
    private NoScroolGridView mImgGv;
    @ViewInject(R.id.time_tv)
    private TextView mTimeTv;
    @ViewInject(R.id.delete_tv)
    private TextView mDeleteTv;

    @ViewInject(R.id.center_comment_count_tv)
    private TextView mCenterCommentCountTv;
    @ViewInject(R.id.comment_count_v)
    private View mCommentCountV;
    @ViewInject(R.id.center_support_count_tv)
    private TextView mCenterSupportCountTv;
    @ViewInject(R.id.support_count_v)
    private View mSupportCountV;
    @ViewInject(R.id.comment_rl)
    private RelativeLayout mCommentRl;

    @ViewInject(R.id.comment_lv)
    private NoScrollListView mCommentLv;
    @ViewInject(R.id.support_lv)
    private NoScrollListView mSupportLv;
    @ViewInject(R.id.refresh_ptrsv)
    private PullToRefreshScrollView mRefreshSv;
    @ViewInject(R.id.rootRl)
    private RelativeLayout mRootRl;

    private CommentInternetAdapter mCommentAdapter;
    private SupportInternetAadpter mSupportImgAadpter;
    private InternetPictureAdapter mPictureGridAdapter;
    private ArrayList<Comment> mComments;
    private ArrayList<Comment> mSupports;//点赞集合
    private TopicCase mTopicCase;
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
                BaseManager.topicDetailFind(mTopicCase.topicId, mDetailCallback);
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

                mComments.add(new Comment(mTopicCase.topicId, mDoctor.id, 2, mCommentContent, getCurrentTime(), mReviewerId, mDoctor.picFileName, mDoctor.userName, mReviewerName));
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
                BaseManager.topicDetailFind(mTopicCase.topicId, mDetailCallback);
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

    private NetCallback mDetailCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<TopicCaseDetail>>() {
                }.getType();
                BaseResp<TopicCaseDetail> res = new Gson().fromJson(result, objectType);
                mDetail = res.value;
                initSupportAndComment(res.value.commentList);

                mTopicCase.createDate = mDetail.createDate;
                mTopicCase.userName = mDetail.userName;
                mTopicCase.picFileName = mDetail.picFileName;
                mTopicCase.licenseHospital = mDetail.licenseHospital;
                mTopicCase.licenseTitle = mDetail.licenseTitle;
                mTopicCase.licenseDept = mDetail.licenseDept;
                mTopicCase.picList = mDetail.picList;
                mTopicCase.title = mDetail.title;
                mTopicCase.detail = mDetail.detail;
                mTopicCase.isCollect = mDetail.isCollect;
                mTopicCase.collectCount = mDetail.collectCount;
                mTopicCase.commentList = mDetail.commentList;

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

    @Override
    protected void initView() {

        Intent intent = getIntent();
        mTopicCase = intent.getParcelableExtra(ConstantValues.KEY_DATA);
        mComments = new ArrayList<>();
        mSupports = new ArrayList<>();
        mTopicCase.picList = new ArrayList<>();
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
                BaseManager.topicDetailFind(mTopicCase.topicId, mDetailCallback);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        mImgGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, LookBigPicActivity.class);
                intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                ArrayList<PicLocalBean> picDataList = new ArrayList<>();
                for (Picture s : mTopicCase.picList) {
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
                intent.putExtra(ConstantValues.KEY_ID, mTopicCase.topicId);
                intent.putExtra(ConstantValues.KEY_TYPE, 1);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initActionbar() {

    }

    @Event(R.id.bottom_comment_ll)
    private void comment(View view) {
        Intent intent = new Intent(mContext, EditCommentActivity.class);
        intent.putExtra(ConstantValues.KEY_REVIEWER_ID, mReviewerId);
        intent.putExtra(ConstantValues.KEY_ID, mTopicCase.topicId);
        intent.putExtra(ConstantValues.KEY_TYPE, 1);

        startActivity(intent);
    }

    @Event(R.id.bottom_support_ll)
    private void support(View view) {
        showdialog();
        if (isSupport()) {
            BaseManager.circleCommentDelete(mSupportId, mCancelSupportCallback);
        } else {
            BaseManager.commentAdd(mTopicCase.topicId, ConstantValues.COMMENT_TYPE_SUPPORT, "", "", mSupportCallback);
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
            BaseManager.internetCommentAdd(mTopicCase.topicId, ConstantValues.COMMENT_TYPE_COMMENT, mCommentContent, mReviewerId, mCommentCallback);
        }
    }


    @Event(R.id.ll_team_topic_store)
    private void store(View view) {

        if (mTopicCase.isCollect.equals("true")) {//取消收藏
            showdialog();
            BaseManager.collectionCancel(mTopicCase.topicId, mRemoveCallback);
        } else {
            String picture = "";
            if (mTopicCase.picList != null && mTopicCase.picList.size() > 0) {
                picture = mTopicCase.picList.get(0).picUrl;
            }
            showdialog();
            String title = mTopicCase.title;
            if (title.length() > 50) {
                title = title.substring(0, 50);
            }
            BaseManager.collectionAdd(mTopicCase.topicId, mDoctor.id, ConstantValues.STORE_CIRCLE + "", title, picture, "", mStoreCallback);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.topicDetailFind(mTopicCase.topicId, mDetailCallback);
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

        mTopicCase.commentCount = mComments.size();
        mTopicCase.upvoteCount = mSupports.size();

        initCount();
    }

    private void initCount() {
        if (isSupport()) {
            mBottomSupportImg.setImageResource(R.drawable.ic_support_check);
        } else {
            mBottomSupportImg.setImageResource(R.drawable.ic_support);
        }
        setTextView(mCenterCommentCountTv, "评论:" + mComments.size());
        setTextView(mCenterSupportCountTv, "赞:" + mSupports.size());
        setTextView(mBottomSupportTv, mSupports.size() + "");
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
        setTextView(mNameTv, mTopicCase.userName);
        setTextView(mContentTv, mTopicCase.detail);

        setTextView(mHospitalTv, mTopicCase.licenseHospital);
        setTextView(mTopicTitleTv, "#" + mTopicCase.title + "#");

        setTextView(mTimeTv, mTopicCase.createDate);
        setTextView(mBottomStoreTv, mTopicCase.collectCount + "");
        setTextView(mBottomSupportTv, mTopicCase.upvoteCount + "");
        setTextView(mBottomCommentTv, mTopicCase.commentCount + "");
        mStoreImg.setSelected(mTopicCase.isCollect.equals("true"));
        CommonUtils.loadImg(mTopicCase.picFileName, mHeadImg);
        mPictureGridAdapter.setData(mTopicCase.picList);

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
                            BaseManager.topicDelete(mTopicCase.topicId, mDeleteCallback);
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
        mTopicCase.isCollect = b + "";
        mStoreImg.setSelected(b);
        if (b) {
            mTopicCase.collectCount = mTopicCase.collectCount + 1;
        } else {
            mTopicCase.collectCount = mTopicCase.collectCount - 1;
        }

        setTextView(mBottomStoreTv, mTopicCase.collectCount + "");
        initCount();
    }
}
