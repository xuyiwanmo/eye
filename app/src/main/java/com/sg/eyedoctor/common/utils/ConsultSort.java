package com.sg.eyedoctor.common.utils;

import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ConsultSort implements Comparator {

    @Override
    public int compare(Object time1, Object time2) {
        long flag =((Patient) time1).latestDate.compareTo(((Patient) time2).latestDate);
        if (flag > 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
