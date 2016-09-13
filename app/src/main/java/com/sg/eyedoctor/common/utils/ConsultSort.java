package com.sg.eyedoctor.common.utils;

import com.sg.eyedoctor.consult.textConsult.bean.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ConsultSort implements Comparator {

    @Override
    public int compare(Object time1, Object time2) {
        boolean flag = false;
        //2016/9/12 16:09:45
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss", Locale.CHINA);
        if(((Patient) time1).latestDate==null||((Patient) time2).latestDate==null){
            return -1;
        }
        try {
            Date d1 = format.parse(((Patient) time1).latestDate);
            Date d2 = format.parse(((Patient) time2).latestDate);
            if (d1.getTime() < d2.getTime()) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (flag) {
            return 1;
        } else {
            return -1;
        }
    }
}
