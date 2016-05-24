/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 * <p/>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.testweb.views;

/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 * <p/>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.build.ReactBuildConfig;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.webview.WebViewConfig;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Manages instances of {@link WebView}
 *
 * Can accept following commands:
 *  - GO_BACK
 *  - GO_FORWARD
 *  - RELOAD
 *
 * {@link WebView} instances could emit following direct events:
 *  - topLoadingFinish
 *  - topLoadingStart
 *  - topLoadingError
 *
 * Each event will carry the following properties:
 *  - target - view's react tag
 *  - url - url set for the webview
 *  - loading - whether webview is in a loading state
 *  - title - title of the current page
 *  - canGoBack - boolean, whether there is anything on a history stack to go back
 *  - canGoForward - boolean, whether it is possible to request GO_FORWARD command
 */
public class ReactAdvancedWebViewManager extends SimpleViewManager<AdvancedWebView> {

    public static final String REACT_CLASS = "RCTAdvancedWebView";
    private ThemedReactContext reactContext;
    private Activity activity;
//    private static final String REACT_CLASS = "RCTWebView";

    private static final String HTML_ENCODING = "UTF-8";
    private static final String HTML_MIME_TYPE = "text/html; charset=utf-8";

    private static final String HTTP_METHOD_POST = "POST";

    public static final int COMMAND_GO_BACK = 1;
    public static final int COMMAND_GO_FORWARD = 2;
    public static final int COMMAND_RELOAD = 3;
    public static final int COMMAND_STOP_LOADING = 4;

    // Use `webView.loadUrl("about:blank")` to reliably reset the view
    // state and release page resources (including any running JavaScript).
    private static final String BLANK_URL = "about:blank";

    private WebViewConfig mWebViewConfig;

    /**
     * Subclass of {@link WebView} that implements {@link LifecycleEventListener} interface in order
     * to call {@link WebView#destroy} on activty destroy event and also to clear the client
     */


    public ReactAdvancedWebViewManager() {
        mWebViewConfig = new WebViewConfig() {
            public void configWebView(WebView webView) {
            }
        };
    }

    public ReactAdvancedWebViewManager(WebViewConfig webViewConfig) {
        mWebViewConfig = webViewConfig;
    }

//    private class ReactWebViewClient extends AdvancedWebView.AdvancedWebViewClient {
//
//    }

    /**
     * Subclass of {@link WebView} that implements {@link LifecycleEventListener} interface in order
     * to call {@link WebView#destroy} on activty destroy event and also to clear the client
     */
//    private static class ReactWebView extends WebView implements LifecycleEventListener {

