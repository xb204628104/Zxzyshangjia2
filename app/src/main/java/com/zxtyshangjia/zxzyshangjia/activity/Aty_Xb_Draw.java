package com.zxtyshangjia.zxzyshangjia.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zxtyshangjia.zxzyshangjia.R;

/**
 * Created by 18222 on 2017/8/8.
 */

public class Aty_Xb_Draw extends Activity {
    //抽奖webview按钮
    private WebView webview;
    private ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_draw);
        webview= (WebView) findViewById(R.id.draw_webview);
        p= (ProgressBar) findViewById(R.id.pb_progress);
        //启用支持javascript
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("http://www.baidu.com/");
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
}
