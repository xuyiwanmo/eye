package com.sg.eyedoctor.main.fragment;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.chuwe1.pulltorefreshandswipemenu.library.PullToRefreshAndSwipeMenuExpandableListView;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuCreator;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuExpandableListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.chartFile.activity.AttentionPatientActivity;
import com.sg.eyedoctor.chartFile.activity.PatientCenterActivity;
import com.sg.eyedoctor.chartFile.activity.SearchChartFileActivity;
import com.sg.eyedoctor.chartFile.adapter.MedicalRecordExpandAdapter;
import com.sg.eyedoctor.chartFile.bean.Group;
import com.sg.eyedoctor.chartFile.bean.PatientByGroup;
import com.sg.eyedoctor.commUtils.addPatient.activity.AddPatientActivity;
import com.sg.eyedoctor.commUtils.caseDiscuss.activity.CaseDiscussActivity;
import com.sg.eyedoctor.common.fragment.BaseFragment;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.impl.DlgEdit;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.ActionSheetDialog;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.common.view.SearchLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 病历夹
 */
@ContentView(R.layout.fragment_chart_file)
public class ChartFileFragment extends BaseFragment implements MedicalRecordExpandAdapter.GroupClick {
    private static final int ACTION_RENAME_GROUP = 0;
    private static final int ACTION_DELETE_GROUP = 1;
    private static final int ACTION_DELETE_PATIENT = 2;
    private static final int ACTION_QUERY_GROUP = 3;

