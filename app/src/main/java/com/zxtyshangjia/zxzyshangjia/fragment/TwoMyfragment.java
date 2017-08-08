package com.zxtyshangjia.zxzyshangjia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zxtyshangjia.zxzyshangjia.R;


/**
 * Created by 18222 on 2017/8/7.
 */

public class TwoMyfragment extends Fragment {
    private boolean hadIntercept;
    private ProgressBar two_progress;
    private WebView two_webview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.mian_two,null);
        two_progress = (ProgressBar) view.findViewById(R.id.two_progress);

        two_webview = (WebView) view.findViewById(R.id.two_webview);
        //启用支持javascript
        WebSettings settings = two_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        two_webview.loadUrl("");
        two_webview.canGoBack();
        two_webview.canGoForward();
        two_webview.clearHistory();
        two_webview.clearFormData();
        two_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        two_webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    // 网页加载完成  
                    two_progress.setVisibility(View.GONE);
                }else{
                    // 加载中  
                    two_progress.setVisibility(View.VISIBLE);
                    two_progress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        return view;
    }




}