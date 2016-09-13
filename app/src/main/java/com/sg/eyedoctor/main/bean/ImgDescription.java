package com.sg.eyedoctor.main.bean;

/**
 * 首页gridview  图片和文字说明
 * Created by zhanghua on 2015/12/29.
 */
public class ImgDescription {
    /**
     * 图片在本地的id
     */
    public int mDrawableId;
    /**
     * 文字说明
     */
    public String mDescription;
    public int count;

    public ImgDescription(int mDrawableId, String mDescription,int count) {
        this.mDrawableId = mDrawableId;
        this.mDescription = mDescription;
        this.count=count;
    }
}