        /**
         * WebView must be created with an context of the current activity
         *
         * Activity Context is required for creation of dialogs internally by WebView
         * Reactive Native needed for access to ReactNative internal system functionality
         *
         */
//        public ReactWebView(ThemedReactContext reactContext) {
//            super(reactContext);
//        }
//    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected AdvancedWebView createViewInstance(final ThemedReactContext reactContext) {
        final AdvancedWebView webView = new AdvancedWebView(reactContext) {
            @Override
            public String onReceivedJsMessage(String message)  {
//                JSONObject json = new JSONObject(message);
                WritableMap params = Arguments.createMap();
                params.putString(XiuJavaModule.JsEvents.EVENT_NAME_ReceivedMessageFromWebView_param, message);
                sendEvent(reactContext, XiuJavaModule.JsEvents.EVENT_NAME_ReceivedMessageFromWebView, params);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                return AdvancedJsWebChromeClient.DEFAULT_RESULT_SUCCESS;
            }
        };
        mWebViewConfig.configWebView(webView);
        activity = reactContext.getNativeModule(XiuJavaModule.class).getActivity();
        reactContext.getNativeModule(XiuJavaModule.class).reactContext.addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                webView.onActivityResult(requestCode, resultCode, data);
            }
        });

        webView.setListener(activity, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(String url) {
            }

            @Override
            public void onPageError(int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            }

            @Override
            public void onExternalPageRequest(String url) {
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);

        Log.e("ReactTag", "ReactAdvancedWebViewManager created");

        if (ReactBuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        return webView;
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactProp(name = "javaScriptEnabled")
    public void setJavaScriptEnabled(AdvancedWebView view, boolean enabled) {
        view.getSettings().setJavaScriptEnabled(enabled);
        Log.e("ReactTag", "javaScriptEnabled: "+ enabled);
    }

    @ReactProp(name = "scalesPageToFit")
    public void setScalesPageToFit(AdvancedWebView view, boolean enabled) {
        view.getSettings().setUseWideViewPort(!enabled);
        Log.e("ReactTag", "scalesPageToFit: "+ enabled);
    }

    @ReactProp(name = "domStorageEnabled")
    public void setDomStorageEnabled(AdvancedWebView view, boolean enabled) {
        view.getSettings().setDomStorageEnabled(enabled);
        Log.e("ReactTag", "domStorageEnabled: "+ enabled);
    }

    @ReactProp(name = "userAgent")
    public void setUserAgent(AdvancedWebView view, @Nullable String userAgent) {
        if (userAgent != null) {
            // TODO(8496850): Fix incorrect behavior when property is unset (uA == null)
            view.getSettings().setUserAgentString(userAgent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @ReactProp(name = "mediaPlaybackRequiresUserAction")
    public void setMediaPlaybackRequiresUserAction(AdvancedWebView view, boolean requires) {
        view.getSettings().setMediaPlaybackRequiresUserGesture(requires);
    }

    @ReactProp(name = "injectedJavaScript")
    public void setInjectedJavaScript(AdvancedWebView view, @Nullable String injectedJavaScript) {
        (view).setInjectedJavaScript(injectedJavaScript);
    }

    @ReactProp(name = "source")
    public void setSource(AdvancedWebView view, @Nullable ReadableMap source) {
        if (source != null) {
            Log.e("ReactTag", "source:"+ source.toString());
            if (source.hasKey("html")) {
                String html = source.getString("html");
                if (source.hasKey("baseUrl")) {
                    view.loadDataWithBaseURL(
                            source.getString("baseUrl"), html, HTML_MIME_TYPE, HTML_ENCODING, null);
                } else {
                    view.loadData(html, HTML_MIME_TYPE, HTML_ENCODING);
                }
                return;
            }
            if (source.hasKey("uri")) {
                String url = source.getString("uri");
                if (source.hasKey("method")) {
                    String method = source.getString("method");
                    if (method.equals(HTTP_METHOD_POST)) {
                        byte[] postData = null;
                        if (source.hasKey("body")) {
                            String body = source.getString("body");
                            try {
                                postData = body.getBytes("UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                postData = body.getBytes();
                            }
                        }
                        if (postData == null) {
                            postData = new byte[0];
                        }
                        view.postUrl(url, postData);
                        return;
                    }
                }
                HashMap<String, String> headerMap = new HashMap<>();
                if (source.hasKey("headers")) {
                    ReadableMap headers = source.getMap("headers");
                    ReadableMapKeySetIterator iter = headers.keySetIterator();
                    while (iter.hasNextKey()) {
                        String key = iter.nextKey();
                        headerMap.put(key, headers.getString(key));
                    }
                }
                view.loadUrl(url, headerMap);
                return;
            }
        }
        view.loadUrl(BLANK_URL);
        Log.e("ReactTag", " load:"+ BLANK_URL);
    }

    @Override
    protected void addEventEmitters(ThemedReactContext reactContext, AdvancedWebView view) {
        // Do not register default touch emitter and let WebView implementation handle touches
//        view.setWebViewClient(new AdvancedWebView.AdvancedWebViewClient());
        view.initWebViewClient();
    }

    @Override
    public @Nullable Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "goBack", COMMAND_GO_BACK,
                "goForward", COMMAND_GO_FORWARD,
                "reload", COMMAND_RELOAD,
                "stopLoading", COMMAND_STOP_LOADING);
    }

    @Override
    public void receiveCommand(AdvancedWebView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_GO_BACK:
                root.goBack();
                break;
            case COMMAND_GO_FORWARD:
                root.goForward();
                break;
            case COMMAND_RELOAD:
                root.reload();
                break;
            case COMMAND_STOP_LOADING:
                root.stopLoading();
                break;
        }
    }

    @Override
    public void onDropViewInstance(AdvancedWebView webView) {
        super.onDropViewInstance(webView);
        ((ThemedReactContext) webView.getContext()).removeLifecycleEventListener(webView);
        (webView).cleanupCallbacksAndDestroy();
    }
}
