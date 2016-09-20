package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.netease.nim.session.SessionHelper;
import com.netease.nim.session.activity.TeamMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.activity.ChartFileActivity;
import com.sg.eyedoctor.chartFile.bean.MedicalRecord;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.AddRoundImgGridAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussResult;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.MemberBean;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.adapter.AddUrlImageGridAdapter;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.SpeechManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.AppManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.contact.activity.FriendListActivity;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.lookPicture.activity.LookBigPicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sg.eyedoctor.common.utils.UiUtils.setViewHeightBasedOnChildren;

/**
 * 添加患者病例
 */
@ContentView(R.layout.activity_add_case)
public class AddCaseActivity extends BaseActivity {
    public static final int MAX_PHOTO = 9;

    @ViewInject(R.id.name_et)
    private EditText mNameEt;
    @ViewInject(R.id.male_img)
    private ImageView mMaleImg;
    @ViewInject(R.id.both_eye_tv)
    private TextView mTvAddPatientMale;
    @ViewInject(R.id.female_img)
    private ImageView mFemaleImg;
    @ViewInject(R.id.tv_add_patient_female)
    private TextView mTvAddPatientFemale;
    @ViewInject(R.id.age_et)
    private EditText mAgeEt;
    @ViewInject(R.id.diagnose_tv)
    private EditText mDiagnoseEt;//诊断结果
    @ViewInject(R.id.diagnose_img)
    private ImageView mDiagnoseImg;
    @ViewInject(R.id.illness_et)
    private EditText mIllnessEt;
    @ViewInject(R.id.illness_img)
    private ImageView mIllnessImg;
    @ViewInject(R.id.add_photo_gv)
    private GridView mAddPhotoGv;
    @ViewInject(R.id.add_head_gv)
    private GridView mAddHeadGv;
    @ViewInject(R.id.save_tv)
    private TextView mSaveTv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private int mSex = 1;  //0女  1男
    private ArrayList<FriendList> mHeads = new ArrayList<>();

    private AddUrlImageGridAdapter mRecAdapter;
    private AddRoundImgGridAdapter mRoundAdapter;
    private CaseDiscussResult mCaseDiscussResult;
    private SpeechManager mSpeechManager;
    private MedicalRecord mRecord;

    private ArrayList<String> mCameraPath = new ArrayList<>();//相册图片
    private ArrayList<String> mDeletePath = new ArrayList<>();//删除的地址图片
    private ArrayList<PicBean> mPicBeans = new ArrayList<>();//导入图片
    private ArrayList<PicLocalBean> mAllPics = new ArrayList<>();//所有图片

