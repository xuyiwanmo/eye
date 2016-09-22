package com.sg.eyedoctor;

import com.sg.eyedoctor.main.bean.ImgDescription;

import java.util.ArrayList;

public class ConstantValues {

    public static final String KEY_RECORD = "record";
    public static final String KEY_DELETE = "delete";
    public static final String KEY_PATIENT = "patient";
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";

    public static final String KEY_POSITON = "position";
    public static final String KEY_UPDATE = "update";
    public static final String KEY_DATA = "data";
    public static final String KEY_HIS = "his";
    public static final String KEY_CAN_EDIT = "canEdit";
    public static final String KEY_VERSION = "version";
    public static final String KEY_TEAM_ID = "teamId";
    public static final String KEY_REVIEWER_ID = "ReviewerId";
    public static final String KEY_CONSULT_DETAIL = "consultDetail";
    public static final String KEY_IS_TEXT_CONSULT = "isTextConsult";
    public static final String KEY_ORDER_ID = "orderId";
    public static final String KEY_END = "end";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IS_RECORD = "isRecord";
    public static final String KEY_XTR_ID = "xtrId";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_HOS_ID = "hosId";
    public static final String KEY_SORT = "sort";
    public static final String KEY_HINT = "hint";
    public static final String KEY_DAY = "day";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_ORDER = "order";
    public static final String KEY_PUSH = "push";

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_HEAD = "EXTRA_HEAD";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String DIS_ID = "DIS_ID";
    public static final String DIS_NAME = "DIS_NAME";
    public static final String CHOOSE_HEAD = "CHOOSE_HEAD";
    public static final String CHANNELID = "CHANNELID";
    public static final String CHANNELNAME = "CHANNELNAME";
    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_LOGIN = "first_login";

    public static final int REQEST_HEAD_CODE = 3;
    public static final int REQUEST_IMAGE = 2;
    public static final int REQUEST_IMPORT_CASE = 1;
    public static final int REQUEST_DELETE_IMAGE = 4;

    public static final int WIFI = 3;//内网wifi
    public static final int NET = 2;//外网
    public static final int SERVER = 1;//服务器

    public static String HOST = ConstantValues.SERVER_HOST;
    public static String IMG_HOST = ConstantValues.SERVER_IMG_HOST;

    public static final String WIFI_HOST = "http://192.168.1.100:90/api";
    public static final String WIFI_IMG_HOST = "http://192.168.1.100:90";

    public static final String NET_HOST = "http://liookun.imwork.net:90/api";
    public static final String NET_IMG_HOST = "http://liookun.imwork.net:90";

    public static final String SERVER_HOST = "http://www.yanketong.com:90/api";
    public static final String SERVER_IMG_HOST = "http://www.yanketong.com:90";

    /**
     * 查询
     */
    public static ArrayList<ImgDescription> HOME_QUERY_GRID_LIST = new ArrayList<>();
    /**
     * 常用工具
     */
    public static ArrayList<ImgDescription> HOME_COMMON_TOOLS_LIST = new ArrayList<>();
    /**
     * 辅助工具
     */
    public static ArrayList<ImgDescription> HOME_HELP_TOOLS_LIST = new ArrayList<>();


