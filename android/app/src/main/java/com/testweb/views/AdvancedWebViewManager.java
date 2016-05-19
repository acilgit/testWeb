package com.testweb.views;

import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by 18953 on 2016/5/19.
 */
    public class AdvancedWebViewManager extends SimpleViewManager<AdvancedWebView> {

        public static final String REACT_CLASS = "RCTAdvancedWebView";

        @Override
        public String getName() {
            return REACT_CLASS;
        }

        @Override

        protected AdvancedWebView createViewInstance(ThemedReactContext reactContext) {
            AdvancedWebView webView= new AdvancedWebView(reactContext);
           /* webView.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });*/
            return webView;
        }

        @ReactProp(name = "textZoom")
        public void setTextZoom(AdvancedWebView view, @Nullable int zoom) {
            Log.e("TAG", "setTextZoom");
//            Toast.makeText()
            view.getSettings().setTextZoom(zoom);
        }

        @ReactProp(name = "url")
        public void setUrl(AdvancedWebView view, @Nullable String url) {
            Log.e("TAG", "setUrl");
            view.loadUrl(url);
        }

        @ReactProp(name = "html")
        public void setHtml(AdvancedWebView view,@Nullable String html) {
            Log.e("TAG", "setHtml");
            view.loadData(html, "text/html; charset=utf-8", "UTF-8");
        }

    }