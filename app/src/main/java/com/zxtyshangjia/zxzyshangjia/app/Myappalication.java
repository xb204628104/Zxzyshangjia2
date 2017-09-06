package com.zxtyshangjia.zxzyshangjia.app;

import android.app.Application;
import android.content.Context;

import com.zxtyshangjia.zxzyshangjia.commen.utils.GlideManager;

/**
 * Created by 18222 on 2017/8/7.
 */

public class Myappalication extends Application {

    private static Context mContext;
    private static Myappalication _instance = null;


    /**
     * Glide 图片加载管理类
     */
    GlideManager mGlideManager;


    @Override
    public void onCreate() {

        super.onCreate();
        mContext = getApplicationContext();
        this._instance = this;
    }
    //获取全局上下文
    public static Context getContext(){
        return mContext;
    }

    /**
     * 获取加载图片方法
     *
     * @return 加载图片的具体方法
     */
    public static GlideManager getGlideManager() {
        _instance.mGlideManager = _instance.mGlideManager == null ? new GlideManager(_instance) : _instance.mGlideManager;
        return _instance.mGlideManager;
    }



}
