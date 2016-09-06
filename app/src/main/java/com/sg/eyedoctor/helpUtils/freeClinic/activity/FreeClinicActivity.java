package com.sg.eyedoctor.helpUtils.freeClinic.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenu;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuCreator;
import com.pulltorefresh.chuwe1.swipemenu.library.SwipeMenuItem;
import com.pulltorefresh.chuwe1.swipemenu.library.swipemenuview.SwipeMenuListView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.manager.DialogManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.helpUtils.freeClinic.adapter.FreeClinicAdapter;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.GetFreeClinicList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * 义诊发布
 */
@ContentView(R.layout.activity_free_clinic)
public class FreeClinicActivity extends BaseActivity {
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.free_clinic_lv)
    private SwipeMenuListView mFreeLv;
    private String today;

    private FreeClinicAdapter mAdapter;
    private ArrayList<GetFreeClinicList> mFreeClinics=new ArrayList<>();
    private int mOperatePosition;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<GetFreeClinicList>>(){}.getType();
                BaseArrayResp<GetFreeClinicList> res=new Gson().fromJson(result, objectType);

                mFreeClinics=res.value;
                //sortTime(mFreeClinics);
                mAdapter.setData(mFreeClinics);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    //删除义诊
    private NetCallback mDelateCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.delete_success);
                mFreeClinics.remove(mOperatePosition);
                mAdapter.setData(mFreeClinics);
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };
    @Override
    protected void initView() {
        today=CommonUtils.getSpritTime(new Date(System.currentTimeMillis()));
        LogUtils.i("today: "+today);
        mAdapter=new FreeClinicAdapter(this,mFreeClinics,0);
        mFreeLv.setAdapter(mAdapter);
        UiUtils.setEmptyText(mContext, mFreeLv, getString(R.string.empty));

        initSwipeMenu();
    }

    @Override
    protected void initListener() {
        mFreeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetFreeClinicList freeClinicList = mFreeClinics.get(position);

                if (freeClinicList.openDate.split(" ")[0].equals(today)) {

                    DialogManager.showConfimDlg(mContext, R.string.today_can_not_edit, new DlgClick() {
                        @Override
                        public boolean click() {

                            return false;
                        }
                    });
                    return;
                }
                Intent intent = new Intent(mContext, FreeClinicPublishActivity.class);
                intent.putExtra("data", freeClinicList);
                startActivity(intent);
            }
        });

        mFreeLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                final GetFreeClinicList freeClinicList = mFreeClinics.get(position);
                mOperatePosition=position;
                if (freeClinicList.openDate.split(" ")[0].equals(today)) {

                    DialogManager.showConfimDlg(mContext, R.string.today_can_not_delete, new DlgClick() {
                        @Override
                        public boolean click() {
                            return false;
                        }
                    });
                    return;
                }

                DialogManager.showConfimCancelDlg(mContext, "是否删除该义诊消息", new DlgClick() {
                    @Override
                    public boolean click() {
                        showdialog();

                        BaseManager.freeClinicDelete(freeClinicList.id, mDelateCallback);
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

    @Override
    protected void initActionbar() {
      mActionbar.setRightTv(R.string.publishing, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(mContext, FreeClinicPublishActivity.class));
          }
      });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getFreeClinicList(mCallback);
    }

    private void initSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 两种获取屏幕宽度的方式
                int width1 = getWindowManager().getDefaultDisplay().getWidth();
                int width2 = getResources().getDisplayMetrics().widthPixels;

                SwipeMenuItem item2 = new SwipeMenuItem(mContext);

                item2.setBackground(R.color.pink);
                item2.setImageId(R.drawable.ic_delete_normal);
                item2.setTitle(R.string.delete);
                item2.setTitleColor(R.color.white);
                item2.setTitleSize(10);
                item2.setWidth(width2 / 5);

                menu.addMenuItem(item2);
            }
        };
        mFreeLv.setMenuCreator(creator);
    }

//    private ArrayList<GetFreeClinicList> sortTime(ArrayList<GetFreeClinicList> list){
//        Collections.sort(list, new Comparator<GetFreeClinicList>() {
//
//            @Override
//            public int compare(GetFreeClinicList lhs, GetFreeClinicList rhs) {
//                Date date1 = CommonUtils.getSpritDate(lhs.openDate.split(" ")[0]);
//                Date date2 = CommonUtils.getSpritDate(rhs.openDate.split(" ")[0]);
//                // 对日期字段进行升序，如果欲降序可采用after方法
//                if (date1.after(date2)) {
//                    return 1;
//                }
//                return -1;
//            }
//        });
//
//        return list;
//    }
}