    static {
        HOME_QUERY_GRID_LIST.add(new ImgDescription(R.drawable.home_text_consult, "图文咨询",0));
        HOME_QUERY_GRID_LIST.add(new ImgDescription(R.drawable.home_phone_consult, "电话咨询",0));
        HOME_QUERY_GRID_LIST.add(new ImgDescription(R.drawable.home_video_consult, "视频咨询",0));

        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_add_patient, "新增患者",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_outpatient, "门诊记录",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_plus, "加号管理",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_patient_register, "患者报到",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_case_discuss, "病历讨论",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_net, "互联网会诊",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_front_academic, "学术前沿",0));
        HOME_COMMON_TOOLS_LIST.add(new ImgDescription(R.drawable.home_tool_box, "工具箱",0));

        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_crowd_send, "群发通知",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_stop, "停诊通知",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_elec_case, "电子病历",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_doctor_charged, "医嘱",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_volunteer, "义诊发布",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_assister, "医生助理",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.home_communicate, "通讯录",0));
        HOME_HELP_TOOLS_LIST.add(new ImgDescription(R.drawable.ic_free_consult, "免费咨询",0));
    }

    /**
     * 站内消息 已发送消息
     */
    public static final int STATE_ALREADY_SEND = 0;
    public static final int STATE_RECEIVER = 1;

    /**
     * 患者中心:  新增  平台   医院
     */
    public static final int PATIENT_CENTER_HOSPITAL = 2;
    public static final int PATIENT_CENTER_PLATFORM = 1;
    public static final int PATIENT_CENTER_NEW_ADD = 3;
    public static final int PATIENT_CENTER_CHOOSE = 4;

    /**
     * 病历夹
     */
    public static final int CHART_REQUEST_CHECK = 1;
    public static final int CHART_REQUEST_DRUG = 2;
    public static final int CHART_REQUEST_IMAGE = 3;

    /**
     * 拍照
     */
    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;

    /**
     * 个人中心  信息类别
     * 0性别    1身份证  2教学职称  3.擅长  4.介绍  5.头像   6.认证   7.姓名  8电话
     */
    public static final int DOCTOR_SEX = 0;
    public static final int DOCTOR_IDCARD = 1;
    public static final int DOCTOR_TITLE = 2;
    public static final int DOCTOR_SPECIALITY = 3;
    public static final int DOCTOR_INTRODUCE = 4;
    public static final int DOCTOR_HEAD = 5;
    public static final int DOCTOR_IMPROVE = 6;
    public static final int DOCTOR_NAME = 7;
    public static final int DOCTOR_TEL = 8;
    /**
     * 0执业  1医院  2.科室   3.职称   4.级别  5身份证
     */
    public static final int APPROVE_OPERATION = 0;
    public static final int APPROVE_HOSPITAL = 1;
    public static final int APPROVE_DEPT = 2;
    public static final int APPROVE_TITLE = 3;
    public static final int APPROVE_RANK = 4;
    public static final int APPROVE_IDCARD = 5;

    /**
     * 星期
     */
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    /**
     * 1图文 2电话  3视频  4加号4
     */
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PHONE = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_PLUS = 4;

    /**
     * 1学术前沿  2互联网会诊   3眼科圈
     */
    public static final int STORE_ACADEMIC = 1;
    public static final int STORE_INTERNET = 2;
    public static final int STORE_CIRCLE = 3;

    /**
     * 病历夹  1重命名  2删除分组  3删除患者 4查询分组
     */
    public static final int CHART_RENAME_GROUP = 0;
    public static final int CHART_DELETE_GROUP = 1;
    public static final int CHART_DELETE_PATIENT = 2;
    public static final int CHART_QUERY_GROUP = 3;

    /**
     * 聊天类型  1:图文咨询/2:免费咨询/3:病例讨论/4患者报道/5:医生通讯录
     */
    public static final int CHAT_TEXT = 1;
    public static final int CHAT_FREE = 2;
    public static final int CHAT_CASE_DISCUSS = 3;
    public static final int CHAT_REPORT = 4;
    public static final int CHAT_DOCTOR = 5;

    /**
     * P2P,TEAM 聊天面板类型   1:图文 未回复   5:图文 已回复    4：图文 已完成
     * 6:医生聊天     8:免费咨询,转诊   9患者报道
     */
    public static final int P2P_TEXT_NEW = 1;
    public static final int P2P_TEXT_REPLY = 5;
    public static final int P2P_TEXT_COMPLETE = 4;
    public static final int P2P_DOCTOR = 6;
    public static final int P2P_FREE = 8;
    public static final int P2P_REPORT = 9;

    /**
     * 免费咨询类型  1:新咨询  2.已回复   3.已完成
     */
    public static final int FREE_NEW = 1;
    public static final int FREE_REPLY = 2;
    public static final int FREE_COMPLETE = 3;

    /**
     * 评论: 0互联网会诊  1为眼科圈
     */
    public static final int COMMENT_INTERNET = 0;
    public static final int COMMENT_EYE_CIRCLE = 1;

    /**
     * 点赞 1点赞  2评论
     */
    public static final String COMMENT_TYPE_SUPPORT = "1";
    public static final String COMMENT_TYPE_COMMENT = "2";

    /**
     * 患者报到  0待接受   1已添加
     */
    public static final int REPORT_TYPE_ACCEPT = 0;
    public static final int REPORT_TYPE_ALREADY = 1;
    /**
     * 推送消息  1:图文咨询 2：电话咨询 3：视频咨询 4:加号 5：互联网会诊 6：眼科圈
     */
    public static final int PUSH_TEXT_CONSULT = 11;
    public static final int PUSH_PHONE_CONSULT = 22;
    public static final int PUSH_VIDEO_CONSULT = 33;
    public static final int PUSH_PLUS = 44;
    public static final int PUSH_INTERNET_CONSULT = 55;
    public static final int PUSH_EYE_CIRCLE = 66;



}