    @ViewInject(R.id.patient_smelv)
    private SwipeMenuExpandableListView mExpandableListView;
    @ViewInject(R.id.add_group_rl)
    private RelativeLayout mAddgroupRl;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.search_sl)
    private SearchLayout mSearchSl;

    private MedicalRecordExpandAdapter mAdapter;
    private ArrayList<Group> mGroups = new ArrayList<>();
    private ArrayList<ArrayList<PatientByGroup>> mPatients = new ArrayList<>();
    private ArrayList<PatientByGroup> mPatientParamses = new ArrayList<>();
    private ArrayList<String> mGroupNames;
    private OptionsPickerView<String> mGroupPv;
    private int mCurrQueryGroupId;
    private int mGroupPosition;
    private int mChildPosition;

    private int mAction = ACTION_QUERY_GROUP;

    private NetCallback mGroupCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<Group>>() {
                }.getType();
                BaseArrayResp<Group> res = new Gson().fromJson(result, objectType);
                mGroups = res.value;

                initGroup();
                switch (mAction) {
                    case ACTION_DELETE_GROUP:
                        showToast("删除分组成功!");
                        break;
                    case ACTION_RENAME_GROUP:
                        showToast("重命名分组成功!");
                        break;
                }
                mAction = ACTION_QUERY_GROUP;
            } else {
                showToast(R.string.query_empty);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    private NetCallback mDelateCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                mPatients.get(mGroupPosition).remove(mChildPosition);
                mAdapter.notifyDataSetChanged();
                showToast("删除患者成功!");
            } else {
                showToast(R.string.net_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mGroupRenameCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                mAction = ACTION_RENAME_GROUP;
                initData();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mGroupDelateCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {

            if (CommonUtils.isResultOK(result)) {
                mAction = ACTION_DELETE_GROUP;
                initData();
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mMoveCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.operation_ok);

                for (int i = 0; i < mGroups.size(); i++) {
                    mExpandableListView.collapseGroup(i);//全部收缩
                }
            } else {
                showToast(R.string.net_fail);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mAddGroupCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                initData();
            } else {

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };
    private NetCallback mItemCallback = new NetCallback(getActivity()) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<PatientByGroup>>() {
                }.getType();
                BaseArrayResp<PatientByGroup> res = new Gson().fromJson(result, objectType);

                mPatientParamses = res.value;
                mPatients.set(mCurrQueryGroupId, mPatientParamses);
                mAdapter.setData(mGroups, mPatients);
                if (mPatientParamses.size() == 0) {
                    showToast(R.string.patient_empty);
                }
            } else {

            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    @Override
    protected void initView() {
        mAdapter = new MedicalRecordExpandAdapter(getActivity(), mGroups, mPatients);
        mAdapter.setGroupClick(this);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setGroupIndicator(null);

        mGroupNames = new ArrayList<>();
        mGroupPv = new OptionsPickerView<>(getActivity());
        mGroupPv.setPicker(mGroupNames);
        mGroupPv.setTitle("请选择移动到分组");
        mGroupPv.setCyclic(false);
        mGroupPv.setCancelable(true);

        initSwipeMenu();
        initData();
    }

    @Override
    protected void initListener() {

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                PatientByGroup patient = mPatients.get(groupPosition).get(childPosition);
                Intent intent = new Intent(getActivity(), PatientCenterActivity.class);
                intent.putExtra(ConstantValues.KEY_PATIENT, patient);
                startActivity(intent);
                return false;
            }
        });
        mExpandableListView
                .setOnExpandableMenuItemClickListener(new SwipeMenuExpandableListView.OnExpandableMenuItemClickListener() {

                    @Override
                    public void onExpandableMenuItemClick(final int groupPosition,
                                                          final int childPosition, SwipeMenu menu, int index) {
                        switch (index) {
                            case 1:
                                DialogManager.showConfimCancelDlg(getActivity(), R.string.delete_confirm, new DlgClick() {
                                    @Override
                                    public boolean click() {
                                        showLoginDlg();
                                        mGroupPosition = groupPosition;
                                        mChildPosition = childPosition;
                                        PatientByGroup deletePatient = mPatients.get(groupPosition).get(childPosition);
                                        BaseManager.deletePatient(deletePatient.id, mDelateCallback);
                                        showToast("删除成功");
                                        return false;
                                    }
                                }, new DlgClick() {
                                    @Override
                                    public boolean click() {

                                        return false;
                                    }
                                });

                                break;
                            case 0:
                                mGroupPosition = groupPosition;
                                mChildPosition = childPosition;

                                mGroupPv.show();
                                break;

                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

        mGroupPv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                PatientByGroup movePatient = mPatients.get(mGroupPosition).get(mChildPosition);

                showLoginDlg();
                String groupId = mGroups.get(options1).id;
                BaseManager.patientGroupMove(movePatient.id, groupId, mMoveCallback);
            }
        });
        mActionbar.setRightTv(R.string.new_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddPatientActivity.class));
            }
        });
        mSearchSl.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchChartFileActivity.class));
            }
        });
    }

    @Event(R.id.add_group_rl)
    private void addGroup(View view) {
        DialogManager.showEditDlg(getActivity(), R.string.enter_group_name, new DlgEdit() {
            @Override
            public boolean edit(String text) {
                if (text == null || text.equals("")) {
                    showToast("分组名不能为空");
                    return true;
                }
                for (String groupName : mGroupNames) {
                    if (text.equals(groupName)) {
                        showToast("分组名重复,请重新输入!");
                        return true;
                    }
                }
                BaseManager.addGroup(text, mAddGroupCallback);
                return false;
            }
        }, InputType.TYPE_CLASS_TEXT);
    }

    @Event(R.id.rl_discuss_case)
    private void discuss(View view) {
        if (!mIsAuth) {
            startAuthActivity(R.string.case_discuss);
            return;
        }
        startActivity(new Intent(getActivity(), CaseDiscussActivity.class));
    }

    @Event(R.id.rl_attention_case)
    private void attention(View view) {
        startActivity(new Intent(getActivity(), AttentionPatientActivity.class));

    }

    @Override
    public void groupLongClick(int position) {
        LogUtils.i("longclick" + position);
        final Group group = mGroups.get(position);

        ActionSheetDialog dialog = new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("修改组名", R.color.text_body_strong,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                DialogManager.showEditDlg(getActivity(), R.string.enter_group_name, new DlgEdit() {
                                    @Override
                                    public boolean edit(String text) {
                                        if (text.equals("")) {
                                            showToast("分组名不能为空!");
                                            return true;
                                        }
                                        showLoginDlg();
                                        BaseManager.groupUpDate(group.id, text, mGroupRenameCallback);
                                        return false;
                                    }
                                }, InputType.TYPE_CLASS_TEXT);

                            }
                        });

        if (!group.id.equals("1")) {

            dialog.addSheetItem("删除分组", R.color.actionsheet_red,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            DialogManager.showConfimCancelDlg(getActivity(), "删除该分组后，组内联系人将移至默认分组，确定删除此分组？", new DlgClick() {
                                @Override
                                public boolean click() {
                                    showLoginDlg();
                                    BaseManager.groupRemove(group.id, mGroupDelateCallback);
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

        dialog.show();

    }

    @Override
    public void groupClick(int groupPosition, boolean isExpanded) {
        LogUtils.i("shortclick" + groupPosition);
        if (isExpanded) {//已经展开
            mExpandableListView.collapseGroup(groupPosition);
        } else {
            mExpandableListView.expandGroup(groupPosition);
        }

        if (mExpandableListView.isGroupExpanded(groupPosition)) {
            String groupId = mGroups.get(groupPosition).id;
            showLoginDlg();
            mCurrQueryGroupId = groupPosition;
            BaseManager.getPatientByGroup(groupId, mItemCallback);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        for (int i = 0; i < mGroups.size(); i++) {
            mExpandableListView.collapseGroup(i);//全部收缩
        }
    }

    private void refreshComplete(
            final PullToRefreshAndSwipeMenuExpandableListView plv) {
        plv.postDelayed(new Runnable() {

            @Override
            public void run() {
                plv.onRefreshComplete();
            }
        }, 1000);
    }

    private void initSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 两种获取屏幕宽度的方式
                int width1 = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                int width2 = getResources().getDisplayMetrics().widthPixels;

                SwipeMenuItem item2 = new SwipeMenuItem(getActivity());

                item2.setBackground(new ColorDrawable(getResources().getColor(R.color.actionsheet_red)));
                item2.setTitle("删除");
                item2.setTitleColor(getResources().getColor(R.color.white));
                item2.setTitleSize(16);
                item2.setWidth(CommonUtils.dp2px(getActivity(), 80));

                SwipeMenuItem item1 = new SwipeMenuItem(getActivity());
                item1.setBackground(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
                item1.setTitle("移动分组");
                item1.setTitleColor(getResources().getColor(R.color.white));
                item1.setTitleSize(16);
                item1.setWidth(CommonUtils.dp2px(getActivity(), 80));

                menu.addMenuItem(item1);
                menu.addMenuItem(item2);
            }
        };
        mExpandableListView.setMenuCreator(creator);
    }

    private void initGroup() {
        ArrayList<PatientByGroup> item1 = new ArrayList<>();

        for (Group group : mGroups) {
            mPatients.add(item1);
        }
        mAdapter.setData(mGroups, mPatients);
        for (int i = 0; i < mGroups.size(); i++) {
            mExpandableListView.collapseGroup(i);//全部收缩
        }
        mGroupNames.clear();
        for (Group group : mGroups) {
            if (group.groupName != null) {
                mGroupNames.add(group.groupName);
            }
        }
    }

    public void initData() {
        showLoginDlg();
        BaseManager.getGroupByDoc(mGroupCallback);
    }
}
