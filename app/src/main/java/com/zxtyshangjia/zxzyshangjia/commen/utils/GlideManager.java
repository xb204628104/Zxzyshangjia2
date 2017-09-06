package com.zxtyshangjia.zxzyshangjia.commen.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zxtyshangjia.zxzyshangjia.R;

import java.io.File;

/**
 * 图片加载管理器
 * 注1：可使用的方案需要的ImageView不可以是自定义的，否则可能首次加载不出来。
 * 注2：ImageView不可设置tag,否则会有错误提示
 * Created by zhongcx on 2017/2/14.
 */
public class GlideManager {
    private Drawable errorDrawable;//默认加载失败的视图
    private Drawable loadingDrawable;//默认加载中的视图
    private Drawable errorDrawable2;//默认加载失败的视图
    private Drawable loadingDrawable2;//默认加载中的视图
    private Drawable errorDrawable3;//默认加载失败的视图
    private Drawable loadingDrawable3;//默认加载中的视图

    /*=============Model============*/
    /**
     * 加载图片需要的上下文
     */
    private Context applicationContext;


    /**
     * 加载图片的圆形方案
     */
    private GlideCircleTransform mGlideCircleTransform;
    /**
     * 加载圆角的图片
     */
    private GlideRoundTransform mGlideRoundTransform;

    /**
     * 创建管理类的时候，设置相应的加载样式，以及监听
     *
     * @param applicationContext 当前上下文，建议在Application中初始化
     */
    public GlideManager(Context applicationContext) {
        this.applicationContext = applicationContext;
        /**加载圆型方案*/
        mGlideCircleTransform = new GlideCircleTransform(applicationContext);
        revertDefault();
    }


    /*===============ChildFunction===========*/

    /**
     * 给一个ImageView加载一张图片，带渐变动画。
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImage(String loadUrl, ImageView loadImageView) {
        if (loadImageView == null) {//加载图片不存在
            return;
        }
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageBitmap(null);
            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载的时候，背景透明
        try {
            Glide.with(applicationContext)
                    .load(loadUrl)
                    .centerCrop()
                    .placeholder(loadingDrawable)//设置加载过程中显示的图片
                    .error(errorDrawable)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputImage", e.toString());
        }
    }

    /**
     * 给一个ImageView加载一张图片，带渐变动画。
     * 不剪裁,适合大图浏览,默认加载中黑色，加载失败也是黑色
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImageNoCrop(String loadUrl, ImageView loadImageView) {
        if (loadImageView == null) {//加载图片不存在
            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#000000"));//设置加载的时候，背景透明
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageBitmap(null);

            return;
        }

        try {
            Glide.with(applicationContext)
                    .load(loadUrl)
                    .placeholder(loadingDrawable3)//设置加载过程中显示的图片
                    .error(errorDrawable3)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputImageNoCrop", e.toString());
        }

    }

    /**
     * 给一个ImageView加载一张圆形图片（比如头像），带渐变动画。
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImageCircle(String loadUrl, ImageView loadImageView) {

        if (loadImageView == null) {//加载图片不存在
            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载圆的时候，背景透明
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageDrawable(errorDrawable2);
            return;
        }
        try {
            Glide.with(applicationContext)
                    .load(loadUrl)
                    .centerCrop()
                    .transform(mGlideCircleTransform)
                    .placeholder(loadingDrawable2)//设置加载过程中显示的图片
                    .error(errorDrawable2)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputImageCircle", e.toString());
        }

    }

    /**
     * 给一个ImageView加载一张圆角的头像，带渐变动画。
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImageRound(String loadUrl, ImageView loadImageView) {

        if (loadImageView == null) {//加载图片不存在
            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载圆的时候，背景透明
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageDrawable(errorDrawable2);
            return;
        }
        try {
            mGlideRoundTransform = mGlideRoundTransform == null ? new GlideRoundTransform(applicationContext, 10)
                    : mGlideRoundTransform;
            Glide.with(applicationContext)
                    .load(loadUrl)
                    .centerCrop()
                    .transform(mGlideRoundTransform)
                    .placeholder(loadingDrawable2)//设置加载过程中显示的图片
                    .error(errorDrawable2)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputImageCircle", e.toString());
        }

    }

    /**
     * 给一个ImageView加载一张本地图片，带渐变动画。
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImageLocal(String loadUrl, ImageView loadImageView) {
        if (loadImageView == null) {//加载图片不存在
            return;
        }
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageBitmap(null);

            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载的时候，背景透明

        try {

            Glide.with(applicationContext)
                    .load(new File(loadUrl))
                    .centerCrop()
                    .placeholder(loadingDrawable)//设置加载过程中显示的图片
                    .error(errorDrawable)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputImageLocal", e.toString());
        }
    }

    /**
     * 给一个ImageView加载一张圆形本地图片，带渐变动画。
     * @param path          本地图片地址
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputCircleImageLocal(String path, ImageView loadImageView) {
        if (loadImageView == null) {//加载图片不存在
            return;
        }
        if (TextUtils.isEmpty(path)) {//url是空的
            loadImageView.setImageBitmap(null);

            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载的时候，背景透明

        try {

            Glide.with(applicationContext)
                    .load(new File(path))
                    .centerCrop()
                    .transform(mGlideCircleTransform)
                    .placeholder(loadingDrawable)//设置加载过程中显示的图片
                    .error(errorDrawable)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager.inputCircleImageLocal", e.toString());
        }
    }


    /**
     * 给一个ImageView加载一张本地图片，带渐变动画。
     * @param loadUrl       加载的url
     * @param loadImageView 需要加载上去的ImageView
     */
    public void inputImageLocalNoCrop(String loadUrl, ImageView loadImageView) {
        if (loadImageView == null) {//加载图片不存在
            return;
        }
        if (TextUtils.isEmpty(loadUrl)) {//url是空的
            loadImageView.setImageBitmap(null);

            return;
        }
        loadImageView.setBackgroundColor(Color.parseColor("#00000000"));//设置加载的时候，背景透明

        try {

            Glide.with(applicationContext)
                    .load(new File(loadUrl))
                    .placeholder(loadingDrawable)//设置加载过程中显示的图片
                    .error(errorDrawable)//设置加载失败显示的图片
                    .crossFade()//设置渐渐显示的效果
                    .into(loadImageView);
        } catch (Exception e) {
            LogUtil.e("GlideManager", e.toString());
        }
    }
    /*=================================================*/

