package com.sg.eyedoctor.commUtils.caseDiscuss.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.adapter.AllMemberHeadAdapter;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.contact.activity.FriendListActivity;
import com.sg.eyedoctor.contact.bean.FriendList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加新成员
 */
@ContentView(R.layout.activity_all_member)
public class AllMemberActivity extends BaseActivity {

    @ViewInject(R.id.head_gv)
    private GridView mHeadGv;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private AllMemberHeadAdapter mHeadAdapter;
    private ArrayList<CaseDiscussDetail.MemberList> mMemberLists = new ArrayList<>();
    private ArrayList<FriendList> mHeads = new ArrayList<>();
    private String teamId;
    private CaseDiscussDetail mCaseDiscussDetail;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                //云信
                NIMClient.getService(TeamService.class).addMembers(teamId, getHeadIm(mHeads))
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                // 返回onSuccess，表示拉人不需要对方同意，且对方已经入群成功了
                                closeDialog();
                                showToast(R.string.add_ok);
                                addMember();
                                sendMsg(teamId);
                            }

                            @Override
                            public void onFailed(int code) {
                                // 返回onFailed，并且返回码为810，表示发出邀请成功了，但是还需要对方同意
                            }

                            @Override
                            public void onException(Throwable exception) {

                            }
                        });

            }else{
                showToast(CommonUtils.getMsg(result));
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
        mCaseDiscussDetail = intent.getParcelableExtra(ConstantValues.EXTRA_RESULT);
        teamId=intent.getStringExtra(ConstantValues.KEY_TEAM_ID);
        mMemberLists = mCaseDiscussDetail.memberList;
        mActionbar.setTitle("全部成员(" + mMemberLists.size() + ")");
        mMemberLists.add(new CaseDiscussDetail.MemberList());
        mHeadAdapter = new AllMemberHeadAdapter(this, mMemberLists, 0);
        mHeadGv.setAdapter(mHeadAdapter);
        mActionbar.setRightBtnVisible(View.INVISIBLE);
    }

    @Override
    protected void initListener() {

        mHeadGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mMemberLists.size() - 1) {
                    Intent intent = new Intent(mContext, ChooseContactActivity.class);
                    for (CaseDiscussDetail.MemberList memberList : mMemberLists) {
                        mHeads.add(new FriendList(memberList.memberId, memberList.picFileName, memberList.userName));
                    }
                    intent.putExtra(ConstantValues.DIS_ID, mCaseDiscussDetail.disId);
                    intent.putParcelableArrayListExtra(ConstantValues.CHOOSE_HEAD, mHeads);
                    startActivityForResult(intent, ConstantValues.REQEST_HEAD_CODE);
                }
            }
        });
    }

    @Override
    protected void initActionbar() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.REQEST_HEAD_CODE) {
            if (resultCode == RESULT_OK) {
                mHeads= data.getParcelableArrayListExtra(FriendListActivity.EXTRA_DATA);
                if(mHeads==null||mHeads.size()==0){
                    return;
                }
                showdialog();
                BaseManager.discussionMemberAdd(mCaseDiscussDetail.disId, mHeads,mCallback);
            }
        }
    }

    private void addMember(){
        mMemberLists.remove(mMemberLists.size() - 1);
        for (FriendList head : mHeads) {
            mMemberLists.add(new CaseDiscussDetail.MemberList(head.friendId, head.userName, head.picFileName));
        }
        mMemberLists.add(new CaseDiscussDetail.MemberList());
        mActionbar.setTitle("全部成员(" + (mMemberLists.size()-1) + ")");
        mHeadAdapter.setData(mMemberLists);
    }

    private List<String> getHeadIm(ArrayList<FriendList> heads) {
        List<String> headIms=new ArrayList<>();
        for (FriendList head : heads) {
            headIms.add("d"+head.loginId);
        }
        LogUtils.i(headIms.toString());
        return headIms;
    }

    private void sendMsg(String teamId) {
        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(
                teamId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.Team, // 聊天类型，单聊或群组
                "欢迎进入讨论组" // 文本内容
        );
        Map<String, Object> map = new HashMap<>();
        map.put("order", mCaseDiscussDetail.disId);
        message.setRemoteExtension(map);
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }
}
