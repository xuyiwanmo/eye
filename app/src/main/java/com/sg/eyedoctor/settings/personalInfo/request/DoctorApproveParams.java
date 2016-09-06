package com.sg.eyedoctor.settings.personalInfo.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/1/18.
 */

public class DoctorApproveParams extends RequestParams {
    public String hosCode;
    public String deptCode;
    public String docTitle;
    public String docLevel;
    public String certNo;
    public String idCard;
    public String picurl;

    public DoctorApproveParams() {
        super(ConstantValues.HOST+"/Doctor/UploadPictures");
    }

}
