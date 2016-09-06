package com.sg.eyedoctor.commUtils.toolbox.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/18.
 */
public class EyeCheck {
    /**
     * id : 103cb190-4a8a-4b55-a428-b8be6262b1de
     * checkName : 色觉检查
     * description : 如果看到数字12，色觉是正常的。如果看到数字3，可能有红绿色觉异常。
     * createDate : 2016-03-17 16:53:07
     * piclist : [{"id":"1ee1262c-7f9c-44cb-821f-199da8ca1601","checkId":"103cb190-4a8a-4b55-a428-b8be6262b1de","picUrl":"\\UploadFile\\Tool/2016031700101.jpg","createDate":"2016-03-17 17:47:16"},{"id":"2ee1262c-7f9c-44cb-821f-199da8ca1601","checkId":"103cb190-4a8a-4b55-a428-b8be6262b1de","picUrl":"\\UploadFile\\Tool/2016031700102.jpg","createDate":"2016-03-17 17:47:16"},{"id":"3ee1262c-7f9c-44cb-821f-199da8ca1601","checkId":"103cb190-4a8a-4b55-a428-b8be6262b1de","picUrl":"\\UploadFile\\Tool/2016031700103.jpg","createDate":"2016-03-17 17:47:16"},{"id":"4ee1262c-7f9c-44cb-821f-199da8ca1601","checkId":"103cb190-4a8a-4b55-a428-b8be6262b1de","picUrl":"\\UploadFile\\Tool/2016031700104.jpg","createDate":"2016-03-17 17:47:16"},{"id":"5ee1262c-7f9c-44cb-821f-199da8ca1601","checkId":"103cb190-4a8a-4b55-a428-b8be6262b1de","picUrl":"\\UploadFile\\Tool/2016031700105.jpg","createDate":"2016-03-17 17:47:16"}]
     */
    public String id;
    public String checkName;
    public String description;
    public String createDate;
    /**
     * id : 1ee1262c-7f9c-44cb-821f-199da8ca1601
     * checkId : 103cb190-4a8a-4b55-a428-b8be6262b1de
     * picUrl : \UploadFile\Tool/2016031700101.jpg
     * createDate : 2016-03-17 17:47:16
     */

    public ArrayList<PiclistEntity> piclist;

    public static class PiclistEntity {
        public String id;
        public String checkId;
        public String picUrl;
        public String createDate;
    }
}
