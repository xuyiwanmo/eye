package com.netease.nim.session.fragment;

import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.sg.eyedoctor.R;

/**
 * Created by zhoujianghua on 2015/9/10.
 */
public class TeamMessageFragment extends MessageFragment {

    private Team team;

    @Override
    public boolean isAllowSendMessage(IMMessage message) {

        //// TODO: 2016/6/15
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(getActivity(), R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
