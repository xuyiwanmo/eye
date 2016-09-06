package com.sg.eyedoctor.common.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.impl.DlgClick;
import com.sg.eyedoctor.common.impl.DlgEdit;
import com.sg.eyedoctor.common.utils.KeyBoardUtils;
import com.sg.eyedoctor.common.view.AddressData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/17.
 */
public class DialogManager extends BaseManager {


    public DialogManager(Context context) {
        super(context);
        mContext = context;
    }

    static ArrayList<String> options1Items = new ArrayList<String>();
    static ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    public static void showCityDlg(Context context, final CityCallback callback) {

        OptionsPickerView pvOptions;
        //选项选择器
        pvOptions = new OptionsPickerView(context);
        //选项1
        for (String province : AddressData.PROVINCES) {
            options1Items.add(province);
        }

        //选项2
        ArrayList<String> options2Items_04;
        for (String[] city : AddressData.CITIES) {
            options2Items_04 = new ArrayList<>();
            for (String s : city) {
                options2Items_04.add(s);
            }
            options2Items.add(options2Items_04);
        }


        //选项3
        ArrayList<ArrayList<String>> options3Items_01;
        ArrayList<String> options3Items_01_01;

        for (String[][] county : AddressData.COUNTIES) {
            options3Items_01 = new ArrayList<ArrayList<String>>();
            for (String[] strings : county) {
                options3Items_01_01 = new ArrayList<String>();
                for (String string : strings) {
                    options3Items_01_01.add(string);
                }
                options3Items_01.add(options3Items_01_01);
            }
            options3Items.add(options3Items_01);
        }

        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
        // pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);

                callback.chooseCity(tx);
            }
        });

        pvOptions.show();

    }


    public interface CityCallback {
        void chooseCity(String cityTxt);
    }


    public static AlertDialog showConfimDlg(Context context, int stringId, final DlgClick dlgClick) {
        return showConfimDlg(context, context.getResources().getString(stringId), dlgClick);
    }

    public static AlertDialog showConfimDlg(Context context, String stringId, final DlgClick dlgClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View dlgView = View.inflate(context, R.layout.dialog_confirm, null);

        dlgView.findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgClick.click();
                dialog.dismiss();
            }
        });

        TextView textView = (TextView) dlgView.findViewById(R.id.content_tv);
        textView.setText(stringId);

        dialog.show();
        dialog.setContentView(dlgView);
        return dialog;
    }

    public static AlertDialog showConfimCancelDlg(Context context, int stringId, final DlgClick confirmImpl, final DlgClick cancelImpl) {
        return showConfimCancelDlg(context, context.getString(stringId), confirmImpl, cancelImpl);
    }

    private static AlertDialog buildDlgView(Context context, String title, String stringId, final DlgClick confirmImpl, final DlgClick cancelImpl, String confirmText, String cancleText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View dlgView = View.inflate(context, R.layout.dialog_confirm_cancel, null);

        TextView textView = (TextView) dlgView.findViewById(R.id.content_tv);
        textView.setText(stringId);
        TextView titleView = (TextView) dlgView.findViewById(R.id.dlg_title_tv);
        if (!title.equals("")) {
            titleView.setText(title);
        }
        textView.setText(stringId);

        TextView confirm_tv = (TextView) dlgView.findViewById(R.id.confirm_tv);
        TextView cancel_tv = (TextView) dlgView.findViewById(R.id.cancel_tv);

        if (!confirmText.equals("")) {
            confirm_tv.setText(confirmText);
        }
        if (!cancleText.equals("")) {
            cancel_tv.setText(cancleText);
        }
        dlgView.findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmImpl.click();
                dialog.dismiss();
            }
        });
        dlgView.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelImpl.click();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setContentView(dlgView);

        return dialog;

    }

    public static AlertDialog showConfimCancelDlg(Context context, String stringId, final DlgClick confirmImpl, final DlgClick cancelImpl) {
        return buildDlgView(context, "",stringId, confirmImpl, cancelImpl, "", "");
    }

    public static AlertDialog showConfimCancelDlg(Context context, String title, String content, final DlgClick confirmImpl, final DlgClick cancelImpl, String confirmText, String cancleText) {
        return buildDlgView(context, title, content, confirmImpl, cancelImpl, confirmText, cancleText);
    }

    public static void showEditDlg(final Activity context, int titleId, final DlgEdit confirmImpl, int inputType) {

        LayoutInflater factory = LayoutInflater.from(context);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的

        final EditText edit = (EditText) view.findViewById(R.id.content_et);//获得输入框对象
        edit.setInputType(inputType);
        TextView title = (TextView) view.findViewById(R.id.actionbar_title_tv);
        title.setText(titleId);
        final Dialog dialog =
                //提示框标题 R.string.enter_group_name
                new AlertDialog.Builder(context)
                        .setView(view)
                        .create();
        view.findViewById(R.id.confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean show = confirmImpl.edit(edit.getText().toString());
                if (!show) {
                    dialog.dismiss();
                    KeyBoardUtils.hideKeyboard(edit);
                }
            }
        });
        view.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                KeyBoardUtils.hideKeyboard(edit);
            }
        });
        dialog.show();

        KeyBoardUtils.showKeyboard(edit);
    }


}
