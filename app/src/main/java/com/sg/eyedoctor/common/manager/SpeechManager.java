package com.sg.eyedoctor.common.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.sg.eyedoctor.common.utils.Jsonparser;
import com.sg.eyedoctor.common.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 讯飞语音 dialog
 */
public class SpeechManager extends  BaseManager {

    public static SpeechManager sSpeechManager;

    // 语音听写对象
    SpeechRecognizer mIat;
    // 语音听写UI
    RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    SharedPreferences mSharedPreferences;
    // 引擎类型
    String mEngineType = SpeechConstant.TYPE_CLOUD;
    SpeechCallback mCallback;
    Context mContext;

    /**
     * 初始化监听器。
     */
    InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {

            if (code != ErrorCode.SUCCESS) {
                LogUtils.i("初始化失败，错误码：" + code);

            }
        }
    };

    public static SpeechManager getInstance(Context context){
        if (sSpeechManager==null) {
            sSpeechManager=new SpeechManager(context);
        }
        return sSpeechManager;
    }

    public SpeechManager(Context context) {
        super(context);
        mContext = context;

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        mSharedPreferences = mContext.getSharedPreferences("speech",
                Activity.MODE_PRIVATE);
    }

    public void showSpeechDialog(SpeechCallback callback) {
        mCallback = callback;
        mIatResults.clear();
        // 设置参数
        setParam();

        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写UI监听器
     */
    RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {


        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult results, boolean b) {
            printResult(results,b);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {

        }

    };

    private void printResult(RecognizerResult results,boolean isLast) {
        String text = Jsonparser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mCallback.printResult(resultBuffer.toString(),isLast);
    }

    public interface SpeechCallback {
        void printResult(String result,boolean isLast);
    }

    public void destroy() {
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }


}
