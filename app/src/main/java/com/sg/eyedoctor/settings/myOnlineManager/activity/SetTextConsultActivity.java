package com.sg.eyedoctor.settings.myOnlineManager.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.activity.BaseActivity;
import com.sg.eyedoctor.common.manager.BaseManager;
import com.sg.eyedoctor.common.utils.CommonUtils;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.common.view.DescribeView;
import com.sg.eyedoctor.common.view.MyActionbar;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 图文咨询设置
 */
@ContentView(R.layout.activity_set_text_consult)
public class SetTextConsultActivity extends BaseActivity {
    @ViewInject(R.id.open_dv)
    private DescribeView mOpenDv;
    @ViewInject(R.id.price_et)
    private EditText mPriceTv;
    @ViewInject(R.id.price_ll)
    private LinearLayout mPriceLl;
    @ViewInject(R.id.actionbar)
    private MyActionbar mActionbar;

    private boolean mIsOpen = false;
    private String mPrice;

    private NetCallback mOpenCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {

            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.post_ok);
                LogUtils.i(mDoctor.textIsOpen);
                mDoctor.textPrice=mPriceTv.getText().toString();
                try {
                    x.db().saveOrUpdate(mDoctor);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        protected void timeOut() {
            onTimeOut();
        }
    };

    private NetCallback mCloseCallback = new NetCallback(this) {
        @Override
        protected void requestOK(String result) {
            closeDialog();
            if (CommonUtils.isResultOK(result)) {
                showToast(R.string.colse_ok);
            }
        }

        @Override
        protected void timeOut() {
            onTimeOut();
        }

    };

    @Override
    protected void initView() {
        mIsOpen = mDoctor.textIsOpen.equals("True");
        mPrice = mDoctor.textPrice;
        mOpenDv.setSelected(mIsOpen);
        mPriceTv.setText(mPrice);

        mOpenDv.setOpenImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsOpen = !mIsOpen;
                mOpenDv.setSelected(mIsOpen);
                viewChange();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initActionbar() {
        mActionbar.setRightTv(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = mPriceTv.getText().toString();
                if ((CommonUtils.isNumeric(price) || CommonUtils.isDecimal(price))&&Double.valueOf(price)!=0.0) {
                    showdialog();
                    BaseManager.vasServiceSet(ConstantValues.TYPE_TEXT+"", price, mOpenCallback);

                } else {
                    showToast(R.string.enter_right_price);
                    return;
                }
            }
        });
    }


    private void viewChange() {

        if (mIsOpen) {
            mPriceLl.setVisibility(View.VISIBLE);
            mPriceTv.setText(mPrice);
            mActionbar.setRightTvVisible(View.VISIBLE);
            mDoctor.textIsOpen="True";

        } else {
            mPriceLl.setVisibility(View.GONE);
            mActionbar.setRightTvVisible(View.INVISIBLE);
            mDoctor.textIsOpen="False";
            try {
                x.db().saveOrUpdate(mDoctor);
            } catch (DbException e) {
                e.printStackTrace();
            }
            showdialog();
            BaseManager.vasServiceClose(mDoctor.textId,mCloseCallback);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mIsOpen) {
            mPriceLl.setVisibility(View.VISIBLE);
            mPriceTv.setText(mPrice);
            mActionbar.setRightTvVisible(View.VISIBLE);
        } else {
            mPriceLl.setVisibility(View.GONE);
            mActionbar.setRightTvVisible(View.INVISIBLE);
        }
    }
}
