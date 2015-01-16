package com.vanrui.MediaPaltform.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.vanrui.MediaPaltform.Constants;
import com.vanrui.MediaPaltform.R;

public class webActivity extends Activity {
    WebView webView;
    boolean isBack=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);
        String URL=getIntent().getStringExtra(Constants.Media_Type.WEB);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(URL);
        isBack=false;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(!isBack) {
                    Toast.makeText(getApplicationContext(), "网页加载完成", Toast.LENGTH_SHORT).show();
                }else {
                    isBack=false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!isBack) {
                    Toast.makeText(getApplicationContext(), "正在加载网页", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            isBack=true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
