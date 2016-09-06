package com.netease.nim.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.cache.FriendDataCache;
import com.netease.nim.cache.SimpleCallback;
import com.netease.nim.cache.TeamDataCache;
import com.netease.nim.session.SessionCustomization;
import com.netease.nim.session.constant.Extras;
import com.netease.nim.session.fragment.MessageFragment;
import com.netease.nim.session.fragment.TeamMessageFragment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.caseDiscuss.activity.AllMemberActivity;
import com.sg.eyedoctor.commUtils.caseDiscuss.activity.LookPatientCaseActivity;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.CaseDiscussDetail;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 群聊界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
@ContentView(R.layout.nim_team_message_activity)
public class TeamMessageActivity extends BaseMessageActivity {

    private CaseDiscussDetail mCaseDiscussDetail;
    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseResp<CaseDiscussDetail>>() {}.getType();
                BaseResp<CaseDiscussDetail> res = new Gson().fromJson(result, objectType);
                mCaseDiscussDetail=res.value;
            }
        }

        @Override
        protected void timeOut() {
        //    onTimeOut();
        }
    };

    private Team team;
    private String teamId;

    @ViewInject(R.id.invalid_team_tip)
    private View invalidTeamTipView;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    private TeamMessageFragment fragment;

    private Class<? extends Activity> backToClass;


    private String mDisId;
    private String mDisName;

    public static void start(Context context, String tid, String name,SessionCustomization customization, Class<? extends Activity> backToClass) {
        Intent intent = new Intent();

        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        String dis= (String) customization.map.get("order");
        intent.putExtra(ConstantValues.DIS_ID, dis);
        intent.putExtra(ConstantValues.DIS_NAME, name);
        intent.setClass(context, TeamMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
        teamId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        mDisName=getIntent().getStringExtra(ConstantValues.DIS_NAME);
        mDisId = getIntent().getStringExtra(ConstantValues.DIS_ID);
        registerTeamUpdateObserver(true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestTeamInfo();
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = TeamDataCache.getInstance().getTeamById(sessionId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(TeamMessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT);
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        fragment.setTeam(team);

        mActionbar.setTitle(mDisName + "(" + team.getMemberCount() + "人)");

        //添加成员
        mActionbar.setSecondBtnImg(R.drawable.ic_add_doctor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamMessageActivity.this, AllMemberActivity.class);
                intent.putExtra(ConstantValues.EXTRA_RESULT, mCaseDiscussDetail);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
            }
        });
        mActionbar.setSecondbtnVisible(View.VISIBLE);
        mActionbar.setRightBtnImg(R.drawable.ic_see_case, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamMessageActivity.this, LookPatientCaseActivity.class);
                intent.putExtra(ConstantValues.EXTRA_RESULT, mCaseDiscussDetail);
                startActivity(intent);
            }
        });

        if (mDisId != null) {
            showdialog();
            BaseManager.discussionDetailFind(mDisId, mCallback);
        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {

        }
    };

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            fragment.refreshMessageList();
        }
    };

    @Override
    protected MessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
        fragment = new TeamMessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
