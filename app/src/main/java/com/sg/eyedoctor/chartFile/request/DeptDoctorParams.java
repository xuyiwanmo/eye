package com.sg.eyedoctor.chartFile.request;

import com.sg.eyedoctor.ConstantValues;

import org.xutils.http.RequestParams;

/**
 * 获取通讯录
 */
public class DeptDoctorParams extends RequestParams {

    public String deptid;

    public DeptDoctorParams(String deptid) {
        super(ConstantValues.HOST+"/Doctor/GetHosAddressBook");
        this.deptid = deptid;

    }
}