    /**
     * 圆型自定义加载失败的视图
     *
     * @param errorDrawable2 加载失败的视图
     */
    public void setErrorDrawable2(Drawable errorDrawable2) {
        this.errorDrawable2 = errorDrawable2;
    }

    /**
     * 圆型自定义加载失败的视图
     *
     * @param drawableResId2 加载失败的视图的资源Id
     */
    public void setErrorDrawable2(int drawableResId2) {
        this.errorDrawable2 = ContextCompat.getDrawable(applicationContext, drawableResId2);
    }

    /**
     * 圆型自定义加载中的视图
     *
     * @param loadingDrawable2 加载中的视图
     */
    public void setLoadingDrawable2(Drawable loadingDrawable2) {
        this.loadingDrawable2 = loadingDrawable2;
    }

    /**
     * 圆型自定义加载中的视图
     *
     * @param drawableResId2 加载中的视图的资源Id
     */
    public void setLoadingDrawable2(int drawableResId2) {
        this.loadingDrawable2 = ContextCompat.getDrawable(applicationContext, drawableResId2);
    }

    /**
     * 自定义加载失败的视图
     *
     * @param errorDrawable 加载失败的视图
     */
    public void setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    /**
     * 自定义加载失败的视图
     *
     * @param drawableResId 加载失败的视图的资源Id
     */
    public void setErrorDrawable(int drawableResId) {
        this.errorDrawable = ContextCompat.getDrawable(applicationContext, drawableResId);
    }

    /**
     * 自定义加载中的视图
     *
     * @param loadingDrawable 加载中的视图
     */
    public void setLoadingDrawable(Drawable loadingDrawable) {
        this.loadingDrawable = loadingDrawable;
    }

    /**
     * 自定义加载中的视图
     *
     * @param drawableResId 加载中的视图的资源Id
     */
    public void setLoadingDrawable(int drawableResId) {
        this.loadingDrawable = ContextCompat.getDrawable(applicationContext, drawableResId);
    }

    /**
     * 恢复到默认的加载中图片和加载失败图片
     */

    public void revertDefault() {
        loadingDrawable = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);
        errorDrawable = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);
        loadingDrawable2 = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);
//        errorDrawable2 = ContextCompat.getDrawable(applicationContext, R.drawable.jjb_loading_bg_2);
        errorDrawable2 = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);
        loadingDrawable3 = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);
        errorDrawable3 = ContextCompat.getDrawable(applicationContext, R.mipmap.default_img);

    }


}

/**
 * 圆形图片
 * Created by yiw on 2016/6/6.
 */
class GlideCircleTransform extends BitmapTransformation {

    public GlideCircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;

    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}

/**
 *  将图片转化为圆角
 *  构造中第二个参数定义半径
 */
class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}




