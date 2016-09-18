package com.sg.eyedoctor.common.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.chartFile.request.AddGroupParams;
import com.sg.eyedoctor.chartFile.request.DeptDoctorParams;
import com.sg.eyedoctor.chartFile.request.MassNoticeParams;
import com.sg.eyedoctor.chartFile.request.MedicialDetailParams;
import com.sg.eyedoctor.chartFile.request.NewMedicalRecordAddParams;
import com.sg.eyedoctor.chartFile.request.PatientByGroupParams;
import com.sg.eyedoctor.chartFile.request.PatientGroupMoveParams;
import com.sg.eyedoctor.chartFile.request.PatientParams;
import com.sg.eyedoctor.commUtils.academic.request.NewsParams;
import com.sg.eyedoctor.commUtils.caseDiscuss.bean.MemberBean;
import com.sg.eyedoctor.commUtils.caseDiscuss.request.AddDiscussMemberParams;
import com.sg.eyedoctor.commUtils.caseDiscuss.request.DiscussAddParams;
import com.sg.eyedoctor.commUtils.internetConsult.request.InternetCommentAddParams;
import com.sg.eyedoctor.commUtils.internetConsult.request.NetConsultationAddParams;
import com.sg.eyedoctor.commUtils.outPatientRecord.request.AppointPatientParams;
import com.sg.eyedoctor.commUtils.outPatientRecord.request.DeletePatientParams;
import com.sg.eyedoctor.commUtils.plusManager.request.XtrAppointmentAddParams;
import com.sg.eyedoctor.commUtils.plusManager.request.XtrAppointmentListParams;
import com.sg.eyedoctor.commUtils.plusManager.request.XtrAppointmentUpdateParams;
import com.sg.eyedoctor.common.bean.Doctor;
import com.sg.eyedoctor.common.bean.PicBean;
import com.sg.eyedoctor.common.request.SendMessageParams;
import com.sg.eyedoctor.common.utils.HttpClient;
import com.sg.eyedoctor.common.utils.LogUtils;
import com.sg.eyedoctor.common.utils.NetCallback;
import com.sg.eyedoctor.consult.textConsult.request.ShareCheckParams;
import com.sg.eyedoctor.consult.textConsult.request.ShareDrugParams;
import com.sg.eyedoctor.consult.textConsult.request.VasDutyRosterSetParams;
import com.sg.eyedoctor.consult.textConsult.request.VasPriceSetParams;
import com.sg.eyedoctor.consult.videoConsult.request.ServiceRecordAddParams;
import com.sg.eyedoctor.contact.bean.FriendList;
import com.sg.eyedoctor.contact.request.AddFriendParams;
import com.sg.eyedoctor.contact.request.FriendValidateParams;
import com.sg.eyedoctor.eyeCircle.request.CollectionAddParams;
import com.sg.eyedoctor.eyeCircle.request.CommentAddParams;
import com.sg.eyedoctor.eyeCircle.request.TopicReleaseParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.AddAdviceParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.DiagnosisAdviceAddParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.GetAdviceCheckParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.GetAdviceDrugParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.GetAdviceParams;
import com.sg.eyedoctor.helpUtils.doctorAdvice.request.GetAdvicePatientParams;
import com.sg.eyedoctor.helpUtils.electronicRecords.request.CaseDownloadParams;
import com.sg.eyedoctor.helpUtils.electronicRecords.request.NowPubliceEventParams;
import com.sg.eyedoctor.helpUtils.electronicRecords.request.PubliceContentParams;
import com.sg.eyedoctor.helpUtils.electronicRecords.request.PubliceListParams;
import com.sg.eyedoctor.helpUtils.freeClinic.bean.PostClinicList;
import com.sg.eyedoctor.helpUtils.freeClinic.request.FreeClinicAddParams;
import com.sg.eyedoctor.helpUtils.massNotice.bean.MassReceiver;
import com.sg.eyedoctor.helpUtils.stopDiagnoseNotice.request.StopNoticeParams;
import com.sg.eyedoctor.loginRegister.request.GetAuthParams;
import com.sg.eyedoctor.loginRegister.request.LoginParams;
import com.sg.eyedoctor.loginRegister.request.RegisterParams;
import com.sg.eyedoctor.loginRegister.request.ResetPasswordParams;
import com.sg.eyedoctor.settings.myAccount.request.PayAccountAddParams;
import com.sg.eyedoctor.settings.myOnlineManager.bean.PostVideoTime;
import com.sg.eyedoctor.settings.myOnlineManager.request.VasServiceSetParams;
import com.sg.eyedoctor.settings.myOnlineManager.request.VideoPostParams;
import com.sg.eyedoctor.settings.myStateMessage.request.StateMessageParams;
import com.sg.eyedoctor.settings.personalInfo.activity.DoctorInfoActivity;
import com.sg.eyedoctor.settings.personalInfo.request.DoctorApproveParams;
import com.sg.eyedoctor.settings.personalInfo.request.DoctorUpdateParams;

import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/25.
 */