    private Team mTeam;
    //上传图片
    private NetCallback mPicCallback = new NetCallback(mContext) {
        @Override
        protected void requestOK(String result) {
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PicBean>>() {
                }.getType();
                BaseArrayResp<PicBean> res = new Gson().fromJson(result, objectType);
                mPicBeans.addAll(res.value);
                BaseManager.discussionAdd(mPicBeans, getText(mNameEt), mSex + "", getText(mAgeEt), getText(mIllnessEt), getText(mDiagnoseEt), getDoctorId(mHeads), mTeam.getId(), mCallback);
            } else {
                showToast(R.string.get_data_error);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {

                Type objectType = new TypeToken<BaseResp<CaseDiscussResult>>() {
                }.getType();
                BaseResp<CaseDiscussResult> res = new Gson().fromJson(result, objectType);
                mCaseDiscussResult = res.value;
                sendMsg(mCaseDiscussResult.teamId);
                if (!CommonUtils.isLogin()){
                    showToast(R.string.operation_not_open);
                    AppManager.getAppManager().finishActivity();
                    return;
                }
                TeamMessageActivity.start(mContext, mCaseDiscussResult.teamId, mCaseDiscussResult.patientName, SessionHelper.getTeamCustomization(mCaseDiscussResult.disId), null);
                AppManager.getAppManager().finishActivity();
            } else {
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
        mActionbar.setTitle(R.string.add_patient_case);
        mActionbar.setRightBtnVisible(View.GONE);
        mActionbar.setRightTv(R.string.import_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ChartFileActivity.class), ConstantValues.REQUEST_IMPORT_CASE);
            }
        });

        mMaleImg.setSelected(true);
        mRecAdapter = new AddUrlImageGridAdapter(this, mAllPics, 4);
        mAddPhotoGv.setAdapter(mRecAdapter);
        mAddPhotoGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mAllPics.size()) {
                    chooseImg();
                } else {
                    //查看大图
                    Intent intent = new Intent(mContext, LookBigPicActivity.class);

                    intent.putExtra(LookBigPicActivity.CURRENTITEM, position);
                    intent.putExtra(LookBigPicActivity.PICDATALIST, mAllPics);
                    intent.putExtra(LookBigPicActivity.EDIT, true);
                    startActivityForResult(intent, ConstantValues.REQUEST_DELETE_IMAGE);
                }
            }
        });

        mRoundAdapter = new AddRoundImgGridAdapter(this, mHeads, 4);
        mAddHeadGv.setAdapter(mRoundAdapter);
        mAddHeadGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mHeads.size()) {
                    for (FriendList head : mHeads) {
                        //   head.isChecked=true;
                    }
                    Intent intent = new Intent(mContext, ChooseContactActivity.class);
                    intent.putParcelableArrayListExtra(ConstantValues.CHOOSE_HEAD, mHeads);
                    startActivityForResult(intent, ConstantValues.REQEST_HEAD_CODE);
                }
            }
        });

        mSpeechManager = new SpeechManager(mContext);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initActionbar() {
    }

    /**
     * 开始讨论
     */
    @Event(R.id.save_tv)
    private void save(View view) {
        if (CommonUtils.isEmpty(mNameEt)) {
            showToast("姓名" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mAgeEt)) {
            showToast("年龄" + getString(R.string.can_not_empty));
            return;
        }
        if (!CommonUtils.isNumeric(mAgeEt.getText().toString())) {
            showToast("年龄" + getString(R.string.can_not_num));
            return;
        }
        if (Long.valueOf(mAgeEt.getText().toString()) > 150) {
            showToast("年龄不能超过150岁");
            return;
        }
        if (CommonUtils.isEmpty(mDiagnoseEt)) {
            showToast("疾病诊断" + getString(R.string.can_not_empty));
            return;
        }
        if (CommonUtils.isEmpty(mIllnessEt)) {
            showToast("症状描述" + getString(R.string.can_not_empty));
            return;
        }
        if (mHeads.size() == 0) {
            showToast("邀请医生" + getString(R.string.can_not_empty));
            return;
        }
        showdialog();
        createDiscussMember();

    }

    @Event(R.id.male_img)
    private void male(View view) {
        mSex = 1;
        mMaleImg.setSelected(true);
        mFemaleImg.setSelected(false);
    }

    @Event(R.id.female_img)
    private void female(View view) {
        mSex = 0;
        mMaleImg.setSelected(false);
        mFemaleImg.setSelected(true);
    }

    @Event(R.id.diagnose_img)
    private void diagnoseSpeech(View view) {
        mSpeechManager.showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mDiagnoseEt.append(result);
                }
            }
        });
    }

    @Event(R.id.illness_img)
    private void IllnessSpeech(View view) {
        mSpeechManager.showSpeechDialog(new SpeechManager.SpeechCallback() {
            @Override
            public void printResult(String result, boolean isLast) {
                if (isLast) {
                    mIllnessEt.append(result);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantValues.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                Iterator<PicLocalBean> iterator = Collections.synchronizedList(mAllPics).iterator();
                while (iterator.hasNext()) {
                    PicLocalBean item = iterator.next();
                    if (item.localUrl != null) {
                        iterator.remove();
                    }
                }

                mCameraPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String s : mCameraPath) {
                    PicLocalBean bean = new PicLocalBean();
                    bean.localUrl = s;
                    mAllPics.add(bean);
                }

                notifyPicAdapter();
            }
        } else if (requestCode == ConstantValues.REQEST_HEAD_CODE) {
            if (resultCode == RESULT_OK) {
                mHeads = data.getParcelableArrayListExtra(FriendListActivity.EXTRA_DATA);
                mRoundAdapter.setData(mHeads);
                UiUtils.setViewHeightBasedOnChildren(mAddHeadGv, 14, 14);
                mRoundAdapter.notifyDataSetChanged();

                if (mHeads.size() != 0) {
                    mSaveTv.setBackgroundResource(R.drawable.bg_blue_text_white);
                } else {
                    mSaveTv.setBackgroundResource(R.drawable.bg_auth_grey);
                }
            }
        } else if (requestCode == ConstantValues.REQUEST_IMPORT_CASE) {
            if (resultCode == RESULT_OK) {
                mRecord = data.getParcelableExtra(ConstantValues.KEY_RECORD);
                setTextView(mNameEt, mRecord.name);
                setTextView(mIllnessEt, mRecord.illness);
                setTextView(mDiagnoseEt, mRecord.diagnosis);//诊断结果
                if (mRecord.sex == 1) {
                    male(mMaleImg);
                } else {
                    female(mFemaleImg);
                }

                setTextView(mAgeEt, mRecord.age + "");
                mAllPics.clear();
                mCameraPath.clear();
                if (mRecord.picList != null) {
                    mPicBeans = mRecord.picList;
                    for (PicBean picBean : mRecord.picList) {
                        mAllPics.add(new PicLocalBean(picBean.picUrl, picBean.picUrl));
                    }
                }
                notifyPicAdapter();
            }
        } else if (requestCode == ConstantValues.REQUEST_DELETE_IMAGE) {
            if (resultCode == RESULT_OK) {
                mDeletePath = data.getStringArrayListExtra(ConstantValues.KEY_DELETE);
                LogUtils.i("mDeletePath  :"+mDeletePath.size());
                int size = mAllPics.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        PicLocalBean bean = mAllPics.get(i);
                        if ((bean.picUrl != null && bean.picUrl.equals(s))
                                || (bean.localUrl != null && bean.localUrl.equals(s))) {
                            size--;
                            mAllPics.remove(i);
                        }
                    }
                }

                //删除本地相册
                size = mCameraPath.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        String bean = mCameraPath.get(i);
                        if (bean.equals(s)) {
                            size--;
                            mCameraPath.remove(i);
                        }
                    }
                }

                //删除网络
                size = mPicBeans.size();
                //从总图片集合删除
                for (String s : mDeletePath) {
                    for (int i = 0; i < size; i++) {
                        PicBean bean = mPicBeans.get(i);
                        if (bean.picUrl != null && bean.picUrl.equals(s)) {
                            size--;
                            mPicBeans.remove(i);
                        }
                    }
                }

                notifyPicAdapter();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeechManager.destroy();
    }


    private void chooseImg() {

        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, MAX_PHOTO - mAllPics.size());
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (mCameraPath != null && mCameraPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mCameraPath);
        }
        startActivityForResult(intent, ConstantValues.REQUEST_IMAGE);
    }

    private ArrayList<MemberBean> getDoctorId(ArrayList<FriendList> heads) {
        ArrayList<MemberBean> ids = new ArrayList<>();
        for (FriendList head : heads) {
            ids.add(new MemberBean(head.loginId));
        }
        return ids;
    }

    private ArrayList<String> getDoctorImId(ArrayList<FriendList> heads) {
        ArrayList<String> ids = new ArrayList<>();
        for (FriendList head : heads) {
            ids.add("d" + head.loginId);
        }
        ids.add("d" + mDoctor.loginid);
        LogUtils.i("---------member:   " + ids.toString());
        return ids;
    }

    private void createDiscussMember() {
        // 群组类型
        TeamTypeEnum type = TeamTypeEnum.Normal;
        // 创建时可以预设群组的一些相关属性，如果是普通群，仅群名有效。
        // fields 中，key 为数据字段，value 对对应的值，该值类型必须和 field 中定义的 fieldType 一致
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<TeamFieldEnum, Serializable>();
        fields.put(TeamFieldEnum.Name, getText(mNameEt));
        fields.put(TeamFieldEnum.Introduce, getText(mIllnessEt));
        //   fields.put(TeamFieldEnum.VerifyType, verifyType);
        NIMClient.getService(TeamService.class).createTeam(fields, type, "", getDoctorImId(mHeads)).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                mTeam = team;
                if (mCameraPath.size() == 0) {
                    BaseManager.discussionAdd(mPicBeans, getText(mNameEt), mSex + "", getText(mAgeEt), getText(mIllnessEt), getText(mDiagnoseEt), getDoctorId(mHeads), team.getId(), mCallback);
                } else {
                    BaseManager.pictureUpload(mCameraPath, mPicCallback);//上传图片
                }
            }

            @Override
            public void onFailed(int i) {
                LogUtils.i("onFailed Team  :" + i);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void sendMsg(String teamId) {

        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(
                teamId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.Team, // 聊天类型，单聊或群组
                "欢迎进入讨论组" // 文本内容
        );
        Map<String, Object> map = new HashMap<>();
        map.put("order", mCaseDiscussResult.disId);
        message.setRemoteExtension(map);
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }

    private void notifyPicAdapter() {
        mRecAdapter.setData(mAllPics);
        setViewHeightBasedOnChildren(mAddPhotoGv, 16, 16);
        mRecAdapter.notifyDataSetChanged();
    }
}
