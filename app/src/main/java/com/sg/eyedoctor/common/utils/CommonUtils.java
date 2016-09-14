package com.sg.eyedoctor.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.response.BaseArrayResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author dewyze
 */
public class CommonUtils {

    private static final String TAG = "CommonUtils";

    /**
     * 开启activity
     */
    public static void launchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    public static void launchActivityForResult(Activity context,
                                               Class<?> activity, int requestCode) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeybord(Activity activity) {

        if (null == activity) {
            return;
        }
        try {
            final View v = activity.getWindow().peekDecorView();
            if (v != null && v.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 判断是否为合法的json
     *
     * @param jsonContent
     *            需判断的字串
     */
//	public static boolean isJsonFormat(String jsonContent) {
//		try {
//			new Jsonparser().parse(jsonContent);
//			return true;
//		} catch (JsonParseException e) {
//			LogUtils.i(TAG, "bad json: " + jsonContent);
//			return false;
//		}
//	}

    /**
     * 判断返回结果是否成功
     * code=10000  成功
     */
    public static boolean isResultOK(String jsonContent) {
        try {
            JSONObject object = new JSONObject(jsonContent.toString());
            if (object.getInt("code") == 10000) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断返回value是否为0
     */
    public static boolean isValueZero(String jsonContent) {
        try {
            JSONObject object = new JSONObject(jsonContent.toString());
            if (object.getInt("value") == 0) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断返回value是否为字符串
     */
    public static String getValueString(String jsonContent) {
        String msg = null;
        try {
            JSONObject object = new JSONObject(jsonContent.toString());
            if (object.getString("value").startsWith("[")) {
                return null;
            } else {
                return object.getString("value");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 错误的时候返回错误信息
     */
    public static String getMsg(String jsonContent) {
        try {
            JSONObject object = new JSONObject(jsonContent.toString());
            return object.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean isNull(String text) {
        if (text == null || "".equals(text.trim()) || "null".equals(text))
            return true;
        return false;
    }


    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 显示进度
     *
     * @param context
     * @param title
     * @param message
     * @param indeterminate
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgress(Context context,
                                              CharSequence title, CharSequence message, boolean indeterminate,
                                              boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setDefaultButton(false);
        dialog.show();
        return dialog;
    }

    public static String softVersion(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isMobile(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static String formatDay(Date date) {
        String str = "";
        switch (date.getDay()) {
            case 0:
                str = "日";
                break;
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            case 6:
                str = "六";
                break;
        }
        return str;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("M-d");
        return format.format(date);
    }

    public static String changeDateFormat(String string) {
        //1986-06-06
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static int getAge(String birthDay) {
        if (birthDay == null) {
            return 0;
        }
        //1986-06-06
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        try {
            date = format.parse(birthDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static int dateToAge(String birthDay) {
        if (birthDay == null) {
            return 0;
        }
        //1986-06-06
        SimpleDateFormat format;
        Date date = new Date();

        if (birthDay.contains("/")) {
            format = new SimpleDateFormat("yyyy/M/d H:mm:ss");
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        try {
            date = format.parse(birthDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

//        if (monthNow <= monthBirth) {
//            if (monthNow == monthBirth) {
//                if (dayOfMonthNow < dayOfMonthBirth)
//                    age--;
//            } else {
//                age--;
//            }
//        }
        if (age < 0) {
            age = 0;
        }
        return age;
    }


    //身份证号码验证：start

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static boolean isIdcard(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位";
            LogUtils.i(errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            LogUtils.i(errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            LogUtils.i(errorInfo);
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                LogUtils.i(errorInfo);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            LogUtils.i(errorInfo);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            LogUtils.i(errorInfo);
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            LogUtils.i(errorInfo);
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                LogUtils.i(errorInfo);
                return false;
            }
        } else {
            return true;
        }
        // =====================(end)=====================
        return true;
    }

    /**
     * 功能：判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为数字
     */
    public static boolean isAge(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }

        if (isNumeric(str) && Integer.parseInt(str) <= 130 && Integer.parseInt(str) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是数字  小数
     */
    public static boolean isDecimal(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    //身份证号码验证：end


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    public static int getScreenSizeWidth(Activity con) {
        DisplayMetrics metric = new DisplayMetrics();
        con.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    public static int getScreenSizeHeight(Activity con) {
        DisplayMetrics metric = new DisplayMetrics();
        con.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int heightPixels = metric.heightPixels;     // 屏幕宽度（像素）
        return heightPixels;
    }
    // 获取ApiKey

    /**
     * @param img
     * @return 返回宽是屏幕宽度，高按照比例的imagView
     */
    public static ImageView getImagViewSize(Activity context, ImageView img, float f) {
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = getScreenSizeWidth(context);
        layoutParams.height = (int) (getScreenSizeWidth(context) * f);
        img.setLayoutParams(layoutParams);
        return img;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.isAvailable() && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void errorNetMes(Context context) {
        if (isNetworkConnected(context)) {
            Toast.makeText(context, "服务器链接错误", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "网络链接错误", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取状态栏高度
     *
     * @param v
     * @return
     */
    public static int getStatusBarHeight(View v) {
        if (v == null) {
            return 0;
        }
        Rect frame = new Rect();
        v.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static String formatUrl(String url) {
        String tempUrl = ConstantValues.IMG_HOST + url;
        String url1 = tempUrl.replaceAll("\\\\", "/");
        Log.i("image", "imageload: " + url1);
        return url1;
    }

    public static void loadImg(String url, ImageView imageView) {
        DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_loading)
                .showImageOnFail(R.drawable.ic_loading)
                .showImageOnLoading(R.drawable.ic_loading)
                .cacheInMemory(true)// 在内存中会缓存该图片
                .cacheOnDisk(true)// 在硬盘中会缓存该图片
                .considerExifParams(true)// 会识别图片的方向信息
                .resetViewBeforeLoading(true)// 重设图片
                .build();
        ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(url), imageView, mConfig);
    }
    public static void loadImgformat(String url, ImageView imageView) {
        DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_loading)
                .showImageOnFail(R.drawable.ic_loading)
                .showImageOnLoading(R.drawable.ic_loading)
                .cacheInMemory(true)// 在内存中会缓存该图片
                .cacheOnDisk(true)// 在硬盘中会缓存该图片
                .considerExifParams(true)// 会识别图片的方向信息
                .resetViewBeforeLoading(true)// 重设图片
                .build();
        String url1 = url.replaceAll("\\\\", "/");
        ImageLoader.getInstance().displayImage(url1, imageView);
    }

    public static void loadImg(String url, ImageView imageView, int defImgId) {
        DisplayImageOptions mConfig = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(defImgId)
                .showImageOnFail(defImgId)
                .showImageOnLoading(defImgId)
                .cacheInMemory(true)// 在内存中会缓存该图片
                .cacheOnDisk(true)// 在硬盘中会缓存该图片
                .considerExifParams(true)// 会识别图片的方向信息
                .resetViewBeforeLoading(true)// 重设图片
                .build();
        ImageLoader.getInstance().displayImage(CommonUtils.formatUrl(url), imageView, mConfig);
    }

    public <T> BaseArrayResp<T> fromJson(String str, Class<T> type) {

        Type objectType = new TypeToken<BaseArrayResp<T>>() {
        }.getType();
        BaseArrayResp<T> res = new Gson().fromJson(str, objectType);

        return res;
    }

    public static boolean isEmpty(TextView textView) {
        if (textView == null) {
            return true;
        }

        String str = textView.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    public static String getChineseMonth(int firstMonth) {
        String str = "";
        switch (firstMonth) {
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            case 6:
                str = "六";
                break;
            case 7:
                str = "七";
                break;
            case 8:
                str = "八";
                break;
            case 9:
                str = "九";
                break;
            case 10:
                str = "十";
                break;
            case 11:
                str = "十一";
                break;
            case 12:
                str = "十二";
                break;

        }
        return str + "月";
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String getDetailTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //获取斜杠时间
    public static String getSpritTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
        return format.format(date);
    }

    public static Date getSpritDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
        Date date = null;

        try {
            date = format.parse(str);
            date.setMinutes(0);
            date.setHours(0);
            date.setSeconds(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    public static long strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;

        try {
            date = format.parse(str);
            date.setYear(2012);
            date.setMonth(10);
            date.setDate(10);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    public static String longToHM(long time) {

        Date date = new Date(time);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String s = format.format(date);

        return s;
    }

    public static int getMonth(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getMonth() + 1;

    }

    public static long getLongTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();

    }

    public static Date strToTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }


    public static String formatDate(String time) {
        if (time.contains("-")) {
            return time;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;

        try {
            date = format.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        return format2.format(date);

    }

    public static String parseDate(String createTime) {
        createTime = formatDate(createTime);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //今天
        String today = sdf.format(calendar.getTime());

        //昨天
        calendar.add(Calendar.DATE, -1);
        String yestoday = sdf.format(calendar.getTime());

        //前天
        calendar.add(Calendar.DATE, -1);
        String dayBeforeYesterday = sdf.format(calendar.getTime());

        if (createTime.equals(today)) {
            return "今天";
        }

        if (createTime.equals(yestoday)) {
            return "昨天";
        }
        if (createTime.equals(dayBeforeYesterday)) {
            return "前天";
        }

        return createTime;
    }

    /**
     * 检测某ActivityUpdate是否在当前Task的栈顶
     */
    public static boolean isTopActivy(Context context, String cmdName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;

        if (null != runningTaskInfos) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.getClassName();
            LogUtils.i("cmpname", "cmpname:" + cmpNameTemp);
        }

        if (null == cmpNameTemp) return false;
        return cmpNameTemp.equals(cmdName);
    }

    public static boolean isLogin() {
        StatusCode status = NIMClient.getStatus();
        if (status == StatusCode.LOGINED) {
            return true;
        }
        return false;
    }

    public static String getDecimal(String str) {
        double d = Double.valueOf(str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
}