public class BaseManager {
    public static final String VERIFYCODE = "VerifyCode";
    protected Context mContext;

    public BaseManager(Context context) {
        mContext = context;
    }

    public static Doctor getDoctor() {
        Doctor doctor = null;
        try {
            doctor = x.db().selector(Doctor.class).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return doctor;
    }


    protected static String getVerifyCode() {
        Doctor doctor = getDoctor();
        return doctor.verifyCode;
    }

    protected static String toUtf(String str) {

        String strUTF8 = "";
        try {
            strUTF8 = java.net.URLEncoder.encode(str);
            //    strUTF8 = URLDecoder.decode(str, "UTF-8");
            System.out.println(strUTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strUTF8;
    }

    //医生新增加号设置
    public static void xtrAppointmentAdd(int orderby, String beginSpan, String endSpan, String xtrAccount, String xtrHospital, String hosId, String xtrType, String xtrPrice, String remark, NetCallback netCallback) {
        XtrAppointmentAddParams params = new XtrAppointmentAddParams(getDoctor().id, orderby, beginSpan, endSpan, xtrAccount, xtrHospital, hosId, xtrType, xtrPrice, remark);
        LogUtils.i("xtrType " + xtrType);
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, netCallback);
    }

    //医生修改加号设置
    public static void xtrAppointmentUpdate(String id, String beginSpan, String endSpan, String xtrAccount, String xtrHospital, String hosId, String xtrType, String xtrPrice, String remark, NetCallback callback) {
        XtrAppointmentUpdateParams params = new XtrAppointmentUpdateParams(id, beginSpan, endSpan, xtrAccount, xtrHospital, hosId, xtrType, xtrPrice, remark);
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //医生删除加号设置
    public static void xtrAppointmentRemove(String id, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/XtrAppointmentRemove");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("id", id);
        HttpClient.get(params, callback);

    }

    //医生获取加号设置列表
    public static void getXtrAppointment(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetXtrAppointment");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);

    }

    //医生医生修改加号预约信息(已就诊| 延时就诊)
    public static void xtrAppointmentListUpdate(String id, int visState, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/XtrAppointmentListUpdate");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("id", id);
        params.addBodyParameter("visState", visState + "");
        HttpClient.get(params, callback);

    }

    //医生获取患者加号预约信息列表(取最近7天数据)
    public static void getXtrAppointmentList(String page, String rows, NetCallback callback) {
        XtrAppointmentListParams params = new XtrAppointmentListParams(getDoctor().id, page, rows);
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //患者申请好友验证
    public static void pdFriendAdd(String doctorId, String patientId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PDFriendAdd");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", doctorId);
        params.addBodyParameter("patientId", patientId);
        HttpClient.get(params, callback);
    }

    //医生通过好友申请
    public static void pdFriendUpdate(String patientId, String validateId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PDFriendUpdate");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("patientId", patientId);
        params.addBodyParameter("validateId", validateId);
        HttpClient.get(params, callback);
    }

    //医生获取患者加号预约信息列表(取最近7天数据)
    public static void pdFriendRemove(String doctorId, String patientId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PDFriendRemove");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", doctorId);
        params.addBodyParameter("patientId", patientId);
        HttpClient.get(params, callback);
    }

    //获取验证记录列表
    public static void getPDValidateList(String visState, String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetPDValidateList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("visState", visState);
        params.addBodyParameter("patientName", patientName);
        HttpClient.get(params, callback);
    }

    //医生获取患者好友列表
    public static void getPDFriendList(String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetPDFriendList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("patientName", patientName);
        HttpClient.get(params, callback);
    }

    public static void getToolsEnglish(NetCallback callback) {
        RequestParams params = new RequestParams("http://123.57.220.217:8080/tools/english");
        HttpClient.post(params, callback);
    }


    //获取眼科检查列表-1
    public static void getEyeCheckList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetEyeCheckList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //获取眼科常用英文-2
    public static void getEyeWordList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetEyeWordList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //获取眼科常用值-3
    public static void getEyeNVAList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetEyeNVAList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //获取眼科常用药品-4-1
    public static void getEyeDrugList(String channelId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetEyeDrugList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("channelId", channelId);
        HttpClient.get(params, callback);
    }

    //获取眼科常用药品分类-4
    public static void getEyeDrugChannel(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetEyeDrugChannel");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //获取人工晶体-5
    public static void getCrystalParamList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetCrystalParamList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //获取学术前沿列表-1
    public static void getEyeFrontiersList(String page, String rows, String type, NetCallback callback) {
        NewsParams params=new NewsParams(page,rows,"1",type,"","");
        HttpClient.post(params, callback);
    }

    //讨论中的病历
    public static void discussionListFind(String page, String rows, String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/DiscussionListFind");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("page", page);
        params.addBodyParameter("rows", rows);
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("patientName", patientName);
        HttpClient.get(params, callback);
    }

    //发布病例讨论
    public static void discussionAdd(ArrayList<PicBean> picList, String patientName, String sex, String age, String illness, String diagnosisResult,
                                     ArrayList<MemberBean> memberIdList, String teamId, NetCallback callback) {
        DiscussAddParams params = new DiscussAddParams();

        params.addHeader(VERIFYCODE, getVerifyCode());
        params.patientName = patientName;
        params.sex = sex;
        params.age = age;
        params.illness = illness;
        params.teamId = teamId;
        params.diagnosisResult = diagnosisResult;
        params.memberList = new Gson().toJson(memberIdList);
        params.picList = new Gson().toJson(picList);
        params.doctorId = getDoctor().id;

        HttpClient.post(params, callback);
    }


    public static void discussionMemberDelete(String disId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/DiscussionMemberDelete");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("disId", disId);
        params.addBodyParameter("memberId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    public static void discussionMemberAdd(String disId, ArrayList<FriendList> friendLists, NetCallback callback) {
        AddDiscussMemberParams params = new AddDiscussMemberParams();
        params.disId = disId;
        String ids = "";
        for (int i = 0; i < friendLists.size(); i++) {
            ids = ids + friendLists.get(i).friendId;
            if (i != friendLists.size() - 1) {
                ids = ids + ",";
            }
        }
        params.memberId = ids;
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //新增互联网会诊
    public static void netConsultationAdd(ArrayList<PicBean> picList, String title, String detail, String patientName, String sex, String age, String illness, String illDetail, NetCallback callback) {

        NetConsultationAddParams params = new NetConsultationAddParams();
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.patientName = patientName;
        params.title = title;
        params.sex = sex;
        params.age = age;
        params.illness = illness;
        params.illDetail = illDetail;
        params.detail = detail;
        params.doctorId = getDoctor().id;
        params.picList = new Gson().toJson(picList);

        HttpClient.post(params, callback);
    }


    public static void netConsultationListFind(int page, int rows, String doctorId, String title, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/NetConsultationListFind");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("rows", rows + "");

        params.addBodyParameter("doctorId", doctorId);
        params.addBodyParameter("title", title);
        HttpClient.get(params, callback);
    }

    public static void netConsultationJoinListFind(int page, int rows, String doctorId, String title, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/NetConsultationJoinListFind");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("rows", rows + "");

        params.addBodyParameter("doctorId", doctorId);
        params.addBodyParameter("title", title);
        HttpClient.get(params, callback);
    }

    /**
     * 查看话题详情
     */
    public static void netConsultationDetailFind(String topicId, NetCallback callback) {

        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/NetConsultationDetailFind");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("id", topicId);
        params.addBodyParameter("doctorId", getDoctor().id);

        HttpClient.get(params, callback);

    }

    /**
     * 添加评论接口
     */
    public static void internetCommentAdd(String consultId, String commentType, String comment, String reviewerId, NetCallback callback) {

        InternetCommentAddParams params = new InternetCommentAddParams(consultId, getDoctor().id, commentType, comment, reviewerId);
        LogUtils.i(params.toString());
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 发布停诊通知
     */
    public static void stopNotice(String stopReason, String startDate, String endDate, String startSpan, String endSpan, String stopTypeList, NetCallback callback) {
        StopNoticeParams params = new StopNoticeParams(getDoctor().id, getDoctor().userName, stopReason, startDate, endDate, startSpan, endSpan, stopTypeList);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);

    }

    /**
     * 获取站内信息
     */
    public static void getStateMessageByDoc(NetCallback callback) {
        StateMessageParams params = new StateMessageParams(getDoctor().id);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.get(params, callback);
    }

    /**
     * 医生发布群发通知
     */
    public static void massNotice(String messageTitle, String messageDetail, ArrayList<MassReceiver> receiverList, NetCallback callback) {
        String string = new Gson().toJson(receiverList);
        MassNoticeParams params = new MassNoticeParams(messageTitle, messageDetail, getDoctor().id, string, getDoctor().userName);
        LogUtils.i("MassNoticeParams  " + string);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 收藏文章
     */
    public static void collectionAdd(String sourceID, String collector, String sourceType, String title, String picUrl, String webUrl, NetCallback callback) {
        CollectionAddParams params = new CollectionAddParams(sourceID, collector, sourceType, title, picUrl, webUrl);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 取消收藏文章
     */
    public static void collectionRemove(String id, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/CollectionRemove");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("id", id);
        HttpClient.get(params, callback);
    }

    /**
     * 获取收藏列表
     */
    public static void getCollectionList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetCollectionList");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("userId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    //我的钱包

    /**
     * 我的钱包
     */
    public static void getWalletSum(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetWalletSum");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 订单列表
     * orderType 订单类型(1：图文咨询|2：电话咨询|3：视频咨询|4：加号预约)若为空则查询全部
     */
    public static void getOrderList(String orderType, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetOrderList");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("orderType", orderType);
        HttpClient.get(params, callback);
    }

    //图文咨询

    /**
     * 服务价格设置
     */
    public static void vasPriceSet(String doctorId, String price, String type, NetCallback callback) {
        VasPriceSetParams params = new VasPriceSetParams(doctorId, price, type);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 获取服务价格
     */
    public static void getVasPriceByType(String type, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetVasPriceByType");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("type", type);
        HttpClient.get(params, callback);
    }

    /**
     * 提交图文咨询购买订单（无图片）
     * 提交图文咨询购买订单（有图片）
     */
    public static void textOrderSubmitNoPic(String patientId, String orderAmount, String description, String payType, String orderType, ArrayList<String> filePath, NetCallback callback) {

        RequestParams params;

        if (filePath.size() != 0) {
            params = new RequestParams(ConstantValues.HOST + "/Share/TextOrderSubmit");
            params.setMultipart(true);
            int i = 0;
            for (String s : filePath) {
                params.addBodyParameter("uploadFile" + i, new File(s));
                i++;
            }
        } else {
            params = new RequestParams(ConstantValues.HOST + "/Share/TextOrderSubmitNoPic");
        }

        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addHeader("doctorId", toUtf(getDoctor().id));
        params.addHeader("patientId", toUtf(patientId));
        params.addHeader("orderAmount", toUtf(orderAmount));
        params.addHeader("description", toUtf(description));
        params.addHeader("payType", toUtf(payType));
        params.addHeader("orderType", toUtf(orderType));

        HttpClient.post(params, callback);
    }

    /**
     * 取消支付订单
     */
    public static void textOrderCancel(String orderId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/TextOrderCancel");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        HttpClient.get(params, callback);
    }

    /**
     * 患者完成支付
     */
    public static void textOrderCompleted(String orderId, String payAccount, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/TextOrderCompleted");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("payAccount", payAccount);
        HttpClient.get(params, callback);
    }

    /**
     * 修改支付状态
     */
    public static void textOrdeTypeUpdate(String orderId, String state, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/TextOrdeTypeUpdate");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("state", state);
        HttpClient.get(params, callback);
    }

    /**
     * 获取药品信息
     */
    public static void getShareDrug(String page, String rows, String fuzzyName, NetCallback callback) {
        ShareDrugParams params = new ShareDrugParams(page, rows, fuzzyName);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 获取诊疗信息
     */
    public static void getShareCheck(String page, String rows, String fuzzyName, NetCallback callback) {
        ShareCheckParams params = new ShareCheckParams(page, rows, fuzzyName);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 患者详情(图文咨询)
     */
    public static void getVasConsultation(String id, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetVasConsultation");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("patientId", id);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);

    }

    /**
     * 发表诊断建议
     */
    public static void diagnosisAdviceAdd(String orderId, String doctorId, String patientId, String result, String advice, String drug, String check, NetCallback callback) {
//        String drug = new Gson().toJson(drugList);
//        String check = new Gson().toJson(checkList);
        DiagnosisAdviceAddParams params = new DiagnosisAdviceAddParams(orderId, doctorId, patientId, result, advice, drug, check);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 获取诊断建议
     */
    public static void getDiagnosisAdvice(String orderId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetDiagnosisAdvice");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        HttpClient.get(params, callback);
    }


    public static void vasDutyRosterSet(int day, ArrayList<String> times, NetCallback callback) {
        VasDutyRosterSetParams params = new VasDutyRosterSetParams(getDoctor().id, day + "", new Gson().toJson(times));
        LogUtils.i(new Gson().toJson(times));
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);

    }

    /**
     * 医生获取排班列表-
     */
    public static void getVasDutyRoster(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetVasDutyRoster");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 拨打电话
     */
    public static void callPhone(String accId, String orderId, String caller, String callee, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/ExtendApi/CallPhone");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("accId", accId);
        //    params.addBodyParameter("caller", "18859230844");
        params.addBodyParameter("caller", caller);
//        params.addBodyParameter("callee", "15959376806");
//        params.addBodyParameter("caller", "13666011972");
        params.addBodyParameter("callee", callee);
        HttpClient.get(params, callback);
    }

    public static void getAdvicePatient(String page, String rows, NetCallback callback) {

        GetAdvicePatientParams params = new GetAdvicePatientParams();
        params.addHeader("verifyCode", getVerifyCode());
        params.page = page;
        params.rows = rows;
        HttpClient.post(params, callback);
    }

    public static void getAdviceCheck(String page, String rows, String fuzzyName, NetCallback callback) {

        GetAdviceCheckParams params = new GetAdviceCheckParams();
        params.addHeader("verifyCode", getVerifyCode());
        params.page = page;
        params.rows = rows;
        params.fuzzyName = fuzzyName;
        HttpClient.post(params, callback);
    }

    public static void getAdviceDrug(String page, String rows, String fuzzyName, NetCallback callback) {

        GetAdviceDrugParams params = new GetAdviceDrugParams();
        params.addHeader("verifyCode", getVerifyCode());
        params.page = page;
        params.rows = rows;
        params.fuzzyName = fuzzyName;
        HttpClient.post(params, callback);
    }

    public static void getAdvice(String page, String rows, String zyh, NetCallback callback) {
        GetAdviceParams params = new GetAdviceParams();

        params.page = page;
        params.rows = rows;
        params.hisHospitalized = zyh;
        //   params.hisHospitalized="53216";
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    public static void serviceRecordAdd(String count, String isFree, NetCallback callback) {
        ServiceRecordAddParams params = new ServiceRecordAddParams(getDoctor().id, count, isFree);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 医生获取关注的患者列表
     */
    public static void getServiceRemind(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetServiceRemind");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 获取医生开诊记录(患者判断医生是否开启问诊)
     */
    public static void getServiceRecord(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetServiceRecord");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    //加号搜索
    public static void getServiceXtrRecordList(String xtrId, String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetServiceXtrRecordList");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("xtrId", xtrId);
        params.addBodyParameter("patientName", patientName);

        HttpClient.get(params, callback);
    }

    public static void serviceXtrRecordModify(String xtrId, int type, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/ServiceXtrRecordModify");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("id", xtrId);
        params.addBodyParameter("state", type + "");
        HttpClient.get(params, callback);
    }

    /**
     * 医生关闭在线服务
     */
    public static void vasServiceClose(String xtrId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/VasServiceClose");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("Id", xtrId);
        HttpClient.get(params, callback);
    }

    /**
     * 医生开通在线服务
     */
    public static void vasServiceSet(String type, String price, NetCallback callback) {
        VasServiceSetParams params = new VasServiceSetParams(getDoctor().id, type, price);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 医嘱写入
     */
    public static void addAdvice(String hisHospitalized, String type, String id, String amount, String yzqx, String frequency, String giveDrugWay, String eyes, NetCallback callback) {
        AddAdviceParams params = new AddAdviceParams();
        params.hisHospitalized = hisHospitalized;
        params.type = type;
        params.id = id;
        params.amount = amount;
        params.yzqx = yzqx;
        params.frequency = frequency;
        params.giveDrugWay = giveDrugWay;
        params.eyes = eyes;
        params.addHeader("verifyCode", getVerifyCode());
        LogUtils.i(params.toString());
        HttpClient.post(params, callback);
    }

    /**
     * 获取义诊列表
     */
    public static void getFreeClinicList(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetFreeClinicList");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 义诊
     */
    public static void freeClinicAdd(ArrayList<PostClinicList> dateSets, NetCallback callback) {
        FreeClinicAddParams params = new FreeClinicAddParams();
        params.addHeader("verifyCode", getVerifyCode());
        params.doctorId = getDoctor().id;
        params.clinicList = new Gson().toJson(dateSets);
        HttpClient.post(params, callback);
    }

    /**
     * 修改密码
     */
    public static void passwordReset(String password, String oldpassword, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PasswordReset");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("password", password);
        params.addBodyParameter("oldpassword", oldpassword);
        HttpClient.get(params, callback);
    }

    /**
     * 获取最新版本
     */
    public static void getLastestVersion(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetLastestVersion");
        //    params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("type", "1");
        HttpClient.get(params, callback);
    }

    /**
     * 关闭视频问诊
     */
    public static void serviceRecordClose(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/ServiceRecordClose");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 支付宝账号绑定
     */
    public static void payAccountAdd(String payAccount, String name, NetCallback callback) {
        PayAccountAddParams params = new PayAccountAddParams(getDoctor().id, payAccount, name);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    /**
     * 医生获取支付宝绑定信息
     */
    public static void getPayAccount(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetPayAccount");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 医生解除绑定支付宝
     */
    public static void payAccountCancel(String id, NetCallback unBindCallback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PayAccountCancel");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("Id", id);
        HttpClient.get(params, unBindCallback);
    }

    /**
     * 医生获取开诊公告
     */
    public static void getVasAnnoucement(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetVasAnnoucement");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 医生设置开诊公告
     */
    public static void vasAnnoucementSet(String week, ArrayList<PostVideoTime> times, NetCallback callback) {
        VideoPostParams params = new VideoPostParams();
        params.addHeader("verifyCode", getVerifyCode());
        params.doctorId = getDoctor().id;
        params.week = week;
        params.annoucementList = new Gson().toJson(times);
        HttpClient.post(params, callback);
    }

    /**
     * 删除义诊接口
     */
    public static void freeClinicDelete(String id, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/FreeClinicDelete");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("Id", id);
        HttpClient.get(params, callback);
    }

    /**
     * 修改分组名
     */
    public static void groupUpDate(String id, String text, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GroupUpDate");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("groupId", id);
        params.addBodyParameter("groupName", text);
        HttpClient.get(params, callback);
    }


    /**
     * 删除分组
     */
    public static void groupRemove(String id, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GroupRemove");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("groupId", id);
        HttpClient.get(params, callback);
    }

    /**
     * 取消收藏
     */
    public static void collectionCancel(String sourceId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/CollectionCancel");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("sourceId", sourceId);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 获取通讯录所有医院
     */
    public static void getHosAddressBookDept(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetHosAddressBookDept");
        HttpClient.post(params, callback);
    }

    /**
     * 获取通讯录医院下的所有医生
     */
    public static void getHosAddressBook(String deptid, NetCallback callback) {
        DeptDoctorParams params = new DeptDoctorParams(deptid);
        params.addHeader("verifyCode", getVerifyCode());

        HttpClient.post(params, callback);
    }

    /**
     * 取消评论,取消点赞
     */
    public static void commentDelete(String commentId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/CommentDelete");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("commentId", commentId);
        HttpClient.get(params, callback);
    }

    /**
     * 取消眼科圈评论,取消点赞
     */
    public static void circleCommentDelete(String commentId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CommentDelete");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("commentId", commentId);
        HttpClient.get(params, callback);
    }

    /**
     * 删除好友
     */
    public static void circleFriendDelete(String friendId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleFriendDelete");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("friendId", friendId);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 设置/取消关注患者
     */
    public static void patientSetFocus(String id, int isFocus, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/PatientSetFocus");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("patientId", id);
        params.addBodyParameter("isFocus", isFocus + "");
        HttpClient.get(params, callback);
    }

    /**
     * 获取关注患者
     */
    public static void getFocusPatient(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetFocusPatient");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 患者报到单
     */
    public static void getPDApplyForm(String patientId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetPDApplyForm");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("patientId", patientId);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 判断文章是否收藏
     */
    public static void isCollection(String sourceId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/IsCollection");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("sourceId", sourceId);
        HttpClient.get(params, callback);
    }

    /**
     * 刷题
     */
    public static void docSelectQuestion(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/DocSelectQuestion");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("doctorIM", "d" + getDoctor().loginid);
        HttpClient.get(params, callback);
    }

    /**
     * 获取问题列表
     */
    public static void getQuestionList(String state, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetQuestionList");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("state", state);
        params.addBodyParameter("doctorIM", "d" + getDoctor().loginid);
        HttpClient.get(params, callback);
    }

    /**
     * 获取医院
     */
    public static void getHospitalByDoc(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetHospitalByDoc");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 检查是否已是好友状态
     */
    public static void checkFriendIsExist(String userId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CheckFriendIsExist");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("userId", userId);
        HttpClient.get(params, callback);
    }

    /**
     * 免费咨询:  修改订单状态    2:已回复   3.已完成
     */
    public static void questionStateUpdate(String state, String questionId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/QuestionStateUpdate");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("state", state);
        params.addBodyParameter("questionId", questionId);
        HttpClient.get(params, callback);
    }

    /**
     * 免费咨询:  医生转诊
     */
    public static void questionTransfer(String deptId, String questionId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/QuestionTransfer");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("deptId", deptId);
        params.addBodyParameter("questionId", questionId);
        HttpClient.get(params, callback);
    }

    /**
     * 免费咨询:  医生获取科室
     */
    public static void getHosDept(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetHosDept");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 修改消息为已读状态-4
     */
    public static void setQuestionMessageIsRead(String orderId, String patientIM, String doctorIM, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/SetMessageIsRead");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("fromAccount", patientIM);
        params.addBodyParameter("toAccount", doctorIM);
        HttpClient.get(params, callback);
    }

    /**
     * 上传图片
     */
    public static void pictureUpload(ArrayList<String> filePath, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/PictureUpload");
        params.addHeader("verifyCode", getVerifyCode());
        params.setMultipart(true);
        int i = 0;
        for (String s : filePath) {
            params.addBodyParameter("File" + i, new File(s));
            i++;
        }
        HttpClient.post(params, callback);
    }

    public static void newMedicalRecordAdd(String patientId, String result, String description, String drugs, String checks, ArrayList<PicBean> picturs, NetCallback callback) {
        NewMedicalRecordAddParams params = new NewMedicalRecordAddParams(
                getDoctor().id,
                patientId,
                result,
                description,
                drugs,
                checks,
                new Gson().toJson(picturs)
        );
        params.addHeader("verifyCode", getVerifyCode());

        HttpClient.post(params, callback);
    }

    /**
     * 新增病例的详情
     */
    public static void getNewMedicalRecord(String newId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetNewMedicalRecord");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("newId", newId);
        HttpClient.get(params, callback);
    }

    /**
     * 个人中心->获取本月收益  余额
     */
    public static void getEarnings(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetEarnings");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 辅助工具->群发通知->获取已发送
     */
    public static void getMassNotice(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetMassNotice");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    //*****************************************评论  start********************************************

    /**
     * 发布话题
     */
    public static void topicRelease(String title, String detail, String type, ArrayList<PicBean> filePath, NetCallback callback) {
        String file="";
        if(filePath.size()!=0){
            file=new Gson().toJson(filePath);
        }
        TopicReleaseParams params = new TopicReleaseParams(title, detail, type, file);
        params.addHeader("verifyCode", getVerifyCode());
        params.doctorId = getDoctor().id;
        HttpClient.post(params, callback);
    }

    //删除眼科圈自己的文章
    public static void topicDelete(String topicId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/TopicDelete");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("topicId", topicId);
        HttpClient.get(params, callback);
    }

    //查询所有话题
    public static void topicListFind(int page, int rows, int type, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/TopicListFind");
        params.addHeader("VerifyCode", getVerifyCode());
        params.addParameter("page", page + "");
        params.addParameter("rows", rows + "");
        params.addParameter("type", type + "");

        HttpClient.get(params, callback);
    }

    //删除互联网会诊
    public static void netConsultationDelete(String consulId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/NetConsultationDelete");
        params.addHeader("VerifyCode", getVerifyCode());
        params.addParameter("consulId", consulId);
        HttpClient.get(params, callback);
    }

    /**
     * 查看话题详情
     */
    public static void topicDetailFind(String topicId, NetCallback callback) {

        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/TopicDetailFind");
        params.addHeader("verifyCode", getVerifyCode());
        params.addBodyParameter("topicId", topicId);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 添加评论接口
     */
    public static void commentAdd(String topicId, String commentType, String comment, String reviewerId, NetCallback callback) {
        CommentAddParams params = new CommentAddParams(topicId, getDoctor().id, commentType, comment, reviewerId);
        params.addHeader("verifyCode", getVerifyCode());
        HttpClient.post(params, callback);
    }

    //*****************************************评论  end********************************************


    //*****************************************通讯录  start****************************************
    //查看好友验证列表
    public static void circleFriendInfo(int rows, int page, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleFriendInfo");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("rows", rows + "");
        params.addBodyParameter("page", page + "");
        HttpClient.get(params, callback);
    }

    //查看好友列表接口
    public static void circleFriendListFind(String doctorName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleFriendListFind");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("doctorId", getDoctor().id);
        params.addBodyParameter("doctorName", doctorName);
        HttpClient.get(params, callback);
    }

    //查看发送好友列表接口
    public static void circleApplyInfo(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleApplyInfo");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    /**
     * 添加好友申请
     */
    public static void circleFriendAdd(String receiveId, NetCallback callback) {
        AddFriendParams params = new AddFriendParams(getDoctor().id, receiveId);
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        HttpClient.post(params, callback);
    }

    /**
     * 好友验证通过/拒绝
     */
    public static void circleFriendValidate(String friendId, String state, String applyId, NetCallback callback) {
        FriendValidateParams params = new FriendValidateParams(friendId, state, applyId, getDoctor().id);
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        HttpClient.post(params, callback);
    }

    /**
     * 删除好友接口
     */
    public static void circleFriendDelete(String friendId, String doctorId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleFriendDelete");
        params.addHeader("verifyCode", getDoctor().verifyCode);
        params.addBodyParameter("friendId", friendId);
        params.addBodyParameter("doctorId", doctorId);
        HttpClient.post(params, callback);

    }

    /**
     * 通过手机或名字查找联系人
     */
    public static void circleFriendFind(String value, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Circle/CircleFriendFind");
        params.addHeader("verifyCode", getDoctor().verifyCode);
        params.addBodyParameter("value", value);

        HttpClient.get(params, callback);

    }

    //***********************************************通讯录   end***********************************
    //***********************************************患者   start***********************************
    //新增患者
    public static void addPatient(PatientParams params, NetCallback callback) {
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //查询所有的预约信息
    public static void queryAppointPatient(AppointPatientParams params, NetCallback callback) {
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //获取所有患者
    public static void queryAllPatient(String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetPatientByDoc");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("patientName", patientName);
        HttpClient.get(params, callback);

    }

    //查询医药详情
    public static void queryDetail(String outpatientCard, NetCallback callback) {
        MedicialDetailParams params = new MedicialDetailParams();
        params.OutpatientCard = outpatientCard;
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }


    // 获取订单列表(医生端患者会话列表)
    // 患者姓名(搜索功能使用，支持模糊搜索,搜索时可不用传state参数进行全局搜索)
    public static void queryConsult(String state, String orderType, String patientName, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetTextOrderList");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addParameter("doctorId", getDoctor().id);
        params.addParameter("state", state);
        params.addParameter("orderType", orderType);
        params.addParameter("patientName", patientName);
        HttpClient.get(params, callback);
    }

    //病历夹->删除患者
    public static void deletePatient(String id, NetCallback callback) {
        DeletePatientParams params = new DeletePatientParams();
        params.id = id;
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    // 移动患者到其他分组
    public static void patientGroupMove(String patientId, String groupId, NetCallback callback) {

        PatientGroupMoveParams params = new PatientGroupMoveParams(patientId, groupId);
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.get(params, callback);
    }

    //更新患者信息
    public static void updatePatient(PatientParams params, NetCallback callback) {
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //添加分组
    public static void addGroup(String groupName, NetCallback callback) {
        AddGroupParams params = new AddGroupParams(groupName);
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //  获取医生所属患者分组
    public static void getGroupByDoc(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetGroupByDoc");
        params.addHeader(VERIFYCODE, getVerifyCode());
        HttpClient.post(params, callback);
    }

    //查询患者详情
    public static void getPatientByGroup(String groupId, NetCallback itemCallback) {
        PatientByGroupParams params = new PatientByGroupParams();
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.groupId = groupId;
        LogUtils.i("groupId" + groupId);
        HttpClient.get(params, itemCallback);
    }

    //病历讨论->病历详情
    public static void discussionDetailFind(String disId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/DiscussionDetailFind");
        params.addHeader(VERIFYCODE, getVerifyCode());
        params.addBodyParameter("disId", disId);
        HttpClient.get(params, callback);
    }
    //***********************************************患者   end*************************************

    //***********************************************医生用户  start*********************************
    //注册
    public static void register(String account, String password, NetCallback callback) {
        RegisterParams params = new RegisterParams();
        params.Loginid = account;
        params.Password = password;
        HttpClient.post(params, callback);
    }

    //登录
    public static void login(String name, String password, NetCallback callback) {
        LoginParams params = new LoginParams();
        params.loginId = name;
        params.password = password;
        HttpClient.post(params, callback);
    }

    //更新信息
    public static void updateInfo(DoctorUpdateParams params, NetCallback callback) {
        params.loginid = getDoctor().loginid;
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        HttpClient.post(params, callback);
    }

    //上传认证
    public static void postApprove(NetCallback callback) {
        DoctorApproveParams params = new DoctorApproveParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.certNo = getDoctor().licenseCard;
        params.hosCode = getDoctor().licenseHospital;
        params.deptCode = getDoctor().licenseDept;
        params.docTitle = getDoctor().licenseTitle;
        params.docLevel = getDoctor().licenseLevel;
        params.idCard = getDoctor().idCard;
        params.picurl = getDoctor().licenseImages;
        HttpClient.post(params, callback);
    }

    //上传头像
    public static void postHead(NetCallback callback) {
        String filePath = DoctorInfoActivity.SDCARD + DoctorInfoActivity.PATH + getDoctor().loginid + "/" + DoctorInfoActivity.HEAD_JPG;
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/SetDoctorHead");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.setMultipart(true);
        params.addBodyParameter("uploadFile", new File(filePath));
        HttpClient.post(params, callback);
    }

    //查询认证
    public static void queryApprove(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/GetLicenseInfo");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        HttpClient.get(params, callback);
    }

    //获取验证码
    public static void getAuth(String phone, NetCallback callback) {
        GetAuthParams params = new GetAuthParams(phone);
        HttpClient.post(params, callback);
    }

    //忘记密码
    public static void resetPassword(String loginid, String password, NetCallback callback) {
        ResetPasswordParams params = new ResetPasswordParams();
        params.loginid = loginid;
        params.password = password;
        HttpClient.post(params, callback);
    }

    //获取停诊通知记录
    public static void getStopNotice(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetStopNotice");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("doctorId", getDoctor().id);
        HttpClient.get(params, callback);
    }

    //修改已读
    public static void messageRead(String messageId, NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/MessageRead");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("messageId", messageId);
        HttpClient.get(params, callback);
    }

    //获取站内消息未读数
    public static void getMessageCount(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetMessageCountByDoc");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        HttpClient.post(params, callback);
    }
    //获取图文咨询消息未读数
    public static void getChatCount(NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Share/GetChatCount");
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.addBodyParameter("doctorIM", "d"+getDoctor().loginid);
        params.addBodyParameter("source", "");
        HttpClient.get(params, callback);
    }
    //药品搜索
    public static void searchEyeDrugList(String name,NetCallback callback) {
        RequestParams params = new RequestParams(ConstantValues.HOST + "/Doctor/SearchEyeDrugList");
        params.addBodyParameter("name", name);
        HttpClient.get(params, callback);
    }

    //获取电子病历-1
    public static void getNowPubliceEvent(String pageNumber, String pageSize, NetCallback callback) {
        NowPubliceEventParams params = new NowPubliceEventParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.pageNumber = pageNumber;
        params.pageSize = pageSize;
        //  params.doctorId=getDoctor().id;

        HttpClient.post(params, callback);
    }

    //查询住院病人电子病历列表-2
    public static void getPubliceList(String pageNumber, String pageSize,String eventNo, NetCallback callback) {
        PubliceListParams params = new PubliceListParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.pageNumber = pageNumber;
        params.pageSize = pageSize;
        params.eventNo = eventNo;
        HttpClient.post(params, callback);
    }

    //获取文件-3
    public static void getPubliceContent(String patientId, String emrId, String name, String eventNo, NetCallback callback) {
        PubliceContentParams params = new PubliceContentParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.patientId = patientId;
        params.emrId = emrId;
        params.eventNo = eventNo;
        params.name = name;

        HttpClient.post(params, callback);
    }

    //文件下载
    public static void caseLoad(String eventNo, String emrId, String patientId, String name, NetCallback callback) {

        CaseDownloadParams params = new CaseDownloadParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.emrId = emrId;
        params.eventNo = eventNo;
        params.patientId = patientId;
        params.name = name;
        HttpClient.post(params, callback);

    }

    //发送短信
    public static void sendMessage(String tel,String type,NetCallback callback) {
        SendMessageParams params=new SendMessageParams();
        params.addHeader("VerifyCode", getDoctor().verifyCode);
        params.doctorName=getDoctor().userName;
        params.type=type;
        params.tel=tel;
        HttpClient.post(params, callback);
    }

    //***********************************************医生用户  end***********************************


}
