package com.zxtyshangjia.zxzyshangjia.commen.network;


import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;
import com.zxtyshangjia.zxzyshangjia.commen.utils.LogUtil;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * new HttpUtil(Api.USER_SIM_CODE, map, Login.class, callBack).postExecute();
 * Created by LvJinrong on 2017/2/28.
 */

public class HttpUtil {
    private String url = "";
    private RequestCall build;
    private Class responseClass;
    private PostCallBack postResponeCallBack;


    public HttpUtil(String url, Map<String, String> map, Class responseClass, PostCallBack callBack) {
        this.url = url;
        this.responseClass = responseClass;
        this.postResponeCallBack = callBack;
        build = OkHttpUtils.post().url(this.url).params(map).build();
        LogUtil.i(responseClass.getName() + "-->request start:", "-----------------------request start-----------------------");
        LogUtil.i(responseClass.getName() + "-->url:", url);
        LogUtil.i(responseClass.getName() + "-->map:", map.toString());
        LogUtil.i(responseClass.getName() + "-->request end:", "-----------------------request end-----------------------");
    }

    public HttpUtil(String url,Class responseClass,PostCallBack callBack){

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();




    }

    /**
     * @param url           URL
     * @param params        其它参数
     * @param fileKey       接收图片的key
     * @param files         图片信息， key ： fileName  , value : file
     * @param responseClass
     * @param callBack      回调
     */
    public HttpUtil(String url, Map<String, String> params, String fileKey, Map<String, File> files,
                    Class responseClass, PostCallBack callBack) {
        this.url = url;
        this.responseClass = responseClass;
        this.postResponeCallBack = callBack;
        PostFormBuilder post = OkHttpUtils.post().url(url);
        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            File val = (File) entry.getValue();
            post.addFile(fileKey, key, val);
        }
        build = post.params(params).build();
    }

    /**
     * 开始请求 onError 中  1：网络异常  2：数据解析异常  3：未知异常
     */
    public void postExecute() {
        if (build == null) {
            return;
        }
        build.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (postResponeCallBack != null) {
                    ToastUtil.showToast("网络异常,请检查网络连接设置");
                    postResponeCallBack.onError(1, "网络异常,请重试");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Object object = null;
                LogUtil.i(responseClass.getName() + "-->response start:", "-----------------------response start-----------------------");
                LogUtil.i(responseClass.getName() + "-->url:", url);
                LogUtil.i(responseClass.getName() + "-->response:", response);
                LogUtil.i(responseClass.getName() + "-->response end:", "-----------------------response end-----------------------");
                try {
                    object = new Gson().fromJson(response, responseClass);
                } catch (Exception e) {
                    ToastUtil.showToast("数据解析异常");
                    postResponeCallBack.onError(2, "数据解析异常");//数据解析异常
                }
                if (postResponeCallBack != null) {
                    postResponeCallBack.onSuccess(object);
                }
            }
        });
    }

    public void cancel() {
        if (build != null)
            build.cancel();
    }
}
