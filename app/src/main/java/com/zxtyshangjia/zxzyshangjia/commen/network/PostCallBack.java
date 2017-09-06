package com.zxtyshangjia.zxzyshangjia.commen.network;

/**
 * 请求回调
 * Created by LvJinrong on 2017/2/28.
 */

public interface PostCallBack {
    void onSuccess(Object data);
    void onError(int code, String msg);
}
