package com.sg.eyedoctor.lookPicture.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sg.eyedoctor.ConstantValues;
import com.sg.eyedoctor.R;
import com.sg.eyedoctor.common.bean.PicLocalBean;
import com.sg.eyedoctor.common.utils.ImageUtils;
import com.sg.eyedoctor.common.view.photoview.PhotoView;
import com.sg.eyedoctor.common.view.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mabeijianxi on 2015/1/5.
 */
public class ImageScaleAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {
    private List<PicLocalBean> mPicData;
    private Context mContext;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.home_youpin)
            .showImageOnFail(R.drawable.home_youpin)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)//会对图片进一步的缩放
            .bitmapConfig(Bitmap.Config.RGB_565)//此种模消耗的内存会很小,2个byte存储一个像素
            .considerExifParams(true)
            .build();
    private PhotoView imageView;
    private View mCurrentView;

    public ImageScaleAdapter(Context mContext, ArrayList<PicLocalBean> data) {
        super();
        this.mPicData = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mPicData != null) {
            return mPicData.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_photoview, container, false);
        imageView = (PhotoView) inflate.findViewById(R.id.pv);
        imageView.setOnPhotoTapListener(this);
        final ProgressBar pb = (ProgressBar) inflate.findViewById(R.id.pb);
        final PicLocalBean ealuationPicBean = mPicData.get(position);

  //      if (ealuationPicBean != null && ealuationPicBean.picUrl != null && !"null".equals(ealuationPicBean.picUrl)) {
            setupNetImage(pb, ealuationPicBean);
//        } else {
//            imageView.setImageResource(R.drawable.home_youpin);
//        }

        container.addView(inflate);//将ImageView加入到ViewPager中
        return inflate;
    }

    /**
     * 设置网络图片加载规则
     * @param pb
     * @param picture
     */
    private void setupNetImage(final ProgressBar pb, final PicLocalBean picture) {
        if(picture.localUrl!=null){
            mImageLoader.displayImage("file://" + picture.localUrl, imageView);
            return;
        }
        String url1="";
        if(picture.picUrl.contains("http")){
            url1=picture.picUrl;
        }else{
            String url = ConstantValues.IMG_HOST + picture.picUrl;
            url1 = url.replaceAll("\\\\", "/");
        }
//        ImageLoadingListener listener=new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                startLoad(pb);
//                showExcessPic(picture, imageView);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                overLoad( pb);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                overLoad( pb);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                overLoad( pb);
//            }
//        };


        mImageLoader.displayImage(url1, imageView, pager_options);
    }

    /**
     * 展示过度图片
     *
     * @param ealuationPicBean
     * @param imageView
     */
    private void showExcessPic(PicLocalBean ealuationPicBean, PhotoView imageView) {
        Bitmap bitmap = ImageUtils.getBitmapFromCache(ealuationPicBean.microPicUrl, mImageLoader);
        if (bitmap != null) {

            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.home_youpin);
        }
    }

    /**
     * 显示进度条
     *
     * @param pb
     */
    private void startLoad( ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    /**
     * 结束进度条
     *
     * @param pb
     */
    private void overLoad( ProgressBar pb) {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    /**
     * 单击屏幕关闭
     *
     * @param view - View the user tapped.
     * @param x    - where the user tapped from the of the Drawable, as
     *             percentage of the Drawable width.
     * @param y    - where the user tapped from the top of the Drawable, as
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
      //  ((LookBigPicActivity) mContext).startActivityAnim();
    }

    public void setData(ArrayList<PicLocalBean> data){
        this.mPicData = data;
        notifyDataSetChanged();
    }
}
