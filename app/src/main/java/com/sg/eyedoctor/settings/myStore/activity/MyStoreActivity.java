package com.sg.eyedoctor.settings.myStore.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.commUtils.academic.activity.AcademicWebActivity;
import com.sg.eyedoctor.commUtils.academic.bean.Academic;
import com.sg.eyedoctor.commUtils.internetConsult.activity.InternetConsultDetailActivity;
import com.sg.eyedoctor.commUtils.internetConsult.bean.InternetConsult;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.response.BaseArrayResp;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.utils.UiUtils;
import com.sg.eyedoctor.common.view.MyActionbar;
import com.sg.eyedoctor.eyeCircle.activity.CaseDetailActivity;
import com.sg.eyedoctor.eyeCircle.bean.TopicCase;
import com.sg.eyedoctor.settings.myStore.adapter.StoreAdapter;
import com.sg.eyedoctor.settings.myStore.bean.StoreItem;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 设置-我的收藏
 */
@ContentView(R.layout.activity_my_store)
public class MyStoreActivity extends BaseActivity {

    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;
    @ViewInject(R.id.store_lv)
    private ListView mStoreLv;

    private ArrayList<StoreItem> mStoreItems = new ArrayList<>();

    private StoreAdapter mAdapter;

    private NetCallback mCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                Type objectType = new TypeToken<BaseArrayResp<StoreItem>>() {
                }.getType();
                BaseArrayResp<StoreItem> res = new Gson().fromJson(result, objectType);

                mStoreItems = res.value;
                Collections.sort(mStoreItems,new StoreSort());
                mAdapter.setData(mStoreItems);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {
        mAdapter = new StoreAdapter(mContext, mStoreItems, 0);
        mStoreLv.setAdapter(mAdapter);
        UiUtils.setEmptyText(mContext, mStoreLv, getString(R.string.empty));
    }

    @Override
    protected void initListener() {
        mStoreLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreItem item = mStoreItems.get(position);

                if (item.sourceType == ConstantValues.STORE_ACADEMIC) {//学术前沿
                    Academic academic = new Academic();
                    academic.newsId = item.sourceID;
                    academic.newsTitle = item.title;
                    academic.url = item.webUrl;
                    Intent intent = new Intent(mContext, AcademicWebActivity.class);
                    intent.putExtra(ConstantValues.KEY_URL, academic);
                    startActivity(intent);
                } else if(item.sourceType == ConstantValues.STORE_INTERNET){
                    InternetConsult consult = new InternetConsult();
                    consult.id = item.sourceID;
                    Intent intent = new Intent(mContext, InternetConsultDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, consult);
                    startActivity(intent);

                }else if(item.sourceType==ConstantValues.STORE_CIRCLE){
                    TopicCase consult = new TopicCase();
                    consult.topicId = item.sourceID;
                    Intent intent = new Intent(mContext, CaseDetailActivity.class);
                    intent.putExtra(ConstantValues.KEY_DATA, consult);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initActionbar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showdialog();
        BaseManager.getCollectionList(mCallback);
    }

    class StoreSort implements Comparator {

        @Override
        public int compare(Object time1, Object time2) {
            long flag =((StoreItem) time1).createDate.compareTo(((StoreItem) time2).createDate);
            if (flag > 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
