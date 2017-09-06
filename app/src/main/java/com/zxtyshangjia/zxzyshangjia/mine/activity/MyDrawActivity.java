package com.zxtyshangjia.zxzyshangjia.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zxtyshangjia.zxzyshangjia.R;
import com.zxtyshangjia.zxzyshangjia.commen.utils.ToastUtil;

/**
 * 我的-抽奖页  加载h5页面
 */

public class MyDrawActivity extends Activity {
    /**
     * 网页
     */
    private WebView webview;
    /**
     *加载进度条
     */

    private ProgressBar p;
    /**
     * url地址
     */
    private String url;

    /**
     *客服按钮
     */
    private ImageView mServicrImg;

    /**
     *  标识
     */
    private int type = -1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_draw);
        webview= (WebView) findViewById(R.id.draw_webview);
        p= (ProgressBar) findViewById(R.id.pb_progress);
        mServicrImg = (ImageView) findViewById(R.id.service_iv);
        mServicrImg.setOnClickListener(mClickListener);
        type = getIntent().getIntExtra("type",1000);
        url = getIntent().getStringExtra("url");

        //启用支持javascript
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl(url);
        webview.canGoBack();
        webview.canGoForward();
        webview.clearHistory();
        webview.clearFormData();
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                   // 网页加载完成  
                    p.setVisibility(View.GONE);
                    if(type == -1000){
                        mServicrImg.setVisibility(View.VISIBLE);
                    }else {
                        mServicrImg.setVisibility(View.GONE);
                    }

                    }else{
                    // 加载中  
                    p.setVisibility(View.VISIBLE);
                    p.setProgress(newProgress);
                    }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webview.canGoBack())
            {
                webview.goBack();//返回上一页面
                return true;
            }
            else
            {

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 和客服聊天
            ToastUtil.showToast("和客服聊天");
        }
    };


}
